package gr.aueb.dbnet.evaluation;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

import gr.aueb.dbnet.tdt.structures.TDTDocument;

public class CosineSimilarity {
	LinkedList<TDTDocument> slidingWindow;
	int windowSize;
	EvaluationParameters ep = new EvaluationParameters("b", "s", "d", 0, 1);

	// Constructor
	public CosineSimilarity(EvaluationParameters ep) {
		slidingWindow = new LinkedList<TDTDocument>();
		windowSize=ep.getN();
	}

	public void nextDocument(TDTDocument doc) throws ParseException {
		if (slidingWindow.size() < windowSize) {
			slidingWindow.add(doc);
		} else {
			computeNoveltyScore(doc);
			slidingWindow.add(doc);
			slidingWindow.removeFirst();
		}
	}

	private void computeNoveltyScore(TDTDocument doc1) {
		double ns=0, sum, sum1, sum2;
		Integer w1, w2;
		Map<String, Integer> terms1 = doc1.getAnalyzedText();
		Map<String, Integer> terms2;
		TreeSet<String> termsSet1 = (TreeSet<String>) terms1.keySet(), termsSet2;
		for (TDTDocument doc2 : slidingWindow) {
			sum = 0;
			sum1 = 0;
			sum2 = 0;
			terms2 = doc2.getAnalyzedText();
			termsSet2 = (TreeSet<String>) terms2.keySet();
			termsSet2.retainAll(termsSet1);
			for (String term : termsSet2) {
				w1 = terms1.get(term);
				w2 = terms2.get(term);
				sum += w1 * w2;
				sum1 += w1 * w1;
				sum2 += w2 * w2;
			}
			ns += sum / Math.sqrt(sum1 * sum2);
		}
		ns/=windowSize;
		doc1.setNoveltyScore(ns);
	}
}
