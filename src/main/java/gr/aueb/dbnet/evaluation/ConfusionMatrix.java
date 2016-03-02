package gr.aueb.dbnet.evaluation;

import gr.aueb.dbnet.tdt.structures.TDTDocument;

public class ConfusionMatrix {
	private int tp;
	private int tn;
	private int fp;
	private int fn;
	private double threshold;

	public ConfusionMatrix() {
	}

	public double singleDetectionCost() {
		return (cMiss() * pMiss() * pTarget()) + (cFa() * pFa() * (1 - pTarget()));
	}

	public double cMiss() {
		return 1.;
	}

	public double pMiss() {
		return ((double) fn) / (fn + tp);
	}

	public double pTarget() {
		return 0.5;
	}

	public double cFa() {
		return 1.;
	}

	public double pFa() {
		return ((double) fp) / (fp + tn);
	}

	public void setThreshold(double threshold) {
		tp = 0;
		tn = 0;
		fp = 0;
		fn = 0;
		this.threshold = threshold;
	}

	public void test(TDTDocument document) {
		if (document.getIsFirstStory())
			if (document.getNoveltyScore() < threshold)
				fn++;
			else
				tp++;
		else {
			if (document.getNoveltyScore() < threshold)
				tn++;
			else
				fp++;
		}
	}
}
