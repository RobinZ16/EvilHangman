/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;
import java.awt.*;


public class EvilHangmanCanvas extends GCanvas {

/** Resets the display so that only the scaffold appears */
	public void reset() {
		removeAll();
		guessesString = ("");
		Color skyBlue = new Color(231, 254, 255);
		setBackground(skyBlue);
		
		centerX = getWidth() / 2;
		centerY = getHeight() / 2;
		double scafX = centerX - BEAM_LENGTH;
		double scafY = centerY - SCAFFOLD_HEIGHT;
		GLine scaf = new GLine(scafX, scafY, scafX, centerY);
		GLine beam = new GLine(scafX, scafY, centerX, scafY);
		GLine rope = new GLine(centerX, scafY, centerX, scafY + ROPE_LENGTH);
		add(scaf);
		add(beam);
		add(rope);
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 * Also includes a lives counter.
 */
	public void displayWord(String word) {
		remove(status);
		remove(livesCounter);
		
		status = new GLabel(word);
		status.setFont("Helvetica-26");
		add(status, WORD_OFFSETX, centerY + WORD_OFFSETY);
		
		int lives = STARTING_LIVES - guessesString.length() / 2;
		livesCounter = new GLabel("Lives remaining: " + lives);
		livesCounter.setFont("Helvetica-14");
		add(livesCounter, WORD_OFFSETX, centerY + LIVES_OFFSETY);
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter) {
		updateGuesses(letter);
		drawDude();
	}
	
	/*
	 * Updates and displays the incorrect guesses string
	 */
	
	private void updateGuesses(char letter) {
		remove(guesses);
		guessesString += " " + letter;
		guesses = new GLabel(guessesString);
		guesses.setFont("Helvetica-18");
		add(guesses, WORD_OFFSETX, centerY + GUESSES_OFFSETY);
	}
	
	/*
	 * Draws a body part depending on how many lives are remaining
	 */
	
	private void drawDude() {
		switch(guessesString.length() / 2) {
			case 1:
				double headX = centerX - HEAD_RADIUS;
				double headY = centerY - SCAFFOLD_HEIGHT + ROPE_LENGTH;
				head = new GOval(headX, headY, 2 * HEAD_RADIUS, 2 * HEAD_RADIUS);
				add(head);
				break;
				
			case 2:
				double bodyY1 = centerY - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS;
				double bodyY2 = bodyY1 + BODY_LENGTH;
				GLine body = new GLine (centerX, bodyY1, centerX, bodyY2);
				add(body);
				break;
				
			case 3:
				double lArmX = centerX - UPPER_ARM_LENGTH;
				double lUpperArmY = centerY - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD;
				double lLowerArmY = lUpperArmY + LOWER_ARM_LENGTH;
				GLine lUpperArm = new GLine(lArmX, lUpperArmY, centerX, lUpperArmY);
				GLine lLowerArm = new GLine(lArmX, lUpperArmY, lArmX, lLowerArmY);
				add(lUpperArm);
				add(lLowerArm);
				break;
				
			case 4:
				double rArmX = centerX + UPPER_ARM_LENGTH;
				double rUpperArmY = centerY - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD;
				double rLowerArmY = rUpperArmY + LOWER_ARM_LENGTH;
				GLine rUpperArm = new GLine(centerX, rUpperArmY, rArmX, rUpperArmY);
				GLine rLowerArm = new GLine(rArmX, rUpperArmY, rArmX, rLowerArmY);
				add(rUpperArm);
				add(rLowerArm);
				break;
				
			case 5:
				double lHipX = centerX - HIP_WIDTH;
				double lHipY = centerY - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH;
				double lLegY = centerY - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH;
				GLine lHip = new GLine(lHipX, lHipY, centerX, lHipY);
				GLine lLeg = new GLine(lHipX, lHipY, lHipX, lLegY);
				add(lHip);
				add(lLeg);
				break;
				
			case 6:
				double rHipX = centerX + HIP_WIDTH;
				double rHipY = centerY - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH;
				double rLegY = centerY - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH;
				GLine rHip = new GLine(centerX, rHipY, rHipX, rHipY);
				GLine rLeg = new GLine(rHipX, rHipY, rHipX, rLegY);
				add(rHip);
				add(rLeg);
				break;
				
			case 7:
				double lFootX1 = centerX - HIP_WIDTH;
				double lFootX2 = lFootX1 - FOOT_LENGTH;
				double lFootY = centerY - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH;
				GLine lFoot = new GLine(lFootX1, lFootY, lFootX2, lFootY);
				add(lFoot);
				
				GLabel warning = new GLabel("BE CAREFUL");
				warning.setFont("Helvetica-36");
				warning.setColor(Color.RED);
				double warningX = centerX - warning.getWidth() / 2;
				double warningY = centerY + WORD_OFFSETY / 2;
				add(warning, warningX, warningY);
				break;
				
			case 8:
				double rFootX1 = centerX + HIP_WIDTH;
				double rFootX2 = rFootX1 + FOOT_LENGTH;
				double rFootY = centerY - SCAFFOLD_HEIGHT + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH;
				GLine rFoot = new GLine(rFootX1, rFootY, rFootX2, rFootY);
				add(rFoot);
				
				head.setFilled(true);
				head.setColor(Color.RED);
				for (int i = 0; i < 5; i++) {
					GLabel failure = new GLabel("HE'S DEAD NOW");
					failure.setFont("Helvetica-36");
					double failureX = centerX - failure.getWidth() / 2;
					double failureY = centerY - failure.getAscent() / 2;
					add(failure, failureX + i, failureY + i);
				}
				break;
				
			default:
				break;
		}
	}
	
/* Instance variables for the word/letter display */
	private double centerX;
	private double centerY;
	private GLabel status = new GLabel("");
	private GLabel guesses = new GLabel("");
	private GLabel livesCounter = new GLabel("");
	private String guessesString = ("");
	private GOval head;
	
/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 180;
	private static final int BEAM_LENGTH = 72;
	private static final int ROPE_LENGTH = 9;
	private static final int HEAD_RADIUS = 18;
	private static final int BODY_LENGTH = 72;
	private static final int ARM_OFFSET_FROM_HEAD = 14;
	private static final int UPPER_ARM_LENGTH = 36;
	private static final int LOWER_ARM_LENGTH = 22;
	private static final int HIP_WIDTH = 18;
	private static final int LEG_LENGTH = 54;
	private static final int FOOT_LENGTH = 14;
	private static final int WORD_OFFSETX = 50;
	private static final int WORD_OFFSETY = 100;
	private static final int GUESSES_OFFSETY = 150;
	private static final int LIVES_OFFSETY = 175;
	private static final int STARTING_LIVES = 8;
}
