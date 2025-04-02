package aha.aoc2024.day21;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * https://github.com/7UpMan/AoC/blob/main/Day21/Day21.java
 */
public class NotMyDay21v1 {

	// Part A: 194944 - too high
	// Part A: 143370 - too low
	// Part A: 188192 - too high
	// Part A: 184716 - Correct
	// Part B: 229403562787554 - Correct
	
	// Want some detail displayed?
	public static final boolean DETAIL = true;
	

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		new Puzzle().solve(2);
		new Puzzle().solve(25);
	}
}

class Puzzle {
	private final ArrayList<String> puzzleInput = new ArrayList<>();
	
	public Puzzle() throws Exception {
		// Read the puzzle input and put into an array for processing
		try (var input = Objects.requireNonNull(getClass().getResourceAsStream("input.txt"))) {
			for (final String lines : new BufferedReader(new InputStreamReader(input)).lines().toList())
				this.puzzleInput.add(lines);
		}
	}
	
	public void solve(final int robotDepth) {
		final PadController numberController = new PadController(PadType.NUMBERS);
		final PadController arrowController = new PadController(PadType.ARROWS);
		
		long total = 0;
		
		// Iterate thought the inputs
		for (final String puzzleItem : this.puzzleInput) {
			// Create a cache for this work. Key is command string, data is count of occurances.
			HashMap<String, Long> currentCache = new HashMap<>();
			
			// Get the sequence from the numeric controller and store it in the cache
			final String commandString = numberController.getKeySeqence(puzzleItem);
			updateCache(commandString, currentCache, 1L);
			
			// Iterate over the arrow controller based on the number of robots
			currentCache = processCommandString(currentCache, arrowController, robotDepth);
			
			// Expand the cache to get the length of the command string.
			final long commandLength = getCommandLength(currentCache);
			
			// Strip the letters out of the key and return a number
			final long puzzleNumber = Long.parseLong(puzzleItem.replaceAll("[^0-9]", ""));
			
			if (NotMyDay21v1.DETAIL)
				System.out.printf("Key %s, which has numeric part %,d, has length of %,d, giving a product %,d\n",
						puzzleItem, puzzleNumber, commandLength, commandLength * puzzleNumber);
			
			// Running total of products
			total += commandLength * puzzleNumber;
		}
		
		System.out.printf("Total: %,d, or without commas %1$d\n", total);
	}
	
	/**
	 * <p>
	 * We keep a cache which is keyed upon the character sequence (say ">>^A") and
	 * along with
	 * that we keep the number of times that it is used. We then process each item
	 * through
	 * the PadController to get the new sequences. These new sequenecs are then
	 * sored
	 * in the new cache. We swap the caches over, and do the entire thing again.<\p>
	 *
	 * <p>
	 * Some points of note:
	 * <ul>
	 * <li>There are only 12 possible sequences generated from the arrow
	 * controller</li>
	 * <li>If sequence S occurs 5 times, when we put it through the PadController it
	 * may generate seuence T
	 * and sequence U. This menas that in the new cache we have to have 5 x Sequence
	 * T and 5 x Sequence U.</li>
	 * </ul>
	 * </p>
	 *
	 * @param currentCache    A cache with all of the currnet items
	 * @param arrowController A handle for the pre-configured arrowController
	 * @param robotDepth      How many robots deep do we want to go?
	 */
	private HashMap<String, Long> processCommandString(HashMap<String, Long> currentCache,
			final PadController arrowController, final int robotDepth) {
		
		// Iterate through the robots on arrow pads
		for (int arrowPadCounter = 0; arrowPadCounter < robotDepth; arrowPadCounter++) {
			// System.out.printf("arrowPadCounter=%d, cacheSize=%d\nCache=%s\n",
			// arrowPadCounter, currentCache.size(), currentCache.toString());
			// Put the new snips into a new cache
			final HashMap<String, Long> newCache = new HashMap<>();
			
			// Process each item once, and add to the cache
			for (final Map.Entry<String, Long> e : currentCache.entrySet()) {
				final String commandSnip = e.getKey();
				final String commandString = arrowController.getKeySeqence(commandSnip);
				updateCache(commandString, newCache, e.getValue());
			}
			// Swap the caches over ready for another run.
			currentCache = newCache;
		}
		
		return currentCache;
	}
	
	/**
	 * <p>
	 * The cache has short snippets of commands (say "<vA") associated with the
	 * number of
	 * times that they have been used. We need to summarise that. To take the above
	 * exmaple it
	 * "<vA" has been used 100 times, then we would add "<vA".lenght() (i.e. 3) *
	 * 100 or 300.
	 * </p>
	 * <p>
	 * I have the feeling that this could probably be done witha stream, but I am
	 * too new to
	 * them to do it
	 * </p>
	 * <p>
	 * PS there are only 12 different sequences in use
	 * </p>
	 *
	 * @param currentCache
	 * @return
	 */
	private long getCommandLength(final HashMap<String, Long> currentCache) {
		long commandLength = 0;
		for (final Map.Entry<String, Long> e : currentCache.entrySet()) {
			final int keyLength = e.getKey().length();
			final long occuranceCount = e.getValue();
			commandLength += keyLength * occuranceCount;
		}
		
		return commandLength;
	}
	
	/**
	 * <p>
	 * Split the command string into short character sequences delimited by the A
	 * character. It doesn't
	 * matter if we process "<<^A ^^>A" or "^^>A <<^A" for what we want the outcome
	 * is the same.
	 * </p>
	 *
	 * <p>
	 * In the cache we just store the short sequence and the count of items. We can
	 * then multiply this all out
	 * when we are done.
	 * </p>
	 *
	 * @param commands
	 * @param currentCache
	 */
	private void updateCache(final String commands, final HashMap<String, Long> newCache, final long qty) {
		
		// We cannot use String.split() because it has strange behaviours with strings
		// ending ..AA
		for (final String command : splitCommands(commands))
			// See if we alrady have it in the new Cache
			if (newCache.containsKey(command))
				newCache.put(command, newCache.get(command) + qty);
			else
				// Don't have it anywhere, so create it
				newCache.put(command, qty);
	}
	
	/**
	 * <p>Take a delimited String and split it into a String array. The delimiter is
	 * the "A" character.  Each string in the resulting array has an "A" at
	 * the end of it.</p>
	 * @param s
	 * @return
	 */
	private String[] splitCommands(final String s) {
		final ArrayList<String> commandList = new ArrayList<>();
		String currentCommand = "";
		for (int i = 0; i < s.length(); i++) {
			final char currentChar = s.charAt(i);
			currentCommand += currentChar;
			if (currentChar == 'A') {
				commandList.add(currentCommand);
				currentCommand = new String();
			}
		}
		final String[] retVal = new String[commandList.size()];
		return commandList.toArray(retVal);
	}
}

/**
 * Used to indicate what sort of pad is being emulated, a Numeric pad or an Arrow pad.
 */
enum PadType {
	NUMBERS,
	ARROWS
}

class PadController {
	private static final HashMap<Character, Coordinate> numberLayout = new HashMap<>();
	private static final HashMap<Character, Coordinate> arrowLayout = new HashMap<>();
	private Coordinate currentLocation = null;
	private final PadType padType;
	
	/**
	 * <p>A class that has methods returning the required keystrokes for a given inpyt bahaviour.  First you
	 * have to configure the class spexifying a padType.  This is from PadController.PadType and
	 * is either NUMBERS or ARROWS depending on what you want.</p>
	 *
	 * <p>Once configured pass in the required string (say "012A" or a NUMBER pad or "^<>vA" for an
	 * ARROW pad and you will get the correct sequence back again).</p>
	 *
	 * <p>As every movement finishes with an "A" (i.e. the internal state is reset) you only need one
	 * object per PadType.</p>
	 */
	
	public PadController(final PadType padType) {
		this.padType = padType;
		
		// Configure the number pad layout if not already set
		if (numberLayout.size() == 0) {
			numberLayout.put('0', new Coordinate(1, 0));
			numberLayout.put('A', new Coordinate(2, 0));
			numberLayout.put('1', new Coordinate(0, 1));
			numberLayout.put('2', new Coordinate(1, 1));
			numberLayout.put('3', new Coordinate(2, 1));
			numberLayout.put('4', new Coordinate(0, 2));
			numberLayout.put('5', new Coordinate(1, 2));
			numberLayout.put('6', new Coordinate(2, 2));
			numberLayout.put('7', new Coordinate(0, 3));
			numberLayout.put('8', new Coordinate(1, 3));
			numberLayout.put('9', new Coordinate(2, 3));
		}
		
		// Configure the arrow pad layout if not already set
		if (arrowLayout.size() == 0) {
			arrowLayout.put('<', new Coordinate(0, 0));
			arrowLayout.put('v', new Coordinate(1, 0));
			arrowLayout.put('>', new Coordinate(2, 0));
			arrowLayout.put('^', new Coordinate(1, 1));
			arrowLayout.put('A', new Coordinate(2, 1));
		}
		
		// Set the current position as the letter A
		if (padType == PadType.NUMBERS)
			this.currentLocation = numberLayout.get('A');
		else
			this.currentLocation = arrowLayout.get('A');
		
	}
	

	/**
	 * <p>This will return a sequence of key presses based on the input sequence of keys.  Internall the
	 * method does slightly difference things depenging on the padType.</p>
	 *
	 * <p>The actual work of deducing what is the correct key sequence for any given situation was done by
	 * "<a href="https://www.reddit.com/user/Prof_McBurney/">Prof_McBurney</a>" (so all credit to him) and documented here:
	 * <a href="https://www.reddit.com/r/adventofcode/comments/1hjgyps/2024_day_21_part_2_i_got_greedyish/">
	 * https://www.reddit.com/r/adventofcode/comments/1hjgyps/2024_day_21_part_2_i_got_greedyish/</a>.  Without
	 * this artical I would still be working on this.</p>
	 *
	 * @param input
	 * @return String
	 */
	public String getKeySeqence(final String input) {
		final StringBuffer sb = new StringBuffer();
		
		for (final char targetChar : input.toCharArray()) {
			Coordinate targetLocation;
			if(this.padType == PadType.NUMBERS)
				targetLocation = numberLayout.get(targetChar);
			else
				targetLocation = arrowLayout.get(targetChar);

			
			ForcedOrder forcedOrder = ForcedOrder.NO_PREFERENCE;
			
			if (this.padType == PadType.NUMBERS) {
				// Bottom row going to left column
				if (this.currentLocation.y() == 0 && targetLocation.x() == 0)
					forcedOrder = ForcedOrder.UD_LR;
				// Left column going to bottom row
				if (this.currentLocation.x() == 0 && targetLocation.y() == 0)
					forcedOrder = ForcedOrder.LR_UD;
			} else {
				// PadType.ARROWS
				
				// Currently in left column
				if (this.currentLocation.x() == 0)
					forcedOrder = ForcedOrder.LR_UD;
				
				// Target is left column
				if (targetLocation.x() == 0)
					forcedOrder = ForcedOrder.UD_LR;
			}
			
			sb.append(getSequenceFromXY(targetLocation.x() - this.currentLocation.x(),
					targetLocation.y() - this.currentLocation.y(), forcedOrder));
			sb.append("A");
			this.currentLocation = targetLocation;
		}
		
		return sb.toString();
	}
	

	/**
	 * <p>Pass in the xDelta and yDelta and get returned the most efficient way of tetting from one place
	 * to the other.  The order can be forced using forcedOrder (and the associated enum).</p>
	 *
	 * <p>The hard work of figuring out what the order should be was done by someone else and
	 * covered in great detail within <a href="https://www.reddit.com/r/adventofcode/comments/1hjgyps/2024_day_21_part_2_i_got_greedyish/">
	 * https://www.reddit.com/r/adventofcode/comments/1hjgyps/2024_day_21_part_2_i_got_greedyish/</a></p>
	 *
	 * From: https://www.reddit.com/r/adventofcode/comments/1hjgyps/2024_day_21_part_2_i_got_greedyish/
	 *  +---------------------------------------+
	 *  | Path       | Best Order               |
	 *  +------------|--------------------------+
	 *  | Up Left    | horizontal then vertical |
	 *  | Down Left  | horizontal then vertical |
	 *  | Down Right | vertical then horizontal |
	 *  | Up Right   | vertical then horizontal |
	 *  +---------------------------------------+
	 *
	 * @param xDelta with right as positive.
	 * @param yDelta with up as positive.
	 * @param forcedOrder a value from the enum PadController.ForceOrder.
	 * @return String
	 */
	private String getSequenceFromXY(final int xDelta, final int yDelta, ForcedOrder forcedOrder) {
		final StringBuffer sb = new StringBuffer();
		
		final String horizontal = xDelta > 0 ? ">".repeat(xDelta) : "<".repeat(Math.abs(xDelta));
		final String vertical = yDelta > 0 ? "^".repeat(yDelta) : "v".repeat(Math.abs(yDelta));
		
		if (forcedOrder == ForcedOrder.NO_PREFERENCE)
			if (xDelta < 0)
				forcedOrder = ForcedOrder.LR_UD;
			else
				forcedOrder = ForcedOrder.UD_LR;
		
		switch (forcedOrder) {
			case UD_LR:
				sb.append(vertical);
				sb.append(horizontal);
				break;
			case LR_UD:
				sb.append(horizontal);
				sb.append(vertical);
				break;
			default:
				System.out.printf("Should not get here\n");
				System.exit(1);
		}
		return sb.toString();
	}
	
	public enum ForcedOrder {
		NO_PREFERENCE,
		UD_LR,
		LR_UD
	}
}

record Coordinate(int x, int y) { }
