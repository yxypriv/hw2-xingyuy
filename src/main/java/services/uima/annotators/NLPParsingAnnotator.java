package services.uima.annotators;
import java.util.List;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import services.types.DocumentContent;
import services.types.NounPhrase;
import services.utils.NLPUtil;
import services.utils.NLPUtil.Corpus;
/**
 * giving sentence to parsing noun phases
 * @author Xingyu
 *
 */
public class NLPParsingAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		FSIterator<Annotation> iterator = aJCas.getAnnotationIndex(DocumentContent.type).iterator();
		while(iterator.hasNext()) {
			DocumentContent docContent = (DocumentContent) iterator.next();
			List<Corpus> corpusSpan = NLPUtil.getCorpusSpan(docContent.getContent(), "NN");
			for(Corpus c : corpusSpan) {
				NounPhrase np = new NounPhrase(aJCas);
				np.setBegin(c.getStart());
				np.setEnd(c.getEnd());
				np.setContent(docContent.getContent().substring(c.getStart(), c.getEnd()));
				np.addToIndexes();
			}
		}
	}

}
