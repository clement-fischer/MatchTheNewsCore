package gr.aueb.dbnet.util;

import java.util.LinkedList;
import java.util.Map;
import java.util.Calendar;
import java.util.HashMap;

import gr.aueb.dbnet.evaluation.EvaluationParameters;
import gr.aueb.dbnet.tdt.structures.TDTDocument;

public class NoveltyScorer {

	LinkedList<TDTDocument> slidingWindow;
	int windowSize;
	Map<String, DatedScore> tDF;
	EvaluationParameters ep;
	
	// Constructor
	public	NoveltyScorer(EvaluationParameters ep) {
		slidingWindow = new LinkedList<TDTDocument>();
		this.windowSize = ep.getN();
		this.ep = ep;
		tDF = new HashMap<String, DatedScore>();
	}
	
	// Stream of documents come through this function
	// Updates the field noveltyScore of doc and tDF
	public void nextDocument(TDTDocument doc) {
		doc.setDate_time(Calendar.getInstance());
		updatetDF(doc);
		if (slidingWindow.size() < windowSize) {
			slidingWindow.add(doc);
		}
		else {
			computeNoveltyScore(doc);
			slidingWindow.add(doc);
			slidingWindow.removeFirst();
		}
	}
	
	public void updatetDF(TDTDocument doc) {
		Map<String, Integer> terms = doc.getAnalyzedText();
		if (terms == null)
			return;
		for (String term : terms.keySet()) {
			// if new term, add with score 1 to tDF
			// slidingWindow.peekFirst().getDate_time() = oldest document in window
			// tDF.get(term).date_time = oldest reference to current term
			// if term doesn't exist or is too old, replace by 1
			if (!(tDF.get(term) != null && slidingWindow.peekFirst().getDate_time().compareTo(tDF.get(term).date_time) > 0)){
				tDF.put(term, new DatedScore(1,doc.getDate_time()));
			}
			// else update thanks to formula (12)
			else {
				double newScore = tDF.get(term).score*decayFunction(tDF.get(term).date_time,doc.getDate_time()) + 1;
				tDF.put(term, new DatedScore(newScore,doc.getDate_time()));
			}
		}
	}
	
	public void computeNoveltyScore(TDTDocument doc) {
		double ns = 0;
		Map<String, Integer> terms = doc.getAnalyzedText();
		if (terms == null)
			return;
		for (String term : terms.keySet()) {
			ns += tf(term,doc,ep)*idf(tDF.get(term).score,ep);
		}
		ns /= norm(doc,ep);
		doc.setNoveltyScore(ns);
	}
	
	public double tf(String term, TDTDocument doc, EvaluationParameters ep) {
		double docLength = doc.getTokens().length;
		double tf = doc.getAnalyzedText().get(term) / docLength;
		return tf;
	}
	
	public double idf(double df, EvaluationParameters ep) {
		return Math.log(windowSize/df);
	}
	
	public double norm(TDTDocument doc, EvaluationParameters ep) {
		return 1;
	}
	
	public double decayFunction(Calendar c1, Calendar c2) {
		double x = c1.getTime().getTime() - c2.getTime().getTime();
		// sigmoid function
		return (1/( 1 + Math.pow(Math.E,(-1*x))));
	}
}
