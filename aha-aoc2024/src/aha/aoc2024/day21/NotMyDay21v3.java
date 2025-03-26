package aha.aoc2024.day21;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aha.aoc2024.Utils;

public class NotMyDay21v3 {
	
	public static void main(final String[] args) {
		new NotMyDay21v3().solve();
	}
	
	private static class Pair<K, V> implements Map.Entry<K, V> {
		private K k;
		private V v;
		
		@Override
		public K getKey() {
			return this.k; }

		@Override
		public V getValue() {
			return this.v; }

		@Override
		public V setValue(final V value) {
			this.v = value;
			return this.v;
		}

		public static Pair<Integer, Integer> of(final int i1, final int i2) {
			final Pair<Integer, Integer> ret = new Pair<>();
			ret.k = i1;
			ret.v = i2;
			return ret;
		}

		public static Pair<Character, Character> of(final char c1, final char c2) {
			final Pair<Character, Character> ret = new Pair<>();
			ret.k = c1;
			ret.v = c2;
			return ret;
		}

		public static Pair<Integer, String> of(final int i, final String s) {
			final Pair<Integer, String> ret = new Pair<>();
			ret.k = i;
			ret.v = s;
			return ret;
		}
		
		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (obj == null || !(obj instanceof Pair))
				return false;
			final Pair o = (Pair) obj;
			return o.getLeft().equals(this.getLeft()) && o.getRight().equals(this.getRight());
		}
		
		@Override
		public int hashCode() {
			return getLeft().hashCode() * 1024 + getRight().hashCode();
		}

		public V getRight() { return getValue(); }
		public K getLeft() { return getKey(); }
		
	}

	private final Map<Character, Pair<Integer, Integer>> numPadCells = Map.ofEntries(
			Map.entry('7', Pair.of(0, 0)),
			Map.entry('8', Pair.of(0, 1)),
			Map.entry('9', Pair.of(0, 2)),
			Map.entry('4', Pair.of(1, 0)),
			Map.entry('5', Pair.of(1, 1)),
			Map.entry('6', Pair.of(1, 2)),
			Map.entry('1', Pair.of(2, 0)),
			Map.entry('2', Pair.of(2, 1)),
			Map.entry('3', Pair.of(2, 2)),
			Map.entry('0', Pair.of(3, 1)),
			Map.entry('A', Pair.of(3, 2))
			);

	private final Map<Character, Pair<Integer, Integer>> dirPadCells = Map.of(
			'^', Pair.of(0, 1),
			'A', Pair.of(0, 2),
			'<', Pair.of(1, 0),
			'v', Pair.of(1, 1),
			'>', Pair.of(1, 2)
			);

	private final Map<Pair<Character, Character>, String> numPadMoves = new HashMap<>();
	private final Map<Pair<Character, Character>, String> dirPadMoves = new HashMap<>();

	// how many manual button presses does it take to execute a given sequence of moves, N keypads deep?
	private final Map<Pair<Integer, String>, Long> moveCache = new HashMap<>();

	public void solve() {
		final List<String> lines = Utils.readLines("day21/input.txt");
		findNumPadMoves();
		findDirPadMoves();
		System.out.println(getTotalComplexity(lines, 2));
		System.out.println(getTotalComplexity(lines, 25));
	}

	private void findNumPadMoves() {
		for (final char from : this.numPadCells.keySet())
			for (final char to : this.numPadCells.keySet())
				this.numPadMoves.put(Pair.of(from, to), generateNumPadMoves(this.numPadCells.get(from), this.numPadCells.get(to)));
	}

	private void findDirPadMoves() {
		for (final char from : this.dirPadCells.keySet())
			for (final char to : this.dirPadCells.keySet())
				this.dirPadMoves.put(Pair.of(from, to), generateDirPadMoves(this.dirPadCells.get(from), this.dirPadCells.get(to)));
	}

	private long getTotalComplexity(final List<String> lines, final int dirPadCount) {
		return lines.stream().map(line -> getLineComplexity(line, dirPadCount)).reduce(Long::sum).orElse(-1L);
	}

	private long getLineComplexity(final String line, final int dirPadCount) {
		return Long.parseLong(line.substring(0, 3), 10) * countSteps(getNumpadMoves(line), dirPadCount);
	}

	private String getNumpadMoves(final String line) {
		final StringBuilder output = new StringBuilder();
		for (int i = 0; i < line.length(); i++)
			output.append(this.numPadMoves.get(Pair.of(i == 0 ? 'A' : line.charAt(i - 1), line.charAt(i))));
		return output.toString();
	}

	private long countSteps(final String line, final int dirPadCount) {
		if (dirPadCount == 0) return line.length();
		if ("A".equals(line)) return 1;
		final Pair<Integer, String> key = Pair.of(dirPadCount, line);
		if (this.moveCache.containsKey(key)) return this.moveCache.get(key);
		long sum = 0;
		final String[] moves = line.split("A");
		for (final String move : moves) {
			final StringBuilder output = new StringBuilder();
			for (int i = 0; i <= move.length(); i++)
				// losing A's in the regex, so you have to put them back in here
				output.append(this.dirPadMoves.get(
						Pair.of(
								i == 0 ? 'A' : move.charAt(i - 1),
										i == move.length() ? 'A' : move.charAt(i))
						));
			sum += countSteps(output.toString(), dirPadCount - 1);
		}
		this.moveCache.put(key, sum);
		return sum;
	}



	private String generateNumPadMoves(final Pair<Integer, Integer> startPos, final Pair<Integer, Integer> endPos) {
		// go left first, if necessary
		boolean vertFirst = startPos.getRight() <= endPos.getRight();
		// but if that would involve going over the blank square, go vertically
		if (startPos.getLeft() == 3 && endPos.getRight() == 0) vertFirst = true;
		// and if THAT would move you over the blank square, go horizontally
		if (startPos.getRight() == 0 && endPos.getLeft() == 3) vertFirst = false;
		return generateMoves(startPos, endPos, vertFirst);
	}

	private String generateDirPadMoves(final Pair<Integer, Integer> startPos, final Pair<Integer, Integer> endPos) {
		// go left first, if necessary
		boolean vertFirst = startPos.getRight() <= endPos.getRight();
		// but if that would involve going over the blank square, go vertically
		if (startPos.getLeft() == 0 && endPos.getRight() == 0) vertFirst = true;
		// and if THAT would move you over the blank square, go horizontally
		if (startPos.getRight() == 0 && endPos.getLeft() == 0) vertFirst = false;
		return generateMoves(startPos, endPos, vertFirst);
	}

	private String generateMoves(final Pair<Integer, Integer> startPos, final Pair<Integer, Integer> endPos, final boolean vertFirst) {
		final StringBuilder result = new StringBuilder();
		if (vertFirst) {
			int vertDiff = endPos.getLeft() - startPos.getLeft();
			while (vertDiff < 0) {
				result.append("^");
				vertDiff++;
			}
			while (vertDiff > 0) {
				result.append("v");
				vertDiff--;
			}
		}
		int horzDiff = endPos.getRight() - startPos.getRight();
		while (horzDiff < 0) {
			result.append("<");
			horzDiff++;
		}
		while (horzDiff > 0) {
			result.append(">");
			horzDiff--;
		}
		if (!vertFirst) {
			int vertDiff = endPos.getLeft() - startPos.getLeft();
			while (vertDiff < 0) {
				result.append("^");
				vertDiff++;
			}
			while (vertDiff > 0) {
				result.append("v");
				vertDiff--;
			}
		}
		return result + "A";
	}
}