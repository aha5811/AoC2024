package aha.aoc2024.day18;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Pos;

public class Part2 extends Part1 {
	
	// https://adventofcode.com/2024/day/17#part2
	
	String res;
	
	@Override
	public Part2 compute(final String file) {
		
		final List<Pos> ps = readPs(file);

		final Pos s = new Pos(0, 0);
		final Pos e = new Pos(this.w, this.h);

		final CharMap cm = createCM(ps, this.b);
		
		for (int i = 0; i < this.b; i++)
			ps.removeFirst();

		List<Pos> trace = doSearch(cm, s, e).trace;

		Pos added;
		while (!ps.isEmpty()) {
			while (true) {
				added = ps.removeFirst();
				cm.setChar(added.x, added.y, '#');
				if (trace.contains(added))
					break;
			}
			final Result r = doSearch(cm, s, e);
			if (r == null) {
				this.res = added.x + "," + added.y;
				break;
			} else {
				trace = r.trace;
				// out(cm, trace);
			}
		}

		return this;
	}
	
	private void out(final CharMap cm, final List<Pos> trace) {
		System.out.println("");
		for (final Pos p : trace)
			cm.setChar(p.x, p.y, 'O');
		System.out.println(cm);
		for (final Pos p : trace)
			cm.setChar(p.x, p.y, '.');
		System.out.println("");
	}

	@Override
	public void aTest() {
		assertEquals("6,1", ((Part2) new Part2().setW(6).setH(6).setBytes(12).compute("test.txt")).res);
		// made 2 additional searches
	}

	@Override
	public void main() {
		assertEquals("44,64", ((Part2) new Part2().setW(70).setH(70).setBytes(1024).compute("input.txt")).res); // 6s
		// made 34 additional searches
	}

}
