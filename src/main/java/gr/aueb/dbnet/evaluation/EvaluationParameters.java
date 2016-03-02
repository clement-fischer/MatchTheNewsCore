package gr.aueb.dbnet.evaluation;

// TODO
public class EvaluationParameters {
	private int n;
	private String tf;
	private String idf;
	private String norm;
	private double b;
	private double k;
	
	public EvaluationParameters(int n, String tf, String idf, String norm, double b, double k){
		this.n=n;
		this.tf=tf;
		this.idf=idf;
		this.norm=norm;
		this.b=b;
		this.k=k;
	}
	
	public int getN(){
		return n;
	}
	
	public String getTF(){
		return tf;
	}
	
	public String getIDF(){
		return idf;
	}
	
	public String getNORM(){
		return norm;
	}
	
	public double getB(){
		return b;
	}
	
	public double getK(){
		return k;
	}
	
	// TODO
	public String toString(){
		return tf + idf + norm + " (" + n + ")";
	}
}
