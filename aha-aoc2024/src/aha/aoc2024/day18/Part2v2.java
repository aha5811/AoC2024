package aha.aoc2024.day18;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Utils;
import aha.aoc2024.Utils.Pos;

public class Part2v2 extends Part2 {

	// https://adventofcode.com/2024/day/17#part2

	@Override
	public Part2v2 compute(final String file) {

		final List<Pos> ps = readPs(file);
		
		final Pos s = new Pos(0, 0);
		final Pos e = new Pos(this.w, this.h);
		
		final Collection<Pos> maps = new HashSet<>();

		for (int i = 0; i < this.b; i++)
			maps.add(ps.removeFirst());
		
		// flood fill instead of a* + binary search

		int pMin = 0, pMax = ps.size();
		
		while (true) {
			
			final int pNext = pMin + (pMax - pMin) / 2 + 1;
			if (pNext == pMax)
				break;
			
			final Collection<Pos> mapsTmp = new HashSet<>(maps);
			{
				final List<Pos> psTmp = new LinkedList<>(ps);
				for (int p = 0; p < pNext; p++)
					mapsTmp.add(psTmp.removeFirst());
			}
			
			if (found(mapsTmp, s, e))
				pMin = pNext;
			else
				pMax = pNext;
		}

		final Pos added = ps.get(pMax - 1);
		this.res = added.x + "," + added.y;
		
		return this;
	}

	private boolean found(final Collection<Pos> maps, final Pos s, final Pos e) {
		Collection<Pos> next = new HashSet<>();
		next.add(s);
		final Collection<Pos> all = new HashSet<>();
		while (true) {
			all.addAll(next);
			if (all.contains(e))
				break;
			final Collection<Pos> nnext = new HashSet<>();
			for (final Pos n : next)
				for (final int[] dir : Utils.DIRS90) {
					final Pos nn = new Pos(n.x + dir[0], n.y + dir[1]);
					if (maps.contains(nn))
						continue;
					if (nn.x < 0 || nn.x > this.w || nn.y < 0 || nn.y > this.h)
						continue;
					if (all.contains(nn))
						continue;
					nnext.add(nn);
				}
			if (nnext.isEmpty())
				break;
			next = nnext;
		}
		return all.contains(e);
	}
	
	@Override
	public void aTest() {
		assertEquals("6,1", ((Part2v2) new Part2v2().setW(6).setH(6).setBytes(12)).compute("test.txt").res);
		// made 4 additional searches
	}
	
	@Override
	public void main() {
		assertEquals("44,64", ((Part2v2) new Part2v2().setW(70).setH(70).setBytes(1024)).compute("input.txt").res); // 6s
		// made 11 additional searches
	}
	
}
