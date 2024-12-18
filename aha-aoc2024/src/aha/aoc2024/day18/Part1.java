package aha.aoc2024.day18;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Pos;

public class Part1 extends Part {

	// https://adventofcode.com/2024/day/17
	
	int w = 0, h = 0, b = 0;

	protected Part1 setW(final int w) {
		this.w = w;
		return this;
	}
	
	protected Part1 setH(final int h) {
		this.h = h;
		return this;
	}
	
	protected Part1 setBytes(final int b) {
		this.b = b;
		return this;
	}

	@Override
	public Part compute(final String file) {

		final List<Pos> ps = readPs(file);
		
		final Pos s = new Pos(0, 0);
		final Pos e = new Pos(this.w, this.h);

		final CharMap cm = createCM(ps, this.b);
		
		final Result r = doSearch(cm, s, e);

		this.res = r.trace.size();
		
		return this;
	}

	protected List<Pos> readPs(final String file) {
		final List<Pos> ps = new LinkedList<>();
		for (final String line : Utils.readLines(this.dir + file)) {
			final List<Integer> is = Utils.toIs(line.replace(",", " "));
			ps.add(new Pos(is.remove(0), is.get(0)));
		}
		return ps;
	}
	
	protected CharMap createCM(final List<Pos> ps, final int blocks) {
		final CharMap cm = new CharMap(this.w + 1, this.h + 1, '.');
		{
			int i = 0;
			for (final Pos p : ps)
				if (i++ == blocks)
					break;
				else
					cm.setChar(p.x, p.y, '#');
		}
		return cm;
	}

	static class Result {
		List<Pos> trace;
		int cost;
	}

	private int h(final Pos p, final Pos e) {
		return Math.abs(p.x - e.x) + Math.abs(p.y - e.y);
	}
	
	protected Result doSearch(final CharMap cm, final Pos start, final Pos end) {
		Result aRes = null;

		final List<Pos> open = new LinkedList<>();
		final List<Pos> closed = new LinkedList<>();
		final Map<Pos, Pos> from = new HashMap<>();
		final Map<Pos, Integer> g = new HashMap<>(), f = new HashMap<>();
		
		{
			open.add(start);
			g.put(start, 0);
			f.put(start, h(start, end));
		}

		while (!open.isEmpty()) {

			Collections.sort(open, new Comparator<Pos>() {
				@Override
				public int compare(final Pos p1, final Pos p2) {
					return Integer.compare(f.get(p1), f.get(p2));
				}
			});
			
			final Pos c = open.remove(0);
			closed.add(c);
			
			if (h(c, end) == 0) {
				aRes = new Result();
				aRes.cost = g.get(c);
				aRes.trace = new LinkedList<>();
				{
					Pos s = c;
					while ((s = from.get(s)) != null)
						aRes.trace.add(0, s);
				}
				break;
			}
			
			for (final int[] dir : Utils.DIRS90) {
				final Pos n = new Pos(c.x + dir[0], c.y + dir[1]);
				if (cm.isOutside(n.x, n.y))
					continue;
				if (cm.getChar(n.x, n.y) == '#')
					continue;
				if (closed.contains(n))
					continue;
				
				final int gnext = g.get(c) + 1;

				if (!g.containsKey(n) || gnext < g.get(n)) {
					from.put(n, c);
					g.put(n, gnext);
					f.put(n, gnext + h(n, end));
					if (!open.contains(n))
						open.add(n);
				}
			}
			
		}
		
		return aRes;
	}
	
	@Override
	public void aTest() {
		assertEquals(22, new Part1().setW(6).setH(6).setBytes(12).compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(232, new Part1().setW(70).setH(70).setBytes(1024).compute("input.txt").res);
	}
	
}
