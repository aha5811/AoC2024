package aha.aoc2024.day20;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Pos;
import aha.aoc2024.Utils.Symbol;

public class Part1 extends Part {

	// https://adventofcode.com/2024/day/_
	
	private int target = 0;
	private boolean atLeast = false;
	
	private Part1 setTarget(final int t) {
		this.target = t;
		return this;
	}

	private Part1 setAtLeast() {
		this.atLeast = true;
		return this;
	}

	@Override
	public Part compute(final String file) {
		
		final Map<Pos, Integer> p2cnt = new HashMap<>();
		List<Pos> ps;
		Pos e;
		{
			final CharMap cm = new CharMap(this.dir + file);
			
			Pos s;
			{
				final Symbol ss = cm.getAll('S').get(0), es = cm.getAll('E').get(0);
				s = new Pos(ss.x, ss.y);
				e = new Pos(es.x, es.y);
			}
			
			ps = aha.aoc2024.day18.Part1.doSearch(cm, s, e).trace;
			ps.add(e);

			int i = 0;
			for (final Pos p : ps)
				p2cnt.put(p, i++);
		}
		
		for (final Pos p : ps) {
			final int step = p2cnt.get(p);
			for (final int[] dir : Utils.DIRS90) {
				final Pos m2 = new Pos(p.x + 2 * dir[0], p.y + 2 * dir[1]);
				if (!p2cnt.containsKey(m2))
					continue;
				final int tstep = p2cnt.get(m2);
				if (tstep < step)
					continue;
				final Pos m1 = new Pos(p.x + dir[0], p.y + dir[1]);
				if (p2cnt.containsKey(m1))
					continue;
				final int save = tstep - step - 2;
				if (save == this.target || this.atLeast && save > this.target)
					// System.out.println(p + "->" + m2);
					this.res++;
			}
		}
		
		return this;
	}
	
	@Override
	public void aTest() {
		assertEquals(14, new Part1().setTarget(2).compute("test.txt").res);
		assertEquals(14, new Part1().setTarget(4).compute("test.txt").res);
		assertEquals(2, new Part1().setTarget(6).compute("test.txt").res);
		assertEquals(4, new Part1().setTarget(8).compute("test.txt").res);
		assertEquals(2, new Part1().setTarget(10).compute("test.txt").res);
		assertEquals(3, new Part1().setTarget(12).compute("test.txt").res);
		assertEquals(1, new Part1().setTarget(20).compute("test.txt").res);
		assertEquals(1, new Part1().setTarget(36).compute("test.txt").res);
		assertEquals(1, new Part1().setTarget(38).compute("test.txt").res);
		assertEquals(1, new Part1().setTarget(40).compute("test.txt").res);
		assertEquals(1, new Part1().setTarget(64).compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(1406, new Part1().setTarget(100).setAtLeast().compute("input.txt").res);
	}
	
}
