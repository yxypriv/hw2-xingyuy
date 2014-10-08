package services.uima.annotators;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import services.utils.EnumerationUtils;
import services.utils.abner.AbnerUtil;
import abner.Tagger;

/**
 * An abner annotator, used original data and used NLPBA library
 *  
 * @author Xingyu
 * 
 */
public class AbnerNoneTokenizeAnnotator extends JCasAnnotator_ImplBase {
	int processed = 0;
	static Tagger tagger = null;
	Set<String> resultTypeFilter = null;

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		resultTypeFilter = new HashSet<String>();
		resultTypeFilter.add("PROTEIN");
		resultTypeFilter.add("DNA");
		resultTypeFilter.add("RNA");

		if (null == tagger) {
			tagger = new Tagger();
			tagger.setTokenization(false);
		}
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
//		System.out.println("[ANT]");
		if (++processed % 500 == 0) {
			System.out.println(String.format("[Abnor][NoneTokenize][processing]%d", processed));
		}
		String content = aJCas.getDocumentText();
		int compareStart = 0;
		String[][] entities = tagger.getEntities(content);
		if (entities.length > 0) {
			for (int i = 0; i < entities[0].length; i++) {
				// System.out.println(entities[0][i] + "\t[" + entities[1][i] +
				// "]");
				if (resultTypeFilter.contains(entities[1][i])) {
					int start = content.indexOf(entities[0][i], compareStart);
					int end = start + entities[0][i].length();
					compareStart = end;

					if (start == -1) {
						System.out.println(String.format("%d,%d\t%s", start, end, entities[0][i]));
						System.out.println(content);
					}
					AbnerUtil.annotateGene(aJCas, start, end, entities[0][i], EnumerationUtils.AnnotatorType.ABNER_NONE_TOKENIZE);
				}
			}
		}
	}

	@Override
	public void destroy() {
		System.out.println(String.format("[Abnor][None Tokenize][FINISHED]"));
		super.destroy();
	}
}
