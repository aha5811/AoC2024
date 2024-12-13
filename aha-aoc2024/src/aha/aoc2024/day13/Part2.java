package aha.aoc2024.day13;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Part2 extends Part1 {
	
	// https://adventofcode.com/2024/day/13#part2
	
	final static long OFFSET = 10000000000000l;
	
	private Part2 setOffset(final long o) {
		this.offset = o;
		return this;
	}

	@Override
	protected long computeFor(final Machine m) {
		
		// a = pushesA, b = pushesB
		// ax = a.xi, bx = b.xi, px = m.px
		// ay = a.yi, by = b.yi, py = m.py

		// 1: ax * a + bx * b = px
		// 2: ay * a + by * b = py

		// 1': a = (px - b * bx) / ax
		// 2': a = (py - b * by) / ay
		
		// 1'' = 2''
		// (px - b * bx) / ax = (py - b * by) / ay | * ax * ay
		// (px - b * bx) * ay = (py - b * by) * ax
		// px * ay - b * bx * ay = py * ax - b * by * ax
		// b * (by * ax - bx * ay) = py * ax - px * ay
		// b = (py * ax - px * ay) / (by * ax - bx * ay)

		final long pushesB = (m.py * m.a.xi - m.px * m.a.yi) / (m.b.yi * m.a.xi - m.b.xi * m.a.yi);
		
		// result in 1'
		
		final long pushesA = (m.px - pushesB * m.b.xi) / m.a.xi;
		
		final boolean solved = m.getX(pushesA, pushesB) == m.px && m.getY(pushesA, pushesB) == m.py;
		
		return solved ? m.getCost(pushesA, pushesB) : 0;
	}

	@Override
	public void aTest() {
		assertEquals(280, new Part2().compute("test1.txt").res);
		assertEquals(0, new Part2().compute("test2.txt").res);
		assertEquals(200, new Part2().compute("test3.txt").res);
		assertEquals(0, new Part2().compute("test4.txt").res);

		assertEquals(0, new Part2().setOffset(OFFSET).compute("test1.txt").res);
		assertNotEquals(0, new Part2().setOffset(OFFSET).compute("test2.txt").res);
		assertEquals(0, new Part2().setOffset(OFFSET).compute("test3.txt").res);
		assertNotEquals(0, new Part2().setOffset(OFFSET).compute("test4.txt").res);
	}

	@Override
	public void main() {
		assertEquals(108713182988244l, new Part2().setOffset(OFFSET).compute("input.txt").res);
	}

}
