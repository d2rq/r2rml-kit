package org.d2rq.jena;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.FileUtils;

/**
 * A version of FileManager.get().loadModel(...) that supports
 * Java's jar: URLs
 */
public class CompatibilityFileManager {

	public static Model loadModel(String filenameOrURL) {
		return loadModel(filenameOrURL, null, FileUtils.guessLang(filenameOrURL, null));
	}
	
	public static Model loadModel(String filenameOrURL, String lang) {
		return loadModel(filenameOrURL, null, lang);
	}
	
	public static Model loadModel(String filenameOrURL, String baseIRI, String lang) {
		if (!filenameOrURL.toLowerCase().startsWith("jar:")) {
			return FileManager.get().loadModel(filenameOrURL, baseIRI, lang);
		}
		Model result = ModelFactory.createDefaultModel();
		result.read(filenameOrURL, baseIRI, lang);
		return result;
	}
	
	private CompatibilityFileManager() {}
}
