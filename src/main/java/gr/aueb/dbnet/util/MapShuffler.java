package gr.aueb.dbnet.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapShuffler {
	
	public static <T> void shuffle(Map trainSet) {
		List vs = new ArrayList(trainSet.values());
		Collections.shuffle(vs);
		Iterator vIter = vs.iterator();
		for (Object k : trainSet.keySet()) trainSet.put(k, vIter.next());
	}

}
