package gr.aueb.dbnet.tdt.structures;

public class Topic {
	int topicid;
	String topicTitle;
	String language;
	String seminalEvent;
	String topic_explication;
	String background;
	String terminology;
	String timeline;
	String rule_of_Interpretation;
	String seed_Story;
	
	
	public String getTopicTitle() {
		return topicTitle;
	}
	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}
	public String getSeminalEvent() {
		return seminalEvent;
	}
	public void setSeminalEvent(String seminalEvent) {
		this.seminalEvent = seminalEvent;
	}
	public String getTopic_explication() {
		return topic_explication;
	}
	public void setTopic_explication(String topic_explication) {
		this.topic_explication = topic_explication;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	public String getTerminology() {
		return terminology;
	}
	public void setTerminology(String terminology) {
		this.terminology = terminology;
	}
	public String getTimeline() {
		return timeline;
	}
	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}
	public String getRule_of_Interpretation() {
		return rule_of_Interpretation;
	}
	public void setRule_of_Interpretation(String rule_of_Interpretation) {
		this.rule_of_Interpretation = rule_of_Interpretation;
	}
	public String getSeed_Story() {
		return seed_Story;
	}
	public void setSeed_Story(String seed_Story) {
		this.seed_Story = seed_Story;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public int getTopicid() {
		return topicid;
	}
	public void setTopicid(int topicid) {
		this.topicid = topicid;
	}

	public Topic(int topicid, String language) {
		super();
		this.topicid = topicid;
		this.language = language;
	}
}
