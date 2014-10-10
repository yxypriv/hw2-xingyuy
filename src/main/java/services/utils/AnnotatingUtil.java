package services.utils;

import org.apache.uima.jcas.JCas;

import services.uima.types.GeneName;

public class AnnotatingUtil {
	public static void annotateGene(JCas aJCas, int start, int end, String content, Integer annotatorId) {
		GeneName gene = new GeneName(aJCas);
		SpaceResult obj = _countSpace(content, start, end);

		gene.setBegin(start - obj.start_space);
		gene.setEnd(end - obj.end_space - 1);
		gene.setName(content.substring(start, end));
		gene.setAnnotatorId(annotatorId);
		gene.addToIndexes();
	}

	private static SpaceResult _countSpace(String str, int start, int end) {
		SpaceResult result = new SpaceResult();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ' ') {
				if (i < start)
					result.start_space++;
				if (i < end)
					result.end_space++;
			}
		}
		return result;
	}

	private static class SpaceResult {
		int start_space;
		int end_space;
	}
}
