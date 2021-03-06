package services.uima.annotators;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import services.uima.types.GeneName;
import services.utils.AnnotatingUtil;
import services.utils.EnumerationUtils;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.util.AbstractExternalizable;

/**
 * Lingpipe Annotator.
 * 
 * @author Xingyu
 * 
 */
public class LingpipeAnnotator extends JCasAnnotator_ImplBase {
	public final String MODEL_PATH = "model_path";
	int processed = 0;
	Chunker chunker = null;

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		Object configParameterValue = aContext.getConfigParameterValue(MODEL_PATH);
		if (null == configParameterValue) {
			System.err.println("Lingpipe configuaration error: no model path detected.");
		}

		// System.out.println(configParameterValue);
		// URL resource =
		// LingpipeAnnotator.class.getClassLoader().getResource((String)
		// configParameterValue);
		// File modelFile = new File(resource.getFile());

		try {
			chunker = (Chunker) AbstractExternalizable.readResourceObject((String) configParameterValue);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// System.out.println("[LA]");
		if (++processed % 500 == 0) {
			System.out.println(String.format("[Lingpipe][processing]%d", processed));
		}
		String content = aJCas.getDocumentText();
		Chunking chunking = chunker.chunk(content);
		for (Chunk c : chunking.chunkSet()) {
			int start = c.start();
			int end = c.end();
			AnnotatingUtil.annotateGene(aJCas, start, end, content, EnumerationUtils.AnnotatorType.LINGPIPE_ANNOTATOR);
		}
	}

	@Override
	public void destroy() {
		System.out.println(String.format("[LINGPIPE][FINISHED]"));
		super.destroy();
	}
}
