package gr.aueb.dbnet.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import gr.aueb.dbnet.tdt.structures.TDTDocument;

public class CrossValidation {
	private int nbFolds = 5;
	private List<String> keysForCrossValidation;
	private Map<String, TDTDocument> tdt_documents;

	public CrossValidation(Map<String, TDTDocument> map, ArrayList<String> keys, EvaluationParameters evalParams) {
		keysForCrossValidation = keys.subList(evalParams.getN(), keys.size());
		this.tdt_documents = map;
	}

	public double compute() {
		Collections.shuffle(keysForCrossValidation);
		ArrayList<ArrayList<String>> folds = getFolds();

		ArrayList<String> trainingKeys = new ArrayList<String>(keysForCrossValidation.size());
		ArrayList<String> testingKeys = null;

		// TODO Get costs and thresholds out !!
		double[] costs = new double[nbFolds], thresholds = new double[nbFolds];
		for (int i = 0; i < nbFolds; i++) {
			trainingKeys.clear();
			for (int j = 0; j < nbFolds; j++) {
				if (i != j)
					trainingKeys.addAll(folds.get(j));
				else
					testingKeys = folds.get(j);
			}
			thresholds[i] = train(trainingKeys);
			costs[i] = test(testingKeys,thresholds[i]);
		}
		
		double avgCost= 0;
		for (int i = 0; i < nbFolds; i++)
			avgCost+=costs[i];
		return avgCost/nbFolds;
	}

	private ArrayList<ArrayList<String>> getFolds() {
		int[] posFolds = new int[nbFolds + 1];
		posFolds[0] = 0;
		int q = keysForCrossValidation.size() / nbFolds, r = keysForCrossValidation.size() % nbFolds;
		for (int i = 1; i < nbFolds; i++)
			if (i < r)
				posFolds[i] = posFolds[i - 1] + q + 1;
			else
				posFolds[i] = posFolds[i - 1] + q;
		ArrayList<ArrayList<String>> folds = new ArrayList<ArrayList<String>>(nbFolds);
		for (int i = 0; i < nbFolds; i++) {
			folds.set(i, new ArrayList<String>(q + 1));
			folds.get(i).addAll(keysForCrossValidation.subList(posFolds[i], posFolds[i + 1]));
		}
		return folds;
	}

	private double train(ArrayList<String> trainingKeys) {
		double threshold = 0;
		ConfusionMatrix cm = new ConfusionMatrix();
		double cost, minCost = 1;
		double bestThreshold = 0;

		for (int i = 0; i < 1000; i++) {
			threshold = i / 1000.;
			cm.setThreshold(threshold);
			for (String key : trainingKeys) {
				cm.test(tdt_documents.get(key));
			}
			cost = cm.singleDetectionCost();
			if (cost < minCost) {
				minCost = cost;
				bestThreshold = threshold;
			}
		}
		return bestThreshold;
	}

	private double test(ArrayList<String> testingKeys, double threshold) {
		ConfusionMatrix cm = new ConfusionMatrix();

		cm.setThreshold(threshold);
		for (String key : testingKeys) {
			cm.test(tdt_documents.get(key));
		}

		return cm.singleDetectionCost();
	}
}
