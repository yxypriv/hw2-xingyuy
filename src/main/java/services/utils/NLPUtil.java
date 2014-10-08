package services.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class NLPUtil {

	private static StanfordCoreNLP pipeline;

	static {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos");
		pipeline = new StanfordCoreNLP(props);
	}

	public static List<Corpus> getSingleCorpusSpan(String text, String corpusToken) {
		List<Corpus> corpusList = new ArrayList<NLPUtil.Corpus>();
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String pos = token.get(PartOfSpeechAnnotation.class);
				if (pos.startsWith(corpusToken)) {
					int begin = token.beginPosition();
					int end = token.endPosition();
					Corpus c = new Corpus();
					c.setStart(begin);
					c.setEnd(end);
					corpusList.add(c);
				}
			}
		}
		return corpusList;
	}

	public static List<Corpus> getCorpusSpan(String text, String corpusToken) {
		List<Corpus> corpusList = new ArrayList<NLPUtil.Corpus>();
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			List<CoreLabel> candidate = new ArrayList<CoreLabel>();
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String pos = token.get(PartOfSpeechAnnotation.class);
				if (pos.startsWith(corpusToken)) {
					candidate.add(token);
				} else if (candidate.size() > 0) {
					int begin = candidate.get(0).beginPosition();
					int end = candidate.get(candidate.size() - 1).endPosition();
					Corpus c = new Corpus();
					c.setStart(begin);
					c.setEnd(end);
					corpusList.add(c);
					candidate.clear();
				}
			}
			if (candidate.size() > 0) {
				int begin = candidate.get(0).beginPosition();
				int end = candidate.get(candidate.size() - 1).endPosition();
				Corpus c = new Corpus();
				c.setStart(begin);
				c.setEnd(end);
				corpusList.add(c);
				candidate.clear();
			}
		}
		return corpusList;
	}

	public static class Corpus {
		int start;
		int end;

		public int getStart() {
			return start;
		}

		public void setStart(int start) {
			this.start = start;
		}

		public int getEnd() {
			return end;
		}

		public void setEnd(int end) {
			this.end = end;
		}

	}
}
