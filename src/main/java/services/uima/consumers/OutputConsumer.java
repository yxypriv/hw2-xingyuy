package services.uima.consumers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;

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

/**
 * Generate output file according to parameter <b>OutputFile</b>
 * 
 * @author Xingyu
 *
 */

public class OutputConsumer extends CasConsumer_ImplBase {
	public static final String PARAM_OUTPUT_FILE = "OutputFile";
	PrintWriter writer = null;

	@Override
	public void initialize() throws ResourceInitializationException {
		String outputFile = ((String) getConfigParameterValue(PARAM_OUTPUT_FILE)).trim();
		URL resource = OutputConsumer.class.getResource(outputFile);
		if (null != resource) {
			File f = new File(resource.getFile());
			try {
				writer = new PrintWriter(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				writer = new PrintWriter(outputFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		super.initialize();
	}

	public void processCas(CAS aCAS) throws ResourceProcessException {
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
		while (it.hasNext()) {
			GeneName np = (GeneName) it.next();
			if (np.getAnnotatorId() != EnumerationUtils.AnnotatorType.FINAL)
				continue;
			writer.println(String.format("%s|%d %d|%s", docKey, np.getBegin(), np.getEnd(), np.getName()));
		}
	}

	@Override
	public void destroy() {
		writer.close();
		super.destroy();
	}
}
