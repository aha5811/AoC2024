package aha.aoc2024.day21;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import aha.aoc2024.Utils;

/**
 * https://github.com/dirk527/aoc2021/blob/main/src/aoc2024/Day21.java
 */
public class NotMyDay21v2 {
	private static final HashMap<Character, Pos> dirPad = new HashMap<>();
	private static final Pos dirPadCrash = new Pos(0, 0);
	private static HashMap<Key, Long> bestOnDirPadCache = new HashMap<>();

	public static void main(final String[] args) throws IOException {
		final List<String> codes = Utils.readLines("day21/input.txt");

		/*
        | 7 | 8 | 9 |
        | 4 | 5 | 6 |
        | 1 | 2 | 3 |
            | 0 | A |
		 */
		final HashMap<Character, Pos> numericPad = new HashMap<>();
		numericPad.put('7', new Pos(0, 0));
		numericPad.put('8', new Pos(1, 0));
		numericPad.put('9', new Pos(2, 0));
		numericPad.put('4', new Pos(0, 1));
		numericPad.put('5', new Pos(1, 1));
		numericPad.put('6', new Pos(2, 1));
		numericPad.put('1', new Pos(0, 2));
		numericPad.put('2', new Pos(1, 2));
		numericPad.put('3', new Pos(2, 2));
		numericPad.put('0', new Pos(1, 3));
		numericPad.put('A', new Pos(2, 3));
		final Pos numericPadCrash = new Pos(0, 3);

		dirPad.put('^', new Pos(1, 0));
		dirPad.put('A', new Pos(2, 0));
		dirPad.put('<', new Pos(0, 1));
		dirPad.put('v', new Pos(1, 1));
		dirPad.put('>', new Pos(2, 1));

		long p1 = 0;
		long p2 = 0;
		for (final String code : codes) {
			final List<String> numPadMoves = movesOnPad(numericPad, numericPadCrash, code, numericPad.get('A'));
			List<String> moves = numPadMoves;
			final int dirPadCount = 2;
			for (int i = 0; i < dirPadCount; i++) {
				final List<String> newMoves = new ArrayList<>();
				for (final String move : moves)
					newMoves.addAll(movesOnPad(dirPad, dirPadCrash, move, dirPad.get('A')));
				moves = newMoves;
			}
			final ArrayList<String> all = new ArrayList<>(moves);
			all.sort(Comparator.comparingInt(String::length));
			final long codeNum = Long.parseLong(code.substring(0, 3));
			long complexity = codeNum * all.getFirst().length();
			p1 += complexity;

			long len = Long.MAX_VALUE;
			for (final String numPadMove : numPadMoves) {
				char prev = 'A';
				long candLen = 0;
				for (final char c : numPadMove.toCharArray()) {
					candLen += calcBestOnDirPad(25, c, prev);
					prev = c;
				}
				len = Math.min(len, candLen);
			}
			complexity = codeNum * len;
			p2 += complexity;
		}
		System.out.println(p1);
		System.out.println(p2);

		/*
        | 7 | 8 | 9 |                       | ^ | A |
        | 4 | 5 | 6 |                   | < | v | > |
        | 1 | 2 | 3 |
            | 0 | A |
		 */

		/*
        Proof that order of movement matters some pads down:

      0             3                          7          9                 A
      1         ^   A       ^^        <<       A     >>   A        vvv      A
      2     <   A > A   <   AA  v <   AA >>  ^ A  v  AA ^ A  v <   AAA >  ^ A
      3  v<<A>>^AvA^Av<<A>>^AAv<A<A>>^AAvAA^<A>Av<A>^AA<A>Av<A<A>>^AAAvA^<A>A  longer

      0             3                      7          9                 A
      1         ^   A         <<      ^^   A     >>   A        vvv      A
      2     <   A > A  v <<   AA >  ^ AA > A  v  AA ^ A   < v  AAA >  ^ A
      3  <v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A      shorter
		 */
	}

	private static long calcBestOnDirPad(final int padCount, final char move, final char start) {
		final Key key = new Key(padCount, move, start);
		if (bestOnDirPadCache.containsKey(key))
			return bestOnDirPadCache.get(key);
		long ret = Long.MAX_VALUE;
		for (final String moveCand : movesOnPad(dirPad, dirPadCrash, String.valueOf(move), dirPad.get(start))) {
			long candLen = 0;
			if (padCount == 1)
				candLen = moveCand.length();
			else {
				char prev = 'A';
				for (int i = 0; i < moveCand.length(); i++) {
					final char c = moveCand.charAt(i);
					candLen += calcBestOnDirPad(padCount - 1, c, prev);
					prev = c;
				}
			}
			ret = Math.min(ret, candLen);
		}
		bestOnDirPadCache.put(key, ret);
		return ret;
	}

	private static List<String> movesOnPad(final HashMap<Character, Pos> pad, final Pos crash, final String code, final Pos start) {
		Pos curPos = start;
		final List<StringBuilder> ret = new ArrayList<>();
		ret.add(new StringBuilder());
		for (final Character c : code.toCharArray()) {
			final Pos nextPos = pad.get(c);
			if (nextPos == null)
				System.out.println("*****");
			final List<List<Direction>> sequences = new ArrayList<>(2);
			for (final Direction dir : Direction.values()) {
				final List<Direction> m = dir.calcMoves(curPos, nextPos);
				if (!m.isEmpty())
					sequences.add(m);
			}

			if (sequences.size() == 2) {
				boolean firstFirstPossible = true;
				int x = curPos.x;
				int y = curPos.y;
				for (final Direction d : sequences.get(0)) {
					x += d.x;
					y += d.y;
					if (crash.x == x && crash.y == y) {
						firstFirstPossible = false;
						break;
					}
				}
				boolean secondFirstPossible = true;
				x = curPos.x;
				y = curPos.y;
				for (final Direction d : sequences.get(1)) {
					x += d.x;
					y += d.y;
					if (crash.x == x && crash.y == y) {
						secondFirstPossible = false;
						break;
					}
				}
				if (firstFirstPossible && !secondFirstPossible)
					for (final StringBuilder sb : ret) {
						sequences.get(0).forEach(d -> sb.append(d.c));
						sequences.get(1).forEach(d -> sb.append(d.c));
						sb.append('A');
					}
				else if (!firstFirstPossible && secondFirstPossible)
					for (final StringBuilder sb : ret) {
						sequences.get(1).forEach(d -> sb.append(d.c));
						sequences.get(0).forEach(d -> sb.append(d.c));
						sb.append('A');
					}
				else {
					final List<StringBuilder> add = new ArrayList<>();
					for (final StringBuilder sb : ret) {
						final StringBuilder sb2 = new StringBuilder();
						sb2.append(sb.toString());
						sequences.get(0).forEach(d -> sb.append(d.c));
						sequences.get(1).forEach(d -> sb.append(d.c));
						sb.append('A');
						sequences.get(1).forEach(d -> sb2.append(d.c));
						sequences.get(0).forEach(d -> sb2.append(d.c));
						sb2.append('A');
						add.add(sb2);
					}
					ret.addAll(add);
				}
			} else
				for (final StringBuilder sb : ret) {
					if (!sequences.isEmpty())
						sequences.getFirst().forEach(d -> sb.append(d.c));
					sb.append('A');
				}
			curPos = nextPos;
		}
		return ret.stream().map(StringBuilder::toString).toList();
	}

	record Pos(int x, int y) {
	}

	record Key(int count, char move, char start) {
	}

	enum Direction {
		UP(0, -1, '^'),
		DOWN(0, 1, 'v'),
		LEFT(-1, 0, '<'),
		RIGHT(1, 0, '>');

		int x;
		int y;
		char c;

		Direction(final int x, final int y, final char c) {
			this.x = x;
			this.y = y;
			this.c = c;
		}

		List<Direction> calcMoves(final Pos start, final Pos end) {
			final List<Direction> moves = new ArrayList<>();
			switch (this) {
				case UP -> {
					for (int i = start.y; i > end.y; i--)
						moves.add(UP);
				}
				case DOWN -> {
					for (int i = start.y; i < end.y; i++)
						moves.add(DOWN);
				}
				case LEFT -> {
					for (int i = start.x; i > end.x; i--)
						moves.add(LEFT);
				}
				case RIGHT -> {
					for (int i = start.x; i < end.x; i++)
						moves.add(RIGHT);
				}
			}
			return moves;
		}
	}
}