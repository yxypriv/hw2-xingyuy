package services.uima.annotators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import services.types.DocumentContent;
import services.types.DocumentKey;

/**
 * Using regular to parse between key and sentence.
 * @author Xingyu
 */
public class RawParsingAnnotator extends JCasAnnotator_ImplBase {

	String patternStr = "^(P[^\\s]+)\\s+(.*)$";

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		String documentText = aJCas.getDocumentText();
		Pattern p = Pattern.compile(patternStr);
		Matcher matcher = p.matcher(documentText);
		if (matcher.find()) {
			DocumentKey docKey = new DocumentKey(aJCas);
			docKey.setBegin(matcher.start(1));
			docKey.setEnd(matcher.end(1));
			docKey.setKey(matcher.group(1));
			docKey.addToIndexes();
			DocumentContent docContent = new DocumentContent(aJCas);
			docContent.setBegin(matcher.start(2));
			docContent.setEnd(matcher.end(2));
			docContent.setContent(matcher.group(2));
			docContent.addToIndexes();
		}
	}

}
