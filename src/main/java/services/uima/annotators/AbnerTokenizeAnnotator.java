package services.uima.annotators;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import services.uima.types.abnor.TokenizedContent;
import abner.Tagger;

/**
 * A pre-run class, functioning tokenize original data and feed other Abner
 * Annotator so that in this program tokenize only need to be run once.
 * 
 * @author Xingyu
 * 
 */
public class AbnerTokenizeAnnotator extends JCasAnnotator_ImplBase {
	static Tagger tagger = null;
	private int processed;

	// int
	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		if (null == tagger)
			tagger = new Tagger();
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// System.out.println("[ATA]");
		if (++processed % 500 == 0) {
			System.out.println(String.format("[Abnor][TOKENIZE][processing]%d", processed));
		}

		String content = aJCas.getDocumentText();
		TokenizedContent tc = new TokenizedContent(aJCas);
		tc.setBegin(-1);
		tc.setEnd(-1);
		tc.setContent(tagger.tokenize(content));
		tc.addToIndexes();
	}

	@Override
	public void destroy() {
		System.out.println(String.format("[Abnor][Creative][FINISHED]"));
		super.destroy();
	}

}
