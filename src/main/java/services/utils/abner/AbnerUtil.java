package services.utils.abner;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.jcas.JCas;

import services.uima.types.GeneName;
import services.utils.AnnotatingUtil;

/**
 * Tokenized annotator will come back with tokenized gene name and don't have
 * the start and end point, this is a util class to take raw abner result and do
 * the annotations for abner.
 * 
 * @author Xingyu Yan
 */

public class AbnerUtil {
	public static void annotateTokenizedGene(JCas aJCas, String originalContent, String tokenizedContent, //
			String[][] entities, int annotatorId) {
		int compareStart = 0;
		if (entities.length > 0) {
			List<AbnerGeneTag> currentEntities = new ArrayList<AbnerGeneTag>();
			for (int i = 0; i < entities[0].length; i++) {
				AbnerGeneTag gs = new AbnerGeneTag();
//				gs.gene = entities[0][i];

				int start = tokenizedContent.indexOf(entities[0][i], compareStart);
				int end = start + entities[0][i].length();
				compareStart = end;

				gs.start = start;
				gs.end = end;

				currentEntities.add(gs);
			}
			char[] tokenArray = tokenizedContent.toCharArray();
			char[] originArray = originalContent.toCharArray();

			int tokenIndex = 0;
			int originIndex = 0;
			while (tokenIndex < tokenArray.length && originIndex < originArray.length) {
				if (tokenArray[tokenIndex] == originArray[originIndex]) {
					tokenIndex++;
					originIndex++;
					continue;
				}
				for (AbnerGeneTag ce : currentEntities) {
					if (ce.start > tokenIndex)
						ce.start_reduce++;
					if (ce.end > tokenIndex)
						ce.end_reduce++;
				}
				tokenIndex++;
			}

			for (AbnerGeneTag ce : currentEntities) {
				ce.start -= ce.start_reduce;
				ce.end -= ce.end_reduce;
//				ce.gene = originalContent.substring(ce.start, ce.end);
				AnnotatingUtil.annotateGene(aJCas, ce.start, ce.end, originalContent, annotatorId);
			}

		}

	}

	private static class AbnerGeneTag {
		int start;
		int end;
		int start_reduce;
		int end_reduce;
//		String gene;
	}
}
