package gr.aueb.dbnet.main;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

import gr.aueb.dbnet.evaluation.CosineSimilarity;
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

		// Each set of parameters starts the a test.
		LinkedList<EvaluationParameters> listTests = new LinkedList<EvaluationParameters>();
//		listTests.add(new EvaluationParameters("b", "s", "d"));
//		listTests.add(new EvaluationParameters("b", "b", "d"));
//		listTests.add(new EvaluationParameters("n", "s", "d"));
//		listTests.add(new EvaluationParameters("n", "b", "d"));
//		listTests.add(new EvaluationParameters("l", "s", "d"));
//		listTests.add(new EvaluationParameters("l", "b", "d"));
//		listTests.add(new EvaluationParameters("k", "s", "d",0,1));
//		listTests.add(new EvaluationParameters("k", "b", "d",0,1));

		// Add one by one the documents to be processed, give a NS to each document
		NoveltyScorer ns;
		CrossValidation cv;
		
		// create CSV file
		try {
			new PrintWriter("resultsChart.csv", "UTF-8");
		} catch (Exception e){}
		
		// fill CSV file
		try {
			FileWriter filewriter = new FileWriter("resultsChart.csv", true);
			filewriter.append("model;20;60;100;140;180\n");
			filewriter.close();
		} catch (Exception e){}
		
		for(EvaluationParameters ep : listTests) {
			
			// print model in CSV file
			try {
				FileWriter filewriter = new FileWriter("resultsChart.csv", true);
				filewriter.append("\n"+ep.toString());
				filewriter.close();
			} catch (Exception e){}
						
			for (int i = 0;i<5;i++) {
				ep.setN(40*i + 20);
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
				
				// print cost in CSV file
				try {
					FileWriter filewriter = new FileWriter("resultsChart.csv", true);
					filewriter.append(";"+Double.toString(avgCost));
					filewriter.close();
				} catch (Exception e){}
			}
			System.out.println("Done one model");
		}
		
		EvaluationParameters ep = new EvaluationParameters("z", "z", "z");
		CosineSimilarity cs;
		
		try {
			FileWriter filewriter = new FileWriter("resultsChart.csv", true);
			filewriter.append("\n"+"MeanCS");
			filewriter.close();
		} catch (Exception e){}
				
		for (int i = 0;i<5;i++) {
			ep.setN(40*i + 20);
			cs = new CosineSimilarity(ep);
			for (String key : keys) {
				currentDoc = (TDTDocument) documentImporter.getData().get(key);
				try {
					cs.nextDocument(currentDoc);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			// Cross validation to find the threshold
			cv = new CrossValidation((Map<String, TDTDocument>) documentImporter.getData(), keys, ep);
			double avgCost = cv.compute();
			
			// print cost in CSV file
			try {
				FileWriter filewriter = new FileWriter("resultsChart.csv", true);
				filewriter.append(";"+Double.toString(avgCost));
				filewriter.close();
			} catch (Exception e){}
		}
		
	}
}
