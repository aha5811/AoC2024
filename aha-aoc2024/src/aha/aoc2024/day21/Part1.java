package aha.aoc2024.day21;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.Pos;

public class Part1 extends Part {

	// https://adventofcode.com/2024/day/21
	
	@Override
	public Part compute(final String file) {

		for (final String code : Utils.readLines(this.dir + file)) {
			final String keys = computeKeys(code);
			this.res += Integer.parseInt(code.replaceAll("\\D", "")) * keys.length();
		}

		return this;
	}

	private String computeKeys(final String code) {
		String keys = getKeys(code, this.pad1);
		keys = getKeys(keys, this.pad2);
		keys = getKeys(keys, this.pad2);
		return keys;
	}
	
	@SuppressWarnings("unused")
	private String toString(final char[][] pad) {
		String ret = "";
		for (final char[] element : pad) {
			for (int x = 0; x < pad[0].length; x++)
				ret += element[x];
			ret += "\n";
		}
		return ret;
	}

	final char[][]
			pad1 = new char[][] {
		new char[] { '7', '8', '9' },
		new char[] { '4', '5', '6' },
		new char[] { '1', '2', '3' },
		new char[] { '_', '0', 'A' },
	},
			pad2 = new char[][] {
		new char[] { '_', '^', 'A' },
		new char[] { '<', 'v', '>' },
	};
	
	// 029A
	// <A^A>^^AvvvA
	// v<<A>>^A<A>AvA<^AA>A<vAAA>^A
	// <vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A
	
	private String getKeys(final String code, final char[][] pad) {
		String ret = "";
		Pos now = getPos(pad, 'A');
		for (final char c : code.toCharArray()) {
			final Pos cPos = getPos(pad, c);

			// heuristic: same chars together

			String ud = "";
			final int dy = cPos.y - now.y;
			if (dy != 0)
				ud = (dy > 0 ? "v" : "^").repeat(Math.abs(dy));

			String lr = "";
			final int dx = cPos.x - now.x;
			if (dx != 0)
				lr = (dx > 0 ? ">" : "<").repeat(Math.abs(dx));
			
			// heuristic: left/right first
			// constraint: not over _
			
			final String add = pad[now.y][now.x + dx] != '_' ? lr + ud : ud + lr;
			
			ret += add + "A";
			now = cPos;
		}
		return ret;
	}
	
	// https://www.reddit.com/r/adventofcode/comments/1hjaz2z/2024_day_21_part_1_wrong_combination/
	// https://www.reddit.com/r/adventofcode/comments/1hj2odw/2024_day_21_solutions/
	//
	
	private Pos getPos(final char[][] pad, final char c) {
		for (int y = 0; y < pad.length; y++)
			for (int x = 0; x < pad[0].length; x++)
				if (pad[y][x] == c)
					return new Pos(x, y);
		return null;
	}

	@Test
	public void test11() {
		final String
		e = "<A^A>^^AvvvA",
		a = new Part1().getKeys("029A", this.pad1);
		assertEquals(e.length(), a.length());
	}

	@Test
	public void test12() {
		final String
		e = "v<<A>>^A<A>AvA<^AA>A<vAAA>^A",
		a = new Part1().getKeys("<A^A>^^AvvvA", this.pad2);
		assertEquals(e.length(), a.length());
	}

	@Test
	public void test13() {
		final String
		e = "<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A",
		a = new Part1().getKeys("v<<A>>^A<A>AvA<^AA>A<vAAA>^A", this.pad2);
		assertEquals(e.length(), a.length());
	}
	
	@Test
	public void test21() {
		final String
		e = "<A^A>^^AvvvA",
		a = new Part1().getKeys("029A", this.pad1);
		assertEquals(e, a);
	}

	@Test
	public void test22() {
		final String
		e = "v<<A>>^A<A>AvA<^AA>A<vAAA>^A",
		a = new Part1().getKeys("<A^A>^^AvvvA", this.pad2);
		assertEquals(e, a);
	}

	@Test
	public void test23() {
		final String
		e = "<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A",
		a = new Part1().getKeys("v<<A>>^A<A>AvA<^AA>A<vAAA>^A", this.pad2);
		assertEquals(e, a);
	}

	@Test
	public void test41() {
		assertEquals(68, new Part1().computeKeys("029A").length());
	}
	
	@Test
	public void test42() {
		assertEquals(60, new Part1().computeKeys("980A").length());
	}
	
	@Test
	public void test43() {
		assertEquals(68, new Part1().computeKeys("179A").length());
	}
	
	@Test
	public void test44() {
		assertEquals(64, new Part1().computeKeys("456A").length());
	}
	
	@Test
	public void test45() {
		assertEquals(64, new Part1().computeKeys("379A").length());
	}

	@Test
	public void test35() {
		String keys = "379A";
		keys = new Part1().getKeys(keys, this.pad1);
		System.out.println(keys);
		keys = new Part1().getKeys(keys, this.pad2);
		System.out.println(keys);
		keys = new Part1().getKeys(keys, this.pad2);
		System.out.println(keys);

		assertEquals(
				"<v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A",
				keys);
	}
	
	@Override
	public void aTest() {
		assertEquals(126384, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		// not my solution
		assertEquals(224326, new Part1().compute("input.txt").res);
	}
	
}
