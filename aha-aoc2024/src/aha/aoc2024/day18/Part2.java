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
		
		int bcnt = this.b;

		Pos added;
		while (!ps.isEmpty()) {
			added = ps.removeFirst();
			cm.setChar(added.x, added.y, '#');
			final Result r = doSearch(cm, s, e);
			if (r == null) {
				this.res = added.x + "," + added.y;
				break;
			}
			bcnt++;
			System.out.println(bcnt);
		}

		return this;
	}

	@Override
	public void aTest() {
		assertEquals("6,1", ((Part2) new Part2().setW(6).setH(6).setBytes(12).compute("test.txt")).res);
	}
	
	@Override
	public void main() {
		assertEquals("", ((Part2) new Part2().setW(70).setH(70).setBytes(1024).compute("input.txt")).res);
	}
	
}
