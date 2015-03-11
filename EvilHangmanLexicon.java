/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import acm.util.*;
import java.util.*;
import java.io.*;


public class EvilHangmanLexicon {

	private ArrayList<String> list;

	public EvilHangmanLexicon() {
    	try {
    		BufferedReader rd = new BufferedReader(new FileReader("EvilHangmanLexicon.txt"));
    		list = new ArrayList<String>();
    		while (true) {
    			String line = rd.readLine();
    			if (line != null) list.add(line);
    			else break;
    		}
    		rd.close();
    	} catch (IOException ex) {
    		throw new ErrorException(ex);
    	}
    }
	
/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return list.size();
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		if (index < list.size()) return list.get(index);
		else throw new ErrorException("getWord: Illegal index");
	}
}
