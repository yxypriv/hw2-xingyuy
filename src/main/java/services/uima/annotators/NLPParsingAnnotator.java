package services.uima.annotators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import services.uima.types.NounPhrase;
import services.utils.NLPUtil;
import services.utils.NLPUtil.Corpus;

/**
 * giving sentence to parsing noun phases
 * 
 * @author Xingyu
 *
 */
public class NLPParsingAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
//		System.out.println("[NLP]");
		String docContent = aJCas.getDocumentText();
		List<Corpus> corpusSpan = NLPUtil.getCorpusSpan(docContent, "NN");
		for (Corpus c : corpusSpan) {
			NounPhrase np = new NounPhrase(aJCas);
			np.setBegin(c.getStart());
			np.setEnd(c.getEnd());
			np.setContent(docContent.substring(c.getStart(), c.getEnd()));
			np.addToIndexes();
		}
	}
	

}
