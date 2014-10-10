package services.uima.annotators;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import edu.stanford.nlp.trees.Tree;
import services.uima.types.GeneName;
import services.uima.types.NounPhrase;
import services.utils.AnnotatingUtil;
import services.utils.EnumerationUtils;
import services.utils.trieTree.TrieTreeHandle;
import services.utils.trieTree.TrieTreeNode;

/**
 * Using gene dictionary generated from http://www.ncbi.nlm.nih.gov/gene Using
 * trie tree to save it and compare in the noun phases parts of the sentence.
 * 
 * @author Xingyu
 * 
 */
public class GeneParsingAnnotator extends JCasAnnotator_ImplBase {
	static final String RES_GENE_DICT = "Dictionary";

	Set<Character> validStopCharSet;
	TrieTreeHandle geneNameHandle;

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		String dictionaryPath = (String) aContext.getConfigParameterValue(RES_GENE_DICT);
		// URL resource =
		// GeneParsingAnnotator.class.getResource(dictionaryPath);
		geneNameHandle = TrieTreeHandle.getInstance(GeneParsingAnnotator.class.getClassLoader().getResourceAsStream(dictionaryPath), dictionaryPath);
		// geneNameHandle.buildTrieTree(new File(resource.getFile()));

		char[] validCharArray = " /()[]{}#".toCharArray();
		validStopCharSet = new HashSet<Character>();
		for (char c : validCharArray) {
			validStopCharSet.add(c);
		}

		super.initialize(aContext);
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// System.out.println("[GP]");
		TrieTreeNode root = geneNameHandle.getRoot();
		String content = aJCas.getDocumentText();

		char[] contentArray = content.toCharArray();

		FSIterator<Annotation> nIterator = aJCas.getAnnotationIndex(NounPhrase.type).iterator();
		boolean[] enablePosition = new boolean[contentArray.length];
		while (nIterator.hasNext()) {
			Annotation next = nIterator.next();
			for (int i = next.getBegin(); i < next.getEnd(); i++) {
				enablePosition[i] = true;
			}
		}

		boolean startFlag = true;
		for (int i = 0; i < contentArray.length; i++) {
			if (startFlag) {
				int j = i;
				TrieTreeNode currentNode = root;
				while (j < contentArray.length) {
					if (!enablePosition[j])
						break;
					if (currentNode.acceptableNumberRange[0] != -1) {
						int k = j + 1;
						int numberSuffixLength = 0;
						while (k < contentArray.length && //
								contentArray[k] >= '0' && contentArray[k] <= '9') {
							numberSuffixLength++;
							k++;
						}
						if (k == contentArray.length || validStopCharSet.contains(contentArray[k])) {
							if (currentNode.isNumberLengthInRange(numberSuffixLength)) {
								annotateGene(aJCas, i, j + numberSuffixLength + 1, content);
								break;
							}
						}
					}
					if (j == contentArray.length - 1 || validStopCharSet.contains(contentArray[j])) {
						if (currentNode.stopable) {
							// i to j is a gene name
							annotateGene(aJCas, i, j, content);
							break;
						}
					}

					TrieTreeNode nextNode = currentNode.children[TrieTreeHandle.getCharIndex(contentArray[j])];
					if (null == nextNode)
						break;
					currentNode = nextNode;
					j++;
				}
				startFlag = false;
			} else {
				if (validStopCharSet.contains(contentArray[i])) {
					startFlag = true;
				}
			}
		}
	}

	private void annotateGene(JCas aJcas, int start, int end, String content) {
		AnnotatingUtil.annotateGene(aJcas, start, end, content, EnumerationUtils.AnnotatorType.MANUAL_ANNOTATOR);
	}
}
