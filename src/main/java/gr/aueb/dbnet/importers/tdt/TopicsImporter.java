package gr.aueb.dbnet.importers.tdt;

import gr.aueb.dbnet.tdt.structures.Topic;
import gr.aueb.dbnet.util.SystemProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * This class imports the Topics and Annotations of the TDT5 Dataset into a Map<Integer,Topic>
 * TODO Add the Rules of Interpretation Field in every Topic
 * @author midas
 *
 */
public class TopicsImporter {
	public static ConcurrentHashMap<Integer,Topic> topics=new ConcurrentHashMap<Integer,Topic>();
	

	public void import_data() {
		String data_path=System.getProperty("user.home")+SystemProperties.TOPICS_BY_LANGUAGE_FILEPATH;
		Stream<String> lines;
		try {
			lines = Files.lines(Paths.get(data_path));
			//lines = lines.filter(s -> ((s.contains("ENG") && (!s.contains("ARB") &&(!s.contains("MAN"))))));
			lines.forEachOrdered(s->topics.put(Integer.valueOf(s.split("\t")[0]),new Topic(Integer.valueOf(s.split("\t")[0]),s.split("\t")[1])));
		    lines.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		data_path=System.getProperty("user.home")+SystemProperties.TOPICS_DESCRIPTION_FILEPATH;
		List<String> htmlDescList=new ArrayList<String>();
		try {
			 htmlDescList = Files.readAllLines(Paths.get(data_path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String htmlDesc = StringUtils.join(htmlDescList, ' ');
		Document doc = Jsoup.parse(htmlDesc);
		String body=doc.body().html();
		String[] blocs=body.split("<hr>");
		for(int i=1;i<blocs.length;i++){
			Document docBlock = Jsoup.parse(blocs[i]);
			Elements b = docBlock.select("b");
			Integer block_id=Integer.valueOf(b.first().text().split("\\.\u00a0")[0]);
			String title=b.first().text().split("\\.\u00a0")[1].substring(1);
			Elements blocks_body=docBlock.select("blockquote");
			Topic t= topics.get(block_id);
			t.setTopicTitle(title);		
			t.setSeminalEvent(blocks_body.get(0).text());
			t.setTopic_explication(blocks_body.get(1).text());
			t.setBackground(blocks_body.get(2).text());
			try{
				t.setTerminology(blocks_body.get(3).text());
			}
			catch(Exception e){
				t.setTerminology(null);
			}
			try{
				t.setTimeline(blocks_body.get(4).text());
			}
			catch(Exception e){
				t.setTimeline(null);
			}
			try{
				t.setSeed_Story(docBlock.text().split("Seed Story: ")[1]);
			}
			catch(Exception e){
				String exceptionStory=docBlock.text().substring(docBlock.text().length()-21, docBlock.text().length());
				t.setSeed_Story(exceptionStory);
			}
			
			topics.put(block_id,t);	
		}
		System.out.println("Done importing Topics");
	}
	
}
