package gr.aueb.dbnet.tdt.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import gr.aueb.dbnet.util.DocParser;

public class Document {

	String docno;
	String text;
	String label;
	Map<String, Integer> analyzedText;
	String[] tokens;
	List<String> sentences;
	List<List<String>> analyzedSentences;
	final int windowSize = 4;

	public Document(String docno, String text, String label) {
		this.docno = docno;
		this.text = text;
		tokens = tokenize(text);

		analyzedText = analyzeText(text, false);
	}

	public Document(String docno, String text, boolean isGraphOfWords) {
		this.docno = docno;
		this.text = text;
		tokens = tokenize(text);

		if (isGraphOfWords) {
			sentences = splitSentences(text);
			analyzedSentences = analyzeSentences(sentences);
			analyzedText = analyzeText(text, true);
		} else {
			analyzedText = analyzeText(text, false);
		}
	}

	public Document(String docno, String text, boolean isGraphOfWords, String label) {
		this(docno, text, isGraphOfWords);
		this.label = label;
	}

	@Override
	public String toString() {

		String s = "";
		if (tokens.length == 0)
			return s;
		s = "docno=" + this.docno.substring(0, docno.length() - 2) + "\t";
		// s+="text="+this.text+"\t";
		s += "tokens=";
		for (String t : this.tokens)
			s += t + " ";
		s += "\t";
		s += "label=" + this.label + "\t";
		return s + "\n";
	}

	private List<String> splitSentences(String text) {
		String[] s = text.split("[\\.!?;]");
		return Arrays.asList(s);
	}

	private String[] tokenize(String text) {
		return DocParser.parse2Array(text);
	}

	private Map<String, Integer> analyzeText(String text, boolean graphOfWords) {
		if (graphOfWords)
			return buildGraphOfWords(windowSize);
		else
			return analyzeText(text);

	}

	private Map<String, Integer> buildGraphOfWords(int windowSize) {
		Map<String, Integer> res = new HashMap<String, Integer>();
		TreeSet<WordEdge> edges = new TreeSet<WordEdge>();
		WordEdge e;
		for (List<String> sentence : analyzedSentences) {
			for (int i = 0; i < sentence.size() - 1; i++) {
				for (int j = i + 1; (j - i < windowSize) && (j < sentence.size()); j++) {
					e = new WordEdge(sentence.get(i), sentence.get(j));
					if (!edges.contains(e)) {
						edges.add(e);
						res.put(e.getDest(), 1);
					}
				}
			}
		}
		return res;

	}

	// Bag of words
	private Map<String, Integer> analyzeText(String text) {
		Map<String, Integer> res = new HashMap<String, Integer>();
		String[] doc = tokens;
		for (int i = 0; i < doc.length; i++) {
			if (res.containsKey(doc[i]))
				res.put(doc[i], res.get(doc[i]) + 1);
			else
				res.put(doc[i], 1);
		}

		return res;
	}

	private List<List<String>> analyzeSentences(List<String> sentences) {
		List<List<String>> res = new ArrayList<List<String>>();
		String[] sentence;
		List<String> tokens;

		for (String s : sentences) {
			sentence = DocParser.parse2Array(s);
			tokens = new ArrayList<String>();
			for (int i = 0; i < sentence.length; i++)
				tokens.add(sentence[i]);

			res.add(tokens);
		}

		return res;
	}

	public String getDocno() {
		return docno;
	}

	public void setDocno(String docno) {
		this.docno = docno;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Map<String, Integer> getAnalyzedText() {
		return analyzedText;
	}

	public List<String> getSentences() {
		String[] s = text.split("[\\.!?;]");
		return Arrays.asList(s);
	}

	public List<List<String>> getAnalyzedSentences() {
		return analyzedSentences;
	}

	public String[] getTokens() {
		return tokens;
	}

}
