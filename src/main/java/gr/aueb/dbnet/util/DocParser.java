package gr.aueb.dbnet.util;

import gr.aueb.dbnet.util.SystemProperties;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tr.ApostropheFilter;

public class DocParser {
	
	public static List<String> parse2List(String doc) {
		
		List<String> tokens = new ArrayList<String>();	
		
		Analyzer analyzer;
		try {
			if (SystemProperties.ELIMINATE_STOPWORDS)
				analyzer = new StandardAnalyzer(new FileReader("stop-words_english_6_en.txt"));
			else
				analyzer = new StandardAnalyzer();
			TokenStream stream  = analyzer.tokenStream(null, new StringReader(doc));
			stream = new LowerCaseFilter(stream);
			//stream = new ApostropheFilter(stream);
			stream = new LengthFilter(stream, 2, 20);
			if (SystemProperties.USE_PORTER_STEMMER)
				stream = new PorterStemFilter(stream);
			stream.reset();
			      
			while (stream.incrementToken())
				tokens.add(stream.getAttribute(OffsetAttribute.class).toString());
			
			stream.end();
			stream.close();
			analyzer.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return tokens;
	}
	
	public static String[] parse2Array(String doc) {
		List<String> tokens = parse2List(doc);
		return tokens.toArray(new String[tokens.size()]);
	}
	
	public static String parse2String(String doc) {
		
		String text = new String("");
		String[] tokens = parse2Array(doc);
		
		for (int i=0; i < tokens.length; i++) 
			text += tokens[i] + " ";
		
		return text;
	}
	
	// Test the parser 
	/*
	public static void main(String...args) {
		SystemProperties p = new SystemProperties();
		p.init();
		String text = "Prospects were dim for resolution of the political crisis in Cambodia in October 1998. Prime Minister Hun Sen insisted that talks take place in Cambodia while opposition leaders Ranariddh and Sam Rainsy, fearing arrest at home, wanted them abroad. King Sihanouk declined to chair talks in either place. A U.S. House resolution criticized Hun Sen's regime while the opposition tried to cut off his access to loans. But in November the King announced a coalition government with Hun Sen heading the executive and Ranariddh leading the parliament. Left out, Sam Rainsy sought the King's assurance of Hun Sen's promise of safety and freedom for all politicians.";
		System.out.println(DocParser.parse2String(text));		
	}
	*/
}
