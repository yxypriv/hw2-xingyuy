package services.uima.consumers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;

import services.uima.types.DocumentKey;
import services.uima.types.GeneName;
import services.utils.EnumerationUtils;
import services.utils.FileUtil;

/**
 * Run benchmark for the process. Required providing answer file for the same
 * test case. If failed to provide <b>AnswerFile</b>, this consumer will not
 * report as error but will not act neighter.
 * 
 * @author Xingyu
 *
 */
public class BenchmarkConsumer extends CasConsumer_ImplBase {
	public static final String PARAM_ANSWER_FILE = "AnswerFile";
	Map<String, Set<String>> answer = null;

	int hit = 0;
	int falseHit = 0;
	int missHit = 0;

	@Override
	public void initialize() throws ResourceInitializationException {
		answer = new HashMap<String, Set<String>>();
		Object configParameterValue = getConfigParameterValue(PARAM_ANSWER_FILE);
		if (null == configParameterValue) {
			System.err.println("[No Answer File found, Benchmark not operational]");
			return;
		}

		String answerFilePath = ((String) configParameterValue).trim();

		FileUtil.iterateFileByLine(answerFilePath, new FileUtil.FileLineProcess() {
			@Override
			public void process(String line) {
				String[] split = line.split("\\|");
				if (split.length != 3) {
					System.err.println(line);
					return;
				}
				if (!answer.containsKey(split[0]))
					answer.put(split[0], new HashSet<String>());
				answer.get(split[0]).add(split[2]);
			}
		});

		super.initialize();
	}

	public void processCas(CAS aCAS) throws ResourceProcessException {
		if (null == answer)
			return;

		JCas jcas;
		try {
			jcas = aCAS.getJCas();
		} catch (CASException e) {
			throw new ResourceProcessException(e);
		}

		String docKey = null;
		FSIterator<Annotation> it2 = jcas.getAnnotationIndex(DocumentKey.type).iterator();
		if (it2.hasNext()) {
			DocumentKey key = (DocumentKey) it2.next();
			docKey = key.getKey();
		}
		Set<String> answerSet = answer.get(docKey);

		FSIterator<Annotation> it = jcas.getAnnotationIndex(GeneName.type).iterator();

		if (null == answerSet) {
			while (it.hasNext()) {
				GeneName np = (GeneName) it.next();
				if (np.getAnnotatorId() != EnumerationUtils.AnnotatorType.LINGPIPE_ANNOTATOR)
					continue; // filter for final result only
				
				falseHit++;
			}
		} else {
			int currentHit = 0;
			while (it.hasNext()) {
				GeneName np = (GeneName) it.next();
				if (np.getAnnotatorId() != EnumerationUtils.AnnotatorType.LINGPIPE_ANNOTATOR)
					continue; // filter for final result only

				if (answerSet.contains(np.getName())) {
					currentHit++;
				} else {
					falseHit++;
				}
			}
			hit += currentHit;
			missHit += (answerSet.size() - currentHit);
		}
	}

	@Override
	public void destroy() {
		if (null == answer)
			return;

		double precision = (double) hit / (hit + falseHit);
		double recall = (double) hit / (hit + missHit);
		double f_score = 2 * precision * recall / (precision + recall);
		System.out.println(hit + "\t" + falseHit + "\t" + missHit);
		System.out.println(precision + "\t" + recall + "\t" + f_score);

		super.destroy();
	}
}
