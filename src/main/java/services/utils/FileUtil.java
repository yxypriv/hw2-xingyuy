package services.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * A tool class for more easy reading files and process.
 * 
 * @author Xingyu Yan
 *
 */
public class FileUtil {
	public static interface FileLineProcess {
		public void process(String line);
	}

	public static void iterateFileByLine(String resourcePath, FileLineProcess process) {
		InputStream resource = FileUtil.class.getClassLoader().getResourceAsStream(resourcePath);

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(resource, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				process.process(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
