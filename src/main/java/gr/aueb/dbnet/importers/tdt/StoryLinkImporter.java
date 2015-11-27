package gr.aueb.dbnet.importers.tdt;

import gr.aueb.dbnet.tdt.structures.StoryLink;
import gr.aueb.dbnet.util.SystemProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.stream.Stream;

public class StoryLinkImporter {
	public static HashSet<StoryLink> links=new HashSet<StoryLink>();
	public static HashSet<String> usedDocsIds=new HashSet<String>();
	public static void importData() {

		String data_path=System.getProperty("user.home")+SystemProperties.LINK_DETECTION_FILEPATH;
		Stream<String> lines;
		try {
			lines = Files.lines(Paths.get(data_path));
			lines = lines.filter(s -> !s.startsWith("#"));
			lines.forEachOrdered(s->
						{
							String docid1=s.split(" ")[0].split(":")[1];
							String docid2=s.split(" ")[1].split(":")[1];
							String filename1=s.split(" ")[0].split(":")[0];
							String filename2=s.split(" ")[1].split(":")[0];
							String isTarget=s.split(" ")[2];
							String clusterid=s.split(" ")[3];
							
							if(!filename1.startsWith("mttkn") && !filename2.startsWith("mttkn")){
								links.add(new StoryLink(docid1,docid2 ,filename1 , filename2,isTarget ,clusterid ));
								usedDocsIds.add(docid1);
								usedDocsIds.add(docid2);
							}
						}
					
					);
		    lines.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("DONE IMPORTING LINKS "+links.size());
	}
}
