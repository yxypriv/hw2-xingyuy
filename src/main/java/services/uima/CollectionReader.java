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
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
/**
 * read from <b>InputFile</b>, save in document text
 * @author Xingyu
 *
 */
public class CollectionReader extends CollectionReader_ImplBase {

	public static final String PARAM_INPUT_FILE = "InputFile";
	BufferedReader reader = null;
	String line = null;
	@Override
	public void initialize() throws ResourceInitializationException {
		URL resource = CollectionReader.class.getResource(((String) getConfigParameterValue(PARAM_INPUT_FILE)).trim());
		File f = new File(resource.getFile());
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		super.initialize();
	}
	public void getNext(CAS aCAS) throws IOException, CollectionException {
		aCAS.setDocumentText(line);
	}

	public boolean hasNext() throws IOException, CollectionException {
		line = reader.readLine();
		return line != null;
	}

	public Progress[] getProgress() {
		// TODO Auto-generated method stub
//		System.out.println("1.3");
		return null;
	}

	public void close() throws IOException {
		reader.close();
	}

}
