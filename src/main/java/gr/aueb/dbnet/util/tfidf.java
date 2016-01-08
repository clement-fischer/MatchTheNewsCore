package gr.aueb.dbnet.util;

import java.util.List;

//basic tfidf class without stopwords (DocParser)
public class tfidf {

	//tf = number of term appearances / number of terms in doc
	public double tf(List<String> doc, String term) {
	    double result = 0;
	    for (String word : doc) {
	       if (term.equalsIgnoreCase(word))
	              result++;
	       }
	    return result / doc.size();
	}
	
	//idf = log(number of docs / number of docs with at least one appearance of term)
	public double idf(List<List<String>> docs, String term) {
	    double n = 0;
	    for (List<String> doc : docs) {
	        for (String word : doc) {
	            if (term.equalsIgnoreCase(word)) {
	                n++;
	                break;
	            }
	        }
	    }
	    return Math.log(docs.size() / n);
	}
	
	//tfidf = tf*idf
	public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
	    return tf(doc, term) * idf(docs, term);
	}
	
}
