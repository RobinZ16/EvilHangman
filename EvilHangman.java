/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import java.applet.AudioClip;

import acm.program.*;
import acm.util.*;
import java.util.*;

public class EvilHangman extends ConsoleProgram {
	
	/* Loads the epic audio clip */
	AudioClip epicClip = MediaTools.loadAudioClip("epic.au");
	
	/* Loads the nice audio clip */
	AudioClip niceClip = MediaTools.loadAudioClip("nice.au");
	
	/* Loads the no audio clip */
	AudioClip noClip = MediaTools.loadAudioClip("no.au");
	
	/* Number of lives that the user starts with */
	private static final int STARTING_LIVES = 8;
	
	/* Produces a RandomGenerator instance */
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	/* Produces a HangmanLexicon instant */
	private EvilHangmanLexicon hl = new EvilHangmanLexicon();
	
	/* Produces a HangmanCanvas instant */
	private EvilHangmanCanvas canvas;
	
	/* Keeps track of all guesses */
	private ArrayList<Character> guessList;
	
	/* Keeps track of already guessed letters as pairs of letters & positions */
	private HashMap<Integer, Character> statusMap;
	
	/* Keeps track of current word */
	private String word;
	
	/* Keeps track of Cheat Mode */
	private String cheat;
	
	/* Keeps track of difficulty */
	private int difficulty;
	
	public static void main(String[] args) {
		new EvilHangman().start(args);
	}
	
	/* Adds the HangmanCanvas window. */
	public void init() {
		canvas = new EvilHangmanCanvas();
		add(canvas);
	}
	
	
	/*
	 * Runs Hangman until the user enters "stop".
	 * Section 1 sets up lives counter, random word, and initial game status
	 * Section 2 runs the game until no lives left or word has been guessed
	 * Section 3 prints messages dependent on success/failure
	 * Section 4 asks user to play again
	 */
    public void run() {
		while(true) {
			//Section 1
			setup();
			int lives = STARTING_LIVES;
			int random = rgen.nextInt(0, hl.getWordCount() - 1);
			word = hl.getWord(random);
			String status = "";
			for (int i = 0; i < word.length(); i++) {
				status += "-";
			}
			canvas.reset();
			canvas.displayWord(status);
			
			//Section 2
			boolean result = gameRun(lives, status);
			
			//Section 3
			if (result) println("Congratulations! You've guessed the word " + word + ".");
			else println("Sorry! The word was " + word + ".");
			
			//Section 4
			println("Enter anything to play again. Enter \"stop\" to stop playing.");
			String user_input = readLine("");
			if (user_input.equals("stop")) break;
			println("");
		}
	}

    /* resets list, hashmap, difficulty, starts audio, and displays intro message */
    private void setup(){
    	guessList = new ArrayList<Character>();
		statusMap = new HashMap<Integer, Character>();
		difficulty = 0;
		epicClip.play();
		println("Welcome to Hangman!");
		cheat = readLine("For cheat mode, enter \"cheat\": ");
		println("How evil do you want me to be?");
		while (difficulty != 1 && difficulty != 2 && difficulty != 3){
			difficulty = readInt ("1 = normal, 2 = sinful, 3 = evil: ");
		}
    }
    
    /*
     * Runs the actual gameplay.
     * Section 1 prints current game status
     * Section 2 checks if the guess is legal
     * Section 3 checks if the guess is in the word and updates status & lives accordingly
     */
    private boolean gameRun(int lives, String status) {
    	while(lives > 0) {
    		
			//Section 1
    		println("The word now looks like this: " + status);
			println("You now have " + lives + " guesses left.");
			
			// Section 2
			String guessString = readLine("Your guess: ").toUpperCase();
			if (guessString.length() != 1) println ("Illegal guess! Only one letter at a time please.");
			else {
				char guess = guessString.charAt(0);
				if (!Character.isLetter(guess)) println ("Illegal guess! Letters only please.");
				
				// Section 3
				else {
					guessList.add(guess);
					word = tryReplace(guess);
					status = updateStatus(guess, status);
					if (checkGuess(guess)){
						println("That guess is correct.");
						canvas.displayWord(status);
						niceClip.play();
					}
					else {
						println("There are no " + guess + "'s in the word.");
						lives--;
						canvas.noteIncorrectGuess(guess);
						canvas.displayWord(status);
						noClip.play();
					}
					if(cheat.equals("cheat")) println("Current word: " + word);
				}
			}
			println("");
			if (status.equals(word) == true) return true;
			
		}
    	return false;
    }
    
    /* Returns true if the guessed letter is in the word */
    private boolean checkGuess(char guess) {
    	if (word.indexOf(guess) != -1) return true;
    	else return false;
    }
    
    /* Updates the status by revealing the correctly guessed letter instances and preserving other -'s */
    private String updateStatus(char guess, String prevStatus) {
    	String newStatus = "";
    	for (int i = 0; i < word.length(); i++) {
    		if (word.charAt(i) == guess) {
    			newStatus += word.charAt(i);
    			statusMap.put(i, guess);
    		}
    		else newStatus += prevStatus.charAt(i);
    	}
    	return newStatus;
    }
    
    /*
     * Tries to replace word with one that doesn't contain the guess
     * If fails, returns old word
     */
    private String tryReplace(char guess) {
    	if (difficulty == 1 || !checkGuess(guess)) return word;
    	else {
    		boolean chance = rgen.nextBoolean();
    		if (difficulty == 3) chance = true;
    		if (chance) {
    			for (int i = 0; i < hl.getWordCount(); i++) {
    				String newWord = hl.getWord(i);
    				if (statusCheck(newWord)) return newWord;
    			}
    		}
        	return word;
    	}
    }
    
    /* 
     * Checks, through statusMap, whether the old status will match the new word
     */
    private boolean statusCheck(String newWord){
    	if (checkBadGuessAll(newWord) || newWord.length() != word.length()) return false;
    	Iterator<Integer> iterator = statusMap.keySet().iterator(); 
		while (iterator.hasNext()) {
			int position = iterator.next();
			if(newWord.charAt(position)!= statusMap.get(position)) return false;
		}
    	return true;
    }
    
    /* Returns true if any past incorrectly guessed letters are in the word */
    private boolean checkBadGuessAll(String newWord) {
    	for (int i = 0; i < guessList.size(); i++) {
    		char guess = guessList.get(i);
    		if (newWord.indexOf(guess) != -1) return true;
    	}
    	return false;
    }
}
