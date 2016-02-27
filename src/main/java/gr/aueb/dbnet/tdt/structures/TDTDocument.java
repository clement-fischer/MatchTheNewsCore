package gr.aueb.dbnet.tdt.structures;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TDTDocument extends Document implements Comparable<TDTDocument> {
	
	Calendar date_time;
	List<String> keywords;
	String headline;	
	Boolean isFirstStory;
	double noveltyScore;
	
	

	public TDTDocument(String docno, String text) {
		super(docno, text);
	}
	
	public TDTDocument(String docno, String text,String topic) {
		super(docno, text,topic);
	}
	
	public Calendar getDate_time() {
		return date_time;
	}
	
	public void setDate_time(Calendar date_time) {
		this.date_time = date_time;
	}
	
	public List<String> getKeywords() {
		return keywords;
	}
	
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	
	public String getHeadline() {
		return headline;
	}
	
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	
	public Boolean getIsFirstStory() {
		return isFirstStory;
	}
	public void setIsFirstStory(Boolean isFirstStory) {
		this.isFirstStory = isFirstStory;
	}
	
	public double getNoveltyScore(){
		return noveltyScore;
	}
	
	public void setNoveltyScore(double score){
		this.noveltyScore = score;
	}
	
	// Compare TDTDocuments
	// Return +1 if this document is more recent than the TDTDocument given in the argument
	// Return -1 if this document is older than the TDTDocument given in the argument
	// Return 0 otherwise
	@Override
	public int compareTo(TDTDocument o) {
		TDTDocument doc2=(TDTDocument) o;
		String comparator1=this.docno.substring(3);
		String comparator2=doc2.docno.substring(3);
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
		//AFE20030401.0000.0005
		
		return 0;
	}

}
