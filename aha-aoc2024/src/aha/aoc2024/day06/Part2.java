package aha.aoc2024.day06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;

public class Part2 extends Part1 {

	private final static String dcs;
	static {
		String s = "";
		for (final int[] dir : DIRS)
			s += toChar(dir);
		dcs = s;
	}

	private final List<int[]> os = new LinkedList<>();

	@Override
	protected void doMore(final Guard g, final char nextC) {
		// if nextC is a right turn to current dir then here could be a obstacle
		if (dcs.indexOf(nextC) != -1) {
			final int[] dir = g.dirs.get(0);
			if (dir == Utils.U && nextC == toChar(Utils.R)
					|| dir == Utils.R && nextC == toChar(Utils.D)
					|| dir == Utils.D && nextC == toChar(Utils.L)
					|| dir == Utils.L && nextC == toChar(Utils.U))
			{
				this.res++;
				this.os.add(g.nextPos());
			}
		}

		// this won't find all obstacles :(
	}

	@Override
	protected void computeRes(final CharMap cm) {
		for (final int[] O : this.os)
			System.out.println(O[0] + "x" + O[1]);
	}

	@Override
	public void aTest() {
		assertEquals(6, new Part2().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		// assertEquals(0, new Part2().compute("input.txt").res);
	}
	
}
