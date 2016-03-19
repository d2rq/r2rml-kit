package org.d2rq.jena;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.d2rq.D2RQTestUtil;
import org.d2rq.HSQLDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class JenaAPITest {
	private HSQLDatabase db;
	
	@Before
	public void setUp() {
		db = new HSQLDatabase("test");
		db.executeSQL("CREATE TABLE \"People\" (\"ID\" INT PRIMARY KEY)");
	}
	
	@Test
	public void testCopyPrefixesFromMapModelToD2RQModel() {
		ModelD2RQ m = new ModelD2RQ(D2RQTestUtil.getResourceURL("jena/prefixes.ttl"));
		assertEquals("http://example.org/", m.getNsPrefixURI("ex"));
	}
	
	@Test
	public void testCopyPrefixesFromMapModelToD2RQGraph() {
		GraphD2RQ g = new ModelD2RQ(
				D2RQTestUtil.loadTurtle("jena/prefixes.ttl"), null).getGraph();
		assertEquals("http://example.org/", g.getPrefixMapping().getNsPrefixURI("ex"));
	}
	
	@Test
	public void testDontCopyD2RQPrefixFromMapModel() {
		ModelD2RQ m = new ModelD2RQ(D2RQTestUtil.getResourceURL("jena/prefixes.ttl"));
		assertNull(m.getNsPrefixURI("d2rq"));
	}
	
	@After
	public void tearDown() {
		db.close(true);
	}
}
