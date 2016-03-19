package org.d2rq;

import java.util.Collection;

import org.apache.jena.iri.IRI;
import org.apache.jena.iri.IRIFactory;
import org.d2rq.algebra.TripleRelation;
import org.d2rq.jena.CompatibilityFileManager;
import org.d2rq.lang.D2RQReader;
import org.d2rq.lang.Mapping;
import org.d2rq.vocab.D2RQ;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.shared.impl.PrefixMappingImpl;
import com.hp.hpl.jena.vocabulary.RDF;


/**
 * Test suite for D2RQ, including various helper methods.
 *
 * @author Richard Cyganiak (richard@cyganiak.de)
 */
public class D2RQTestUtil {
	
	public static final String DummyDatabase = "http://d2rq.org/terms/test#DummyDatabase";
	public static final PrefixMapping STANDARD_PREFIXES = new PrefixMappingImpl() {{
		setNsPrefixes(PrefixMapping.Standard);
		setNsPrefix("d2rq", "http://www.wiwiss.fu-berlin.de/suhl/bizer/D2RQ/0.1#");
		setNsPrefix("jdbc", "http://d2rq.org/terms/jdbc/");
		setNsPrefix("test", "http://d2rq.org/terms/test#");
		setNsPrefix("ex", "http://example.org/");
		setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");
	}};

	public static final String[] SKIPPED_DIRECT_MAPPING_TESTS = {
		// We implement the non-duplicate-preserving version
		// of the Direct Mapping, so are allowed to skip these:
		// http://www.w3.org/TR/r2rml/#dfn-duplicate-row-preservation
		"D005-1table3columns3rows2duplicates", 
		"D012-2tables2duplicates0nulls",
		// We strip trailing spaces from CHAR columns on retrieval.
		// We assume that this is almost always what users want, and
		// we don't want to expose this detail of SQL in the RDF.
		"D018-1table1primarykey2columns3rows",
	};
	
	public static final String[] SKIPPED_R2RML_TESTS = {
		// This uses an undefined URI rr:SQL1979 in the mapping, and considers
		// this an error. It should be considered a warning only. So we
		// believe this test case is in error.
		"tc0003a",
		// This uses the language tags "..."@english and "..."@spanish.
		// While these are nonsensical, they are not syntactically invalid,
		// and therefore the requirement to detect them seems overly
		// taxing. We believe this test case is in error.
		"tc0015b",
		//TODO: Known limitation: We don't detect data errors.
		"tc0019b",
		"tc0020b",
		//TODO: Known limitation: We don't support named graphs.
		"tc0006a",
		"tc0007b",
		"tc0007e",
		"tc0007f",
		"tc0008a",
		"tc0009b",
		// We strip trailing spaces from CHAR columns on retrieval.
		// We assume that this is almost always what users want, and
		// we don't want to expose this detail of SQL in the RDF.
		"tc0018a",
	};
	
	public static Model loadTurtle(String fileName) {
		String url = getResourceURL(fileName);
		return CompatibilityFileManager.loadModel(url, url, "TURTLE");
	}
	
	public static Collection<TripleRelation> loadPropertyBridges(String mappingFileName) {
		Model m = loadTurtle(mappingFileName);
		Resource dummyDB = m.getResource(DummyDatabase);
		dummyDB.addProperty(RDF.type, D2RQ.Database);
		Mapping mapping = new D2RQReader(m, "http://example.org/").getMapping();
		return mapping.compile().getTripleRelations();
	}

	public static String getResourceURL(String fileName) {
		return D2RQTestUtil.class.getResource("/" + fileName).toString();
	}
	
	/**
	 * Parses a D2RQ mapping from a file located relative to
	 * the {@link D2RQTestUtil} directory.
	 * 
	 * @param testFileName Filename, relative to {@link D2RQTestUtil}'s location
	 * @return A mapping
	 * @throws D2RQException On error during parse
	 */
	public static Mapping loadMapping(String testFileName) {
		return new D2RQReader(
				loadTurtle(testFileName), 
				"http://example.org/").getMapping();
	}
	
	public static IRI createIRI(String iri) {
		return customIRIFactory.construct(iri);
	}
	// We use a custom IRIFactory that doesn't complain about
	// the jar: scheme being unregistered.
	// TODO: Has this been fixed in Jena post 2.9.3?
	private static final IRIFactory customIRIFactory;
	static {
		customIRIFactory = new IRIFactory(IRIFactory.semanticWebImplementation());
		customIRIFactory.setIsError(IRIFactory.UNREGISTERED_IANA_SCHEME, false);
	}
}