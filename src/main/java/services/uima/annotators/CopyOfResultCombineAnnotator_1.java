package services.uima.annotators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import services.uima.types.GeneName;
import services.utils.EnumerationUtils;

/**
 * Combine all result and vote for final output
 * 
 * @author Xingyu
 * 
 */
public class CopyOfResultCombineAnnotator_1 extends JCasAnnotator_ImplBase {
	public static final int VOTEVALID = 2;

//	@Override
//	public void process(JCas aJCas) throws AnalysisEngineProcessException {
//		FSIterator<Annotation> it2 = aJCas.getAnnotationIndex(GeneName.type).iterator();
//		while (it2.hasNext()) {
//			GeneName gene = (GeneName) it2.next();
//			int start = gene.getBegin();
//			int end = gene.getEnd();
//			String name = gene.getName();
//			if (gene.getAnnotatorId() == EnumerationUtils.AnnotatorType.LINGPIPE_ANNOTATOR)
//				annotateGene(aJCas, start, end, name);
//		}
//	}

	// @Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		FSIterator<Annotation> it2 = aJCas.getAnnotationIndex(GeneName.type).iterator();
		Map<Integer, List<Interval>> startMap = new HashMap<Integer, List<Interval>>();
		Map<Integer, List<Interval>> endMap = new HashMap<Integer, List<Interval>>();

		while (it2.hasNext()) {
			GeneName gene = (GeneName) it2.next();
			int start = gene.getBegin();
			int end = gene.getEnd();
			String name = gene.getName();

			if (!startMap.containsKey(start))
				startMap.put(start, new ArrayList<Interval>());
			boolean found = false;
			List<Interval> startIntervalList = startMap.get(start);
			for (Interval interval : startIntervalList) {
				if (interval.end == end) {
					if (gene.getAnnotatorId() == EnumerationUtils.AnnotatorType.LINGPIPE_ANNOTATOR)
						interval.weight = 2;
					interval.count++;
					found = true;
					break;
				}
			}
			if (!found) {
				// only search in start because if exists, it's the same as in
				// the end, if not then create a new one and add to both index.
				Interval thisInterval = new Interval();
				thisInterval.start = start;
				thisInterval.end = end;
				thisInterval.count = 1;
				thisInterval.name = name;
				if (gene.getAnnotatorId() == EnumerationUtils.AnnotatorType.LINGPIPE_ANNOTATOR)
					thisInterval.weight = 2;
				startIntervalList.add(thisInterval);

				if (!endMap.containsKey(end))
					endMap.put(end, new ArrayList<Interval>());
				List<Interval> endIntervalList = endMap.get(end);
				endIntervalList.add(thisInterval);
			}
		}

		int ignoreTo = 0;
		List<Integer> startKeyList = new ArrayList<Integer>();
		// System.out.println("[check gene]");

		startKeyList.addAll(startMap.keySet());
		Collections.sort(startKeyList);
		for (int start : startKeyList) {
			if (ignoreTo < start)
				continue;

			List<Interval> startList = startMap.get(start);

			boolean agreed = false;
			for (Interval it : startList) {
				if (it.count * it.weight >= VOTEVALID) {
					annotateGene(aJCas, it.start, it.end, it.name);
					ignoreTo = it.end;
					agreed = true;
					break;
				}
			}
			if (!agreed) {
				Interval miniInterval = null;
				int startWeightSum = 0;
				for (Interval it : startList) {
					startWeightSum += it.weight;
				}
				if (startWeightSum >= VOTEVALID) {
					for (Interval it : startList) {
						if (null == miniInterval || miniInterval.end > it.end) {
							ignoreTo = it.end;
							miniInterval = it;
						}
					}
				}
				if (startList.size() > 0) {
					for (Interval currentInterval : startList) {
						List<Interval> endList = endMap.get(currentInterval.end);
						int endWeightSum = 0;
						for (Interval it : startList) {
							endWeightSum += it.weight;
						}
						if (endWeightSum >= VOTEVALID) {
							for (Interval it : endList) {
								if (null == miniInterval || //
										miniInterval.end - miniInterval.start > it.end - it.start) {
									miniInterval = it;
									ignoreTo = it.end;
								}
							}
						}
					}
				}
				if (null != miniInterval) {
					// System.out.println("[added gene]");
					annotateGene(aJCas, miniInterval.start, miniInterval.end, miniInterval.name);
				}
			}
		}
	}

	private void annotateGene(JCas aJcas, int start, int end, String content) {
		GeneName gene = new GeneName(aJcas);
		gene.setBegin(start);
		gene.setEnd(end);
		gene.setName(content);
		gene.setAnnotatorId(EnumerationUtils.AnnotatorType.FINAL);
		gene.addToIndexes();
	}

	private static class Interval {
		int start;
		int end;
		int count;
		String name;
		int weight = 1;
	}
}
