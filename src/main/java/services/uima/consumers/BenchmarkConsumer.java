package services.uima.consumers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
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

import services.types.DocumentKey;
import services.types.GeneName;
import services.uima.CollectionReader;

/**
 * Run benchmark for the process.
 * Required providing answer file for the same test case.
 * If failed to provide <b>AnswerFile</b>, this consumer will 
 * 		not report as error but will not act neighter.  
 * 
 * @author Xingyu
 *
 */
public class BenchmarkConsumer extends CasConsumer_ImplBase {
	public static final String PARAM_ANSWER_FILE = "AnswerFile";
	Map<String, Set<String>> answer = null;

	int correctHit, wrongHit;

	@Override
	public void initialize() throws ResourceInitializationException {
		Object configParameterValue = getConfigParameterValue(PARAM_ANSWER_FILE);
		if(null == configParameterValue)
			return;
		
		String answerFilePath = ((String) configParameterValue).trim();
		
		URL resource = CollectionReader.class.getResource(answerFilePath);
		File f = new File(resource.getFile());
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				String[] split = line.split("\\|");
				if (!answer.containsKey(split[0])) {
					answer.put(split[0], new HashSet<String>());
				}
				answer.get(split[0]).add(split[2]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		correctHit = 0;
		wrongHit = 0;
//		miss = 0;

		super.initialize();
	}

	public void processCas(CAS aCAS) throws ResourceProcessException {
		if(null == answer)
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

		FSIterator<Annotation> it = jcas.getAnnotationIndex(GeneName.type).iterator();
		// System.out.println("[consumer]" + jcas.getDocumentText());
		while (it.hasNext()) {
			GeneName np = (GeneName) it.next();
			// System.out.println(String.format("%s|%d %d|%s", docKey,
			// np.getBegin(), np.getEnd(), np.getName()));
			// writer.println(String.format("%s|%d %d|%s", docKey,
			// np.getBegin(), np.getEnd(), np.getName()));
			Set<String> answerSet = answer.get(docKey);
			if (answerSet.contains(np.getName())) {
				correctHit++;
			} else
				wrongHit++;
		}
	}

	@Override
	public void destroy() {
		if(null == answer)
			return;
		System.out.println(String.format("%d hits, %d hits wrong, precesion %f %", correctHit, wrongHit, ((double) correctHit / (correctHit + wrongHit)) * 100));
		super.destroy();
	}
}
