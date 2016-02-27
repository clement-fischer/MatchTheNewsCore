package gr.aueb.dbnet.importers.tdt;

import gr.aueb.dbnet.importers.Importer;
import gr.aueb.dbnet.tdt.structures.TDTDocument;
import gr.aueb.dbnet.util.MapUtil;
import gr.aueb.dbnet.util.SystemProperties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.random.MersenneTwister;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class DocumentImporter extends Importer {
	final Logger logger = LoggerFactory.getLogger(DocumentImporter.class);

   
	private Map<String, TDTDocument> tdt_documents;
	private ConcurrentHashMap<String, TDTDocument> train ;
	private ConcurrentHashMap<String, TDTDocument> test;
	

	public  void importData(){
		TreeMap<String,TDTDocument> documents_tkn = new TreeMap<String, TDTDocument>(sortByPreferenceKey);
		String data_path = System.getProperty("user.home")+SystemProperties.DOCUMENT_FOLDER_IMPORT_TKN;
		String suffix=".tkn_sgm";
		TopicRelevanceImporter tpcRelImp = new TopicRelevanceImporter();
		tpcRelImp.import_data();
		ConcurrentHashMap<String, String> docs_topics=tpcRelImp.topic_rel;
		tpcRelImp.usedFiles.forEach(f->{
			Path filePath = Paths.get(data_path+f+suffix);
				
			Boolean isEnglish= filePath.toString().endsWith("ENG.tkn_sgm") && (SystemProperties.READ_ONLY_ENGLISH_DOCUMENTS);
			Boolean toRead=false;
			if(SystemProperties.READ_ONLY_ENGLISH_DOCUMENTS && isEnglish){
				toRead=true;
			}
			else if(SystemProperties.READ_ONLY_ENGLISH_DOCUMENTS && !isEnglish){
				toRead=false;
			}

			if ((Files.isRegularFile(filePath)) && toRead ) {
				List<String>htmlDocList = null;
				try {
					htmlDocList = Files.readAllLines((filePath));
				} catch (IOException e) {
					e.printStackTrace();
				}
				String htmlDesc = StringUtils.join(htmlDocList, ' ');
				Document docs = Jsoup.parse(htmlDesc);
				Elements all_docs=docs.select("doc");
				for(Element d:all_docs){
					if(!tpcRelImp.usedDocsIds.contains(d.select("docno").text()))
						continue;
					documents_tkn.put(d.select("docno").text(), new TDTDocument(d.select("docno").text(), d.select("text").text(),docs_topics.get(d.select("docno").text())));			
				}
			}
		});
		//TODO TEST
		Set<Entry<String, TDTDocument>> set = documents_tkn.entrySet();
		for(Entry<String, TDTDocument> e:set){
			if(tpcRelImp.usedTopics.contains(e.getValue().getLabel())){
				tpcRelImp.usedTopics.remove(e.getValue().getLabel());
				e.getValue().setIsFirstStory(true);		
			}
		}
		
		tdt_documents=documents_tkn;
	}

	
	@Override
	public Map<String, ? extends gr.aueb.dbnet.tdt.structures.Document> getTrainDocuments() {
		return null;
	}

	@Override
	public Map<String, ? extends gr.aueb.dbnet.tdt.structures.Document> getTestDocuments() {
		return null;
	}

	@Override
	public Map<String, ? extends gr.aueb.dbnet.tdt.structures.Document> getData() {
		return tdt_documents;
	}
	
	// Compare keys
	// Return +1 if this key is more recent than the key given in the argument
	// Return -1 if this key is older than the key given in the argument
	// Return 0 otherwise
	public static Comparator<String> sortByPreferenceKey = new Comparator<String>(){
	    public int compare(String o1, String o2) {
	    	String comparator1=o1.substring(3);
			String comparator2=o2.substring(3);
			DateFormat format = new SimpleDateFormat("yyyyMd", Locale.ENGLISH);
			try {
				Date date1 = format.parse(comparator1);
				Date date2 = format.parse(comparator2);
				if(date1.compareTo(date2)>0)
					return 1;
				else if(date1.compareTo(date2)<0)
					return -1;
				else{
					 String c1 = comparator1.substring(9);
					 String c2 = comparator2.substring(9);
					 Double comparator11 = Double.valueOf(c1);
					 Double comparator21 = Double.valueOf(c2);
					 if(comparator11>comparator21)
						 return 1;
					 else  if(comparator11<comparator21)
						 return -1;
					 else
						return 0;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return 0;
	    }
	};

}
