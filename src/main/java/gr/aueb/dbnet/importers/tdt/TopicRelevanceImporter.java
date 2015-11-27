package gr.aueb.dbnet.importers.tdt;

import gr.aueb.dbnet.util.SystemProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TopicRelevanceImporter {
	
	private static final Logger logger = Logger.getLogger(TopicRelevanceImporter.class.getName());
	
	public  ConcurrentHashMap<String,String> topic_rel = new ConcurrentHashMap<String,String>();
	public  HashSet<String> usedDocsIds = new HashSet<String>();
	
	public  void import_data() {
		String data_path=System.getProperty("user.home")+SystemProperties.TOPICS_RELEVANCE_FILEPATH;
		Stream<String> lines;
		try {
			lines = Files.lines(Paths.get(data_path));
			lines = lines.filter(s -> ((!s.contains("TOPICSET"))));
			lines.forEachOrdered(s->{	
				Document rel_doc;
				Element el;
				String atr1;
				String atr2;
				try {
						rel_doc = Jsoup.parse(s);
						if( rel_doc.body().getElementsByIndexEquals(0).size()==1){
							el = rel_doc.body().getElementsByIndexEquals(0).first();
							atr1 = el.attr("docno");
							usedDocsIds.add(atr1);
							atr2 = el.attr("topicid");
							topic_rel.put(atr1,atr2);
						}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		    lines.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
		
		logger.log(Level.INFO, String.format("Done Importing Topic Relevance Annotations"));
	}

}
