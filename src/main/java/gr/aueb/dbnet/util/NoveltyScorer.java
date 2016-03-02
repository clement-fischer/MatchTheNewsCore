package gr.aueb.dbnet.util;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import gr.aueb.dbnet.evaluation.EvaluationParameters;
import gr.aueb.dbnet.tdt.structures.TDTDocument;

public class NoveltyScorer {

	LinkedList<TDTDocument> slidingWindow;
	int windowSize;
	Map<String, DatedScore> tDF;
	EvaluationParameters ep;
	double avdl;
	
	// Constructor
	public	NoveltyScorer(EvaluationParameters ep) {
		slidingWindow = new LinkedList<TDTDocument>();
		this.windowSize = ep.getN();
		this.ep = ep;
		tDF = new HashMap<String, DatedScore>();
		avdl = 0;
	}
	
	// Stream of documents come through this function
	// Updates the field noveltyScore of doc and tDF
	public void nextDocument(TDTDocument doc) throws ParseException {
		avdl = (avdl * slidingWindow.size() + doc.getTokens().length) / (slidingWindow.size() + 1);
		String comparator1=doc.getDocno().substring(3);
		DateFormat format = new SimpleDateFormat("yyyyMd", Locale.ENGLISH);
		Date date = format.parse(comparator1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		doc.setDate_time(cal);
		updatetDF(doc);
		if (slidingWindow.size() < windowSize) {
			slidingWindow.add(doc);
		}
		else {
			computeNoveltyScore(doc);
			slidingWindow.add(doc);
			TDTDocument oldest = slidingWindow.removeFirst();
			avdl = (avdl * (slidingWindow.size()+1) - oldest.getTokens().length) / slidingWindow.size();
		}
		//Random r = new Random(); 
	    //doc.setNoveltyScore(r.nextDouble());
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
			ns += tf(term,doc)*idf(tDF.get(term).score);
		}
		ns /= norm(doc);
		doc.setNoveltyScore(ns);
	}
	
	public double tf(String term, TDTDocument doc) {
		double docLength = doc.getTokens().length;
		double tf = doc.getAnalyzedText().get(term) / docLength;
		if (ep.getTF() == "b")
			return doc.getAnalyzedText().get(term) != null ? 1 : 0;
		else if (ep.getTF() == "n")
			return tf;
		else if (ep.getTF() == "l")
			return 1 + Math.log(tf);
		else if (ep.getTF() == "k")
			return (ep.getK() + 1)*tf/(ep.getK()*(1 - ep.getB() + ep.getB()*docLength/avdl) + tf);
		return 0;
	}
	
	public double idf(double df) {
		if (ep.getIDF() == "t")
			return Math.log(windowSize/df);
		else if (ep.getIDF() == "s")
			return Math.log((windowSize+1)/(df+0.5));
		else if (ep.getIDF() == "p")
			return Math.log((windowSize-df)/df);
		else if (ep.getIDF() == "b")
			return Math.log((windowSize-df+0.5)/(df+0.5));
		return 0;
	}
	
	public double norm(TDTDocument doc) {
		double docLength = doc.getTokens().length;
		if (ep.getNORM() == "n")
			return 1;
		else if (ep.getNORM() == "u")
			return doc.getAnalyzedText().size();
		else if (ep.getNORM() == "d") {
			double norm = 0;
			for (String term : doc.getAnalyzedText().keySet())
				norm += doc.getAnalyzedText().get(term) / docLength;
			return norm;
		}
		else if (ep.getNORM() == "c") {
			double snorm = 0;
			for (String term : doc.getAnalyzedText().keySet())
				snorm += Math.pow(doc.getAnalyzedText().get(term) / docLength,2);
			return Math.sqrt(snorm);
		}
		else if (ep.getNORM() == "p")
			return 1 - ep.getB() + ep.getB()*docLength/avdl;
		return 1;
	}
	
	public double decayFunction(Calendar c1, Calendar c2) {
		double x = (c1.getTime().getTime() - c2.getTime().getTime())/1000/3600/24/10; // échelle de temps = 1 mois
		// sigmoid function
		return (1/( 1 + Math.pow(Math.E,(-1*x))));
	}
}
