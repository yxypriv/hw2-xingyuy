package services.utils.trieTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A handle for the trie tree. Contained root as property and can be used directly.
 * @author Xingyu
 *
 */
public class TrieTreeHandle {
	/* note '~' is not in the list */
	final static String acceptableCharactors = " #%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}";
	static Map<Character, Integer> charIndexMap;
	final static int nodeSize = acceptableCharactors.length();

	final static char dividChar = '~';
	
	static {
		char[] acceptableCharArray = acceptableCharactors.toCharArray();
		charIndexMap = new HashMap<Character, Integer>();
		for (int i = 0; i < acceptableCharArray.length; i++) {
			charIndexMap.put(acceptableCharArray[i], i);
		}
	}
	public static int getCharIndex(Character c) {
		return charIndexMap.get(c);
	}
	TrieTreeNode root;

	public TrieTreeHandle() {
		root = new TrieTreeNode(acceptableCharactors.length());
	}

	public TrieTreeNode getRoot() {
		return root;
	}
	/**
	 * using first order search to generate strings for each leaf node. 
	 * 		Then print them into file.
	 * @param File f outpt file path.
	 */
	public void writeToFile(File f) {
		long t0 = System.currentTimeMillis();
		System.out.println("Search and building tree");
		List<String> zipedFileList = new ArrayList<String>();
		frontOrderSearch(root, "", zipedFileList);
		long t1 = System.currentTimeMillis();
		System.out.println("Searching finished in " + (t1 - t0) + " ms");
		System.out.println("Writing file");
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (String zipedLine : zipedFileList) {
			writer.println(zipedLine);
		}
		writer.close();
		long t2 = System.currentTimeMillis();
		System.out.println("Writing finished in " + (t2 - t1) + " ms");
	}

	/**
	 * private function, using recursive.
	 * @param n tree node
	 * @param base base String
	 * @param zipedFileList in fact that's the return value.
	 */
	private void frontOrderSearch(TrieTreeNode n, String base, List<String> zipedFileList) {
		if (n.stopable) {
			zipedFileList.add(base);
		}
		if (n.acceptableNumberRange[0] != -1) {
			zipedFileList.add(base + dividChar + n.acceptableNumberRange[0]//
					+ dividChar + n.acceptableNumberRange[1]);
		}
		for (int i = 0; i < n.children.length; i++) {
			if (n.children[i] == null)
				continue;
			frontOrderSearch(n.children[i], base + acceptableCharactors.charAt(i), zipedFileList);
		}
	}
	/**
	 * generate trie tree using raw data.
	 * zip them using number suffix. 
	 * @param f raw data file
	 */
	public void buildTrieTree_using_raw_file(File f) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String line = null;
		long t0 = System.currentTimeMillis();
		int lineCount = 0;
		Pattern pattern = Pattern.compile("(\\d+)$");
		try {
			while ((line = reader.readLine()) != null) {
				if (++lineCount % 100000 == 0)
					System.out.println(String.format("[TrieTreeHandle] loading... \t%d lines, \t%d ms",//
							lineCount, System.currentTimeMillis() - t0));
				line = line.trim();
				Matcher m = pattern.matcher(line);
				int numberSuffixLength = 0;
				if (m.find()) {
					line = line.substring(0, m.start());
					numberSuffixLength = m.group(1).length();
				}

				addStrng(line, numberSuffixLength);
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
		System.out.println(String.format("[TrieTreeHandle] building finished in %d ms", System.currentTimeMillis() - t0));
	}

	/**
	 * generate trie tree using ziped data
	 * @param f
	 */
	public void buildTrieTree(File f) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String line = null;
		long t0 = System.currentTimeMillis();
		int lineCount = 0;
		try {
			while ((line = reader.readLine()) != null) {
				if (++lineCount % 100000 == 0)
					System.out.println(String.format("[TrieTreeHandle] loading... \t%d lines, \t%d ms",//
							lineCount, System.currentTimeMillis() - t0));
				
				String[] split = line.split(dividChar+"");
				if(split.length == 1)
					addStrng(line, 0);
				else {
					int lowerBound = Integer.parseInt(split[1]);
					int upperBound = Integer.parseInt(split[2]);
					addStrng(split[0], lowerBound);
					if(lowerBound != upperBound) {
						addStrng(split[0], upperBound);
					} 
				}
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
		System.out.println(String.format("[TrieTreeHandle] building finished in %d ms", System.currentTimeMillis() - t0));
	}
	
	/**
	 * Add a new String to trie tree.
	 * @param prefix
	 * @param numberSuffix
	 */
	public void addStrng(String prefix, int numberSuffix) {
		char[] charArray = prefix.toCharArray();
		addString(root, charArray, 0, numberSuffix);
	}

	private void addString(TrieTreeNode n, char[] string, int indexInString, int numberSuffix) {
		if (indexInString >= string.length) {
			if (numberSuffix == 0)
				n.stopable = true;
			else
				n.addNumberLength(numberSuffix);
			return;
		}
		int nodeIndex = charIndexMap.get(string[indexInString]);
		if (n.children[nodeIndex] == null) {
			n.children[nodeIndex] = new TrieTreeNode(nodeSize);
		}
		addString(n.children[nodeIndex], string, indexInString + 1, numberSuffix);
	}
}
