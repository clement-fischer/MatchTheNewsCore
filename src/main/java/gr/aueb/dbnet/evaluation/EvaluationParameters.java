package gr.aueb.dbnet.evaluation;

// TODO
public class EvaluationParameters {
	private int n;
	private String tf;
	private String idf;
	private String norm;
	private double b;
	private double k;
	
	public EvaluationParameters(String tf, String idf, String norm, double b, double k){
		this.tf=tf;
		this.idf=idf;
		this.norm=norm;
		this.b=b;
		this.k=k;
	}
	
	public EvaluationParameters(String tf, String idf, String norm){
		this.tf=tf;
		this.idf=idf;
		this.norm=norm;
		this.b=0;
		this.k=0;
	}
	
	public int getN(){
		return n;
	}
	
	public void setN(int n){
		this.n = n;
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
		String res = tf + idf + norm;
		if (tf=="k")
			res += "-b=" + b;
		return res;
	}
}
