package aha.aoc2024.day20;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aha.aoc2024.Part;
import aha.aoc2024.Utils.Pos;

public class Part2 extends Part1 {
	
	// https://adventofcode.com/2024/day/20#part2
	
	@Override
	public Part compute(final String file) {
		
		final Map<Pos, Integer> p2cnt = new HashMap<>();
		final List<Pos> ps = readFill(file, p2cnt);
		
		for (final Pos p : ps) {
			final int step = p2cnt.get(p);
			for (final Pos t : ps) {
				final int tstep = p2cnt.get(t);
				if (tstep <= step)
					continue;
				final int
				d = Math.abs(t.x - p.x) + Math.abs(t.y - p.y),
				save = tstep - step - d;
				if (d <= 20 && (save == this.target || this.atLeast && save > this.target))
					this.res++;
			}
		}
		
		return this;
	}
	
	@Override
	public void aTest() {
		assertEquals(32, new Part2().setTarget(50).compute("test.txt").res);
		assertEquals(31, new Part2().setTarget(52).compute("test.txt").res);
		assertEquals(29, new Part2().setTarget(54).compute("test.txt").res);
		assertEquals(39, new Part2().setTarget(56).compute("test.txt").res);
		assertEquals(25, new Part2().setTarget(58).compute("test.txt").res);
		assertEquals(23, new Part2().setTarget(60).compute("test.txt").res);
		assertEquals(20, new Part2().setTarget(62).compute("test.txt").res);
		assertEquals(19, new Part2().setTarget(64).compute("test.txt").res);
		assertEquals(12, new Part2().setTarget(66).compute("test.txt").res);
		assertEquals(14, new Part2().setTarget(68).compute("test.txt").res);
		assertEquals(12, new Part2().setTarget(70).compute("test.txt").res);
		assertEquals(22, new Part2().setTarget(72).compute("test.txt").res);
		assertEquals(4, new Part2().setTarget(74).compute("test.txt").res);
		assertEquals(3, new Part2().setTarget(76).compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(1006101, new Part2().setTarget(100).setAtLeast().compute("input.txt").res);
	}

}
