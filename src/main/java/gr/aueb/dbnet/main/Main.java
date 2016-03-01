package gr.aueb.dbnet.main;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import gr.aueb.dbnet.evaluation.CrossValidation;
import gr.aueb.dbnet.evaluation.EvaluationParameters;
import gr.aueb.dbnet.importers.tdt.DocumentImporter;
import gr.aueb.dbnet.tdt.structures.TDTDocument;
import gr.aueb.dbnet.util.NoveltyScorer;
import gr.aueb.dbnet.util.SystemProperties;

public class Main {

	public static void main(String[] args) {

		// Initializing system properties
		SystemProperties systemProperties = new SystemProperties();
		systemProperties.init();

		// Loading the data
		DocumentImporter documentImporter = new DocumentImporter();
		try {
			documentImporter.importData();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		int size = documentImporter.getData().size();
		System.out.println(size + " documents loaded.");
		ArrayList<String> keys = new ArrayList<String>(size);
		keys.addAll(documentImporter.getData().keySet());
		// The following line seems to be unnecessary
		// Collections.sort(keys, DocumentImporter.sortByPreferenceKey);

		// List of 5 parameters. Each set of parameters starts the a test.
		EvaluationParameters ep = new EvaluationParameters(60);
		
		// Add one by one the documents to be processed, give a NS to each document
		
		NoveltyScorer ns = new NoveltyScorer(ep);
		
		double maxScore = 0,minScore = 1000000;
		
		TDTDocument currentDoc;
		int i=0;
		for (String key : keys){
			currentDoc = (TDTDocument) documentImporter.getData().get(key);
			try {
				ns.nextDocument(currentDoc);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			maxScore = Math.max(maxScore, currentDoc.getNoveltyScore());
			minScore = Math.min(minScore, currentDoc.getNoveltyScore());
			System.out.println(currentDoc.getNoveltyScore());
		}
		
		System.out.println(maxScore);
		System.out.println(minScore);
		
		// Cross validation to find the threshold
		// TODO Dangerous cast ?
		CrossValidation cv = new CrossValidation((Map<String, TDTDocument>) documentImporter.getData(), keys, ep);
		double avgCost = cv.compute();
		System.out.println("Parameters:");
		System.out.println(ep.toString());
		System.out.println("Average cost: " + avgCost);

		// Export the results in a csv file
		// TODO
	}
}
