package aha.aoc2024.day08;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Pos;
import aha.aoc2024.Utils.Symbol;

public class Part2 extends Part1 {
	
	// https://adventofcode.com/2024/day/8#part2
	
	@Override
	protected List<Pos> getAntinodes(final CharMap cm, final Symbol a1, final Symbol a2) {
		final List<Pos> ret = new LinkedList<>();
		
		int dist = 0; // so they are all also antinodes!
		while (true) {
			final Pos p = getP(a1, a2, dist);
			if (cm.isOutside(p.x, p.y))
				break;
			ret.add(p);
			dist++;
		}
		
		return ret;
	}

	@Override
	public void aTest() {
		assertEquals(34, new Part2().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(927, new Part2().compute("input.txt").res);
	}

}
