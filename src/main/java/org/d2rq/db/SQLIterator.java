package org.d2rq.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.d2rq.D2RQException;
import org.d2rq.db.schema.ColumnList;
import org.d2rq.lang.Database;

import com.hp.hpl.jena.query.QueryCancelledException;
import com.hp.hpl.jena.util.iterator.ClosableIterator;


/**
 * Executes an SQL query and delivers result rows as an iterator over {@link ResultRow}s.
 * The query is executed lazily. This class logs all executed SQL queries.
 *
 * @author Chris Bizer chris@bizer.de
 * @author Richard Cyganiak (richard@cyganiak.de)
 */
public class SQLIterator implements ClosableIterator<ResultRow> {
	private final static Log log = LogFactory.getLog(SQLIterator.class);
	private String sql;
	private ColumnList columns;
	private SQLConnection database;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private ResultRow prefetchedRow = null;
	private boolean queryExecuted = false;
	private boolean explicitlyClosed = false;
	private boolean cancelled = false;

	public SQLIterator(String sql, ColumnList columns, SQLConnection db) {
		this.sql = sql;
		this.columns = columns;
		this.database = db;
    }

	public boolean hasNext() {
		if (cancelled) {
			throw new QueryCancelledException();
		}
		if (explicitlyClosed) {
			return false;
		}
		if (prefetchedRow == null) {
		    ensureQueryExecuted();
			tryFetchNextRow();
		}
		return prefetchedRow != null;
	}

	/**
	 * @return The next query ResultRow.
	 */
	public ResultRow next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		ResultRow result = this.prefetchedRow;
		this.prefetchedRow = null;
		return result;
	}

	/**
	 * @deprecated Use {@link #next()} instead
	 */
	public ResultRow nextRow() {
		return next();
	}

	private synchronized void tryFetchNextRow() {
	    if (this.resultSet == null) {
	    	this.prefetchedRow = null;
	    	return;
	    }
		try {
			if (!this.resultSet.next()) {
				this.resultSet.close();
				this.resultSet = null;
		    	this.prefetchedRow = null;
		    	return;
			}
			prefetchedRow = ResultRow.fromResultSet(resultSet, columns, database);
		} catch (SQLException ex) {
			throw new D2RQException(ex);
		}
	}
	
	/**
	 * Make sure the SQL result set is closed and freed. Will auto-close when the
	 * record-set is exhausted.
	 */
	public void close() {
		if (explicitlyClosed) return;
		log.debug("Closing SQLIterator");
	    explicitlyClosed = true;
	    
	    /* JDBC 4+ requires manual closing of result sets and statements */
	    if (this.resultSet != null) {
			try {
				this.resultSet.close();
			} catch (SQLException ex) {
				throw new D2RQException(ex.getMessage() + "; query was: " + this.sql);
			}
	    }
	    
	    if (this.database != null) {
			try {
				this.database.vendor().beforeClose(this.database.connection());
			} catch (SQLException ex) {
				throw new D2RQException(ex.getMessage() + "; query was: " + this.sql);
			}
	    }
	    if (this.statement != null) {
			try {
				this.statement.close();
			} catch (SQLException ex) {
				throw new D2RQException(ex.getMessage() + "; query was: " + this.sql);
			}
	    }

	    if (this.database != null) {
			try {
				this.database.vendor().afterClose(this.database.connection());
			} catch (SQLException ex) {
				throw new D2RQException(ex.getMessage() + "; query was: " + this.sql);
			}
	    }
	}

	public synchronized void cancel() {
		cancelled = true;
		if (statement != null) {
			try {
				database.vendor().beforeCancel(database.connection());
				statement.cancel();
				database.vendor().afterCancel(database.connection());
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		}
	}
	
	public void remove() {
		throw new RuntimeException("Operation not supported");
	}

	private void ensureQueryExecuted() {
	    if (this.queryExecuted) {
	    	return;
	    }
    	this.queryExecuted = true;
    	log.info(sql);
        try {
			Connection con = this.database.connection();
			this.statement = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			if (database.fetchSize() != Database.NO_FETCH_SIZE) {
				try {
					this.statement.setFetchSize(database.fetchSize());
				}
				catch (SQLException e) {} /* Some drivers don't support fetch sizes, e.g. JDBC-ODBC */
			}

			database.vendor().beforeExecuteQuery(con);
			this.resultSet = this.statement.executeQuery(this.sql);
			database.vendor().afterExecuteQuery(con);

			log.debug("SQL result set created");
        } catch (SQLException ex) {
        	if (cancelled) {
        		log.debug("SQL query execution cancelled", ex);
        		throw new QueryCancelledException();
        	}
        	throw new D2RQException(ex.getMessage() + ": " + this.sql);
        }
    }
}
