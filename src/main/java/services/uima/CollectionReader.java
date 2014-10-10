package services.uima;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;

import services.uima.types.DocumentKey;

/**
 * read from <b>InputFile</b>, save in document text Homework2 update: do the
 * first step split and also save key Annotation.
 * 
 * @author Xingyu
 * 
 */
public class CollectionReader extends CollectionReader_ImplBase {

	public static final String PARAM_INPUT_FILE = "InputFile";
	BufferedReader reader = null;
	String line = null;

	@Override
	public void initialize() throws ResourceInitializationException {
		String inputFilePath = ((String) getConfigParameterValue(PARAM_INPUT_FILE)).trim();
//		System.out.println(new File(inputFilePath).getAbsolutePath());
		// URL resource =
		// CollectionReader.class.getClassLoader().getResource(inputFilePath);
		// File f = new File(resource.getFile());
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		super.initialize();
	}

	public void getNext(CAS aCAS) throws IOException, CollectionException {
		JCas jCas = null;
		try {
			jCas = aCAS.getJCas();
		} catch (CASException e) {
			e.printStackTrace();
		}

		String[] split = line.split(" +", 2);
		aCAS.setDocumentText(split[1]);
		// System.out.println("[reader]" + split[1]);
		DocumentKey key = null;
		key = new DocumentKey(jCas);
		key.setKey(split[0]);
		key.addToIndexes(jCas);
	}

	public boolean hasNext() throws IOException, CollectionException {
		if (null == reader)
			return false;
		line = reader.readLine();
		return line != null;
	}

	public Progress[] getProgress() {
		return null;
	}

	public void close() throws IOException {
		if (null != reader)
			reader.close();
	}

}
