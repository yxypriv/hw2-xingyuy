package services.uima.annotators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import services.uima.types.GeneName;
import services.utils.EnumerationUtils;

/**
 * Combine all result and vote for final output
 * 
 * @author Xingyu
 * 
 */
public class ResultCombineAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
//		System.out.println("[RCA]");
		FSIterator<Annotation> it2 = aJCas.getAnnotationIndex(GeneName.type).iterator();

		List<GeneName> otherGeneList = null;
		boolean[] occupy = null;
		String content = aJCas.getDocumentText();
		occupy = new boolean[content.length()];
		otherGeneList = new ArrayList<GeneName>();

		List<GeneName> lingPipeList = new ArrayList<GeneName>();
		while (it2.hasNext()) {
			GeneName gene = (GeneName) it2.next();
			if (gene.getAnnotatorId() == EnumerationUtils.AnnotatorType.LINGPIPE_ANNOTATOR) {
				int start = gene.getBegin();
				int end = gene.getEnd();
				for (int i = start; i < end; i++) {
					occupy[i] = true;
				}
				lingPipeList.add(gene);
			} else {
				otherGeneList.add(gene);
			}
		}
		for (GeneName gene : lingPipeList) {
			annotateGene(aJCas, gene.getBegin(), gene.getEnd(), gene.getName());
		}

		for (GeneName og : otherGeneList) {
			int start = og.getBegin();
			int end = og.getEnd();
			boolean overlap = false;
			for (int i = start; i < end; i++) {
				if (occupy[i]) {
					overlap = true;
					break;
				}
			}
			if (!overlap) {
				for (int i = start; i < end; i++) {
					occupy[i] = true;
				}
				annotateGene(aJCas, start, end, og.getName());
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

}
