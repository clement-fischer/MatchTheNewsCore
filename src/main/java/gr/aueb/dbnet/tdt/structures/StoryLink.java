package gr.aueb.dbnet.tdt.structures;

public class StoryLink {
	String docid1;
	String docid2;
	String filename1;
	String filename2;
	Boolean isTarget;
	Integer clusterid;
	Double targerStrength;
	public Boolean getIsTarget() {
		return isTarget;
	}
	@Override
	public boolean equals(Object obj) {
		StoryLink object=(StoryLink) obj;
		if(this.docid1.equals(object.docid1) && this.docid2.equals(object.docid2))
			return true;
		else
		return false;
	}
	
	public StoryLink(String docid1, String docid2, String filename1,
			String filename2, String isTarget, String clusterid) {
		super();
		this.docid1 = docid1;
		this.docid2 = docid2;
		this.filename1 = filename1;
		this.filename2 = filename2;
		if(isTarget.equals("TARGET"))
			this.isTarget=true;
		else
			this.isTarget=false;
		this.clusterid = Integer.valueOf(clusterid);
	}
	
	public StoryLink(String docid1, String docid2, Double targerStrength, String clusterid) {
		super();
		this.docid1 = docid1;
		this.docid2 = docid2;
		this.targerStrength=targerStrength;

		this.clusterid = Integer.valueOf(clusterid);
	}
	public Double getTargerStrength() {
		return targerStrength;
	}
	public void setTargerStrength(Double targerStrength) {
		this.targerStrength = targerStrength;
	}
	public void setIsTarget(Boolean isTarget) {
		this.isTarget = isTarget;
	}
	public Integer getClusterid() {
		return clusterid;
	}
	public void setClusterid(Integer clusterid) {
		this.clusterid = clusterid;
	}
	public String getDocid1() {
		return docid1;
	}
	public void setDocid1(String docid1) {
		this.docid1 = docid1;
	}
	public String getDocid2() {
		return docid2;
	}
	public void setDocid2(String docid2) {
		this.docid2 = docid2;
	}
	public String getFilename1() {
		return filename1;
	}
	public void setFilename1(String filename1) {
		this.filename1 = filename1;
	}
	public String getFilename2() {
		return filename2;
	}
	public void setFilename2(String filename2) {
		this.filename2 = filename2;
	}
	
}
