package gr.aueb.dbnet.tdt.structures;

public class WordEdge implements Comparable<WordEdge> {
	private String base;
	private String dest;

	public WordEdge(String base, String dest) {
		this.base = base;
		this.dest = dest;
	}

	public String getBase() {
		return base;
	}

	public String getDest() {
		return dest;
	}

	@Override
	public int hashCode() {
		String s = base + dest;
		return s.hashCode();
	}

	@Override
	public int compareTo(WordEdge o) {
		String s1 = base + dest, s2 = o.getBase() + o.getDest();
		return s1.compareTo(s2);
	}
}
