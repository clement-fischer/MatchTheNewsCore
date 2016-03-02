package gr.aueb.dbnet.main;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

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

		// Set isFirstStory
		TreeSet<String> readTopics = new TreeSet<String>();
		TDTDocument currentDoc;
		for (String key : keys) {
			currentDoc = (TDTDocument) documentImporter.getData().get(key);
			if (readTopics.contains(currentDoc.getLabel())) {
				currentDoc.setIsFirstStory(false);
			} else {
				currentDoc.setIsFirstStory(true);
				readTopics.add(currentDoc.getLabel());
			}

		}
		// TODO Ask why this is not 250 as it is said in the dataset documentation
		System.out.println("Number of topics in the dataset: " + readTopics.size());

		LinkedList<EvaluationParameters> listTests = new LinkedList<EvaluationParameters>();
		// Each set of parameters starts the a test.
		listTests.add(new EvaluationParameters(60, "b", "s", "d", 0, 1));
		listTests.add(new EvaluationParameters(60, "n", "s", "d", 0, 1));
		listTests.add(new EvaluationParameters(60, "k", "b", "d", 0, 1));
		listTests.add(new EvaluationParameters(60, "b", "b", "u", 0, 1));
		listTests.add(new EvaluationParameters(60, "k", "s", "u", 0, 1));
		listTests.add(new EvaluationParameters(60, "l", "b", "n", 0, 1));
		listTests.add(new EvaluationParameters(60, "k", "s", "n", 0, 1));
		listTests.add(new EvaluationParameters(60, "k", "b", "n", 0, 1));

		// Add one by one the documents to be processed, give a NS to each document
		NoveltyScorer ns;
		CrossValidation cv;
		for(EvaluationParameters ep : listTests) {
			ns = new NoveltyScorer(ep);
			for (String key : keys) {
				currentDoc = (TDTDocument) documentImporter.getData().get(key);
				try {
					ns.nextDocument(currentDoc);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			// Cross validation to find the threshold
			cv = new CrossValidation((Map<String, TDTDocument>) documentImporter.getData(), keys, ep);
			double avgCost = cv.compute();
			System.out.println("Parameters:");
			System.out.println(ep.toString());
			System.out.println("Average cost: " + avgCost);
		}

		// Export the results in a csv file
		// TODO
	}
}
