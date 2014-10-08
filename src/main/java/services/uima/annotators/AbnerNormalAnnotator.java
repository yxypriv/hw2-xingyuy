package services.uima.annotators;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import services.uima.types.abnor.TokenizedContent;
import services.utils.EnumerationUtils;
import services.utils.abner.AbnerUtil;
import abner.Tagger;

/**
 * Using gene dictionary generated from http://www.ncbi.nlm.nih.gov/gene Using
 * trie tree to save it and compare in the noun phases parts of the sentence.
 * 
 * @author Xingyu
 * 
 */
public class AbnerNormalAnnotator extends JCasAnnotator_ImplBase {
	Set<String> resultTypeFilter = null;
	private int processed;
	static Tagger tagger = null;

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
//		System.out.println("[ANA]");
		if (++processed % 500 == 0) {
			System.out.println(String.format("[Abnor][Normal][processing]%d", processed));
		}
		String oContent = aJCas.getDocumentText();
		String tContent = null;
		FSIterator<Annotation> iterator = aJCas.getAnnotationIndex(TokenizedContent.type).iterator();
		if (iterator.hasNext()) {
			TokenizedContent tc = (TokenizedContent) iterator.get();
			tContent = tc.getContent();
		}
		String[][] entities = tagger.getEntities(tContent);

		AbnerUtil.annotateTokenizedGene(aJCas, oContent, tContent, entities, EnumerationUtils.AnnotatorType.ABNER_NORMAL);
	}

	@Override
	public void destroy() {
		System.out.println(String.format("[Abnor][Normal][FINISHED]"));
		super.destroy();
	}

}
