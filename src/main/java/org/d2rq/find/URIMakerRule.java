package org.d2rq.find;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.d2rq.algebra.TripleRelation;
import org.d2rq.db.expr.Expression;
import org.d2rq.db.schema.ColumnName;
import org.d2rq.nodes.FixedNodeMaker;
import org.d2rq.nodes.NodeMaker;
import org.d2rq.nodes.NodeMakerVisitor;
import org.d2rq.nodes.NodeSetFilter;
import org.d2rq.nodes.TypedNodeMaker;
import org.d2rq.values.BlankNodeIDValueMaker;
import org.d2rq.values.TemplateValueMaker;
import org.d2rq.values.Translator;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.core.Var;


/**
 * <p>The URI maker rule states that any URI that matches a URI pattern is not contained
 * in a URI column. The reasoning is that lookup of a node in a URI pattern is relatively
 * quick -- often it requires just an integer lookup in a primary key table -- but
 * lookup in URI columns may require multiple full table scans. Since URI lookup is
 * such a common operation, this rule can help a lot by reducing full table scans.</p>
 * 
 * <p>Checking a number of NodeMakers against the rule works like this:</p>
 * 
 * <ol>
 * <li>An URIMakerRuleChecker is created using {@link #createRuleChecker(Node)},</li>
 * <li>node makers are added one by one to the rule checker,</li>
 * <li>as soon as a NodeMaker backed by an URI pattern has matched, subsequent
 *   calls to {@link URIMakerRuleChecker#canMatch(NodeMaker)} will return false
 *   if the argument is backed by a URI column.</li>
 * </ol>
 * 
 * <p>Performance is best when all candidate NodeMakers backed by URI patterns are
 * sent to the rule checker before any NodeMaker backed by a URI column. For this
 * purpose, {@link #sortRDFRelations(Collection)} sorts a collection of
 * RDFRelations in an order that puts URI patterns first.</p>
 * 
 * @author Richard Cyganiak (richard@cyganiak.de)
 */
public class URIMakerRule implements Comparator<TripleRelation> {
	private Map<NodeMaker, URIMakerIdentifier> identifierCache = new HashMap<NodeMaker, URIMakerIdentifier>();

	public List<TripleRelation> sortRDFRelations(Collection<TripleRelation> tripleRelations) {
		ArrayList<TripleRelation> results = new ArrayList<TripleRelation>(tripleRelations);
		Collections.sort(results, this);
		return results;
	}

	public URIMakerRuleChecker createRuleChecker(Node node) {
		return new URIMakerRuleChecker(node);
	}
	
	public int compare(TripleRelation o1, TripleRelation o2) {
		int priority1 = priority(o1);
		int priority2 = priority(o2);
		if (priority1 > priority2) {
			return -1;
		}
		if (priority1 < priority2) {
			return 1;
		}
		return 0;
	}
	
	private int priority(TripleRelation relation) {
		int result = 0;
		for (Var var: TripleRelation.SPO) {
			URIMakerIdentifier id = uriMakerIdentifier(relation.nodeMaker(var));
			if (id.isURIPattern()) {
				result += 3;
			}
			if (id.isURIColumn()) {
				result -= 1;
			}
		}
		return result;
	}

	private URIMakerIdentifier uriMakerIdentifier(NodeMaker nodeMaker) {
		URIMakerIdentifier cachedIdentifier = (URIMakerIdentifier) this.identifierCache.get(nodeMaker);
		if (cachedIdentifier == null) {
			cachedIdentifier = new URIMakerIdentifier(nodeMaker);
			nodeMaker.describeSelf(cachedIdentifier);
			this.identifierCache.put(nodeMaker, cachedIdentifier);
		}
		return cachedIdentifier;
	}
	
	private class URIMakerIdentifier implements NodeSetFilter {
		private boolean isURIMaker = false;
		private boolean isColumn = false;
		private boolean isPattern = false;
		URIMakerIdentifier(NodeMaker nodeMaker) {
			nodeMaker.describeSelf(this);
		}
		boolean isURIColumn() { return this.isURIMaker && this.isColumn; }
		boolean isURIPattern() { return this.isURIMaker && this.isPattern; }
		public void limitTo(Node node) { }
		public void limitToBlankNodes() { }
		public void limitToEmptySet() { }
		public void limitToLiterals(String language, RDFDatatype datatype) { }
		public void limitToURIs() { this.isURIMaker = true; }
		public void limitValues(String constant) { }
		public void limitValuesToColumn(ColumnName column) { this.isColumn = true; }
		public void limitValuesToBlankNodeID(BlankNodeIDValueMaker id) { }
		public void limitValuesToPattern(TemplateValueMaker pattern) { this.isPattern = true; }
		public void limitValuesToExpression(Expression expression) { }
		public void setUsesTranslator(Translator translator) { }
	}
	
	public class URIMakerRuleChecker {
		private final Node node;
		private boolean canMatchURIColumn = true;
		public URIMakerRuleChecker(Node node) {
			this.node = node;
		}
		public void addPotentialMatch(NodeMaker nodeMaker) {
			if (!uriMakerIdentifier(nodeMaker).isURIPattern()) return;
			nodeMaker.accept(new NodeMakerVisitor() {
				public void visitEmpty() {}
				public void visit(FixedNodeMaker nodeMaker) {
					if (nodeMaker.getFixedNode().equals(node)) {
						canMatchURIColumn = false;
					}
				}
				public void visit(TypedNodeMaker nodeMaker) {
					if (!nodeMaker.getNodeType().matches(node)) return;
					String value = nodeMaker.getNodeType().extractValue(node);
					if (value == null) return;
					if (!nodeMaker.getValueMaker().matches(value)) return;
					canMatchURIColumn = false;
				}
			});
		}
		public boolean canMatch(NodeMaker nodeMaker) {
			return this.canMatchURIColumn || !uriMakerIdentifier(nodeMaker).isURIColumn();
		}
	}
}
