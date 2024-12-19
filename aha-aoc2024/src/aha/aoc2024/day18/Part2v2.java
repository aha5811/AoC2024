package aha.aoc2024.day18;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import aha.aoc2024.Utils;
import aha.aoc2024.Utils.Pos;

public class Part2v2 extends Part2 {
	
	// https://adventofcode.com/2024/day/17#part2
	
	@Override
	public Part2v2 compute(final String file) {
		
		final Pos s = new Pos(0, 0);
		final Pos e = new Pos(this.w, this.h);

		final Map<Pos, Integer> p2i = new HashMap<>();
		{
			final List<Pos> ps = readPs(file);
			int i = 0;
			for (final Pos p : ps)
				p2i.put(p, i++);
		}

		// flood fill instead of a* + binary search
		
		int iMin = this.b, iMax = p2i.size();

		while (true) {

			final int iNext = iMin + (iMax - iMin) / 2 + 1;
			if (iNext == iMax)
				break;

			if (found(p2i, iNext, s, e))
				iMin = iNext;
			else
				iMax = iNext;
		}
		
		Pos added = null;
		for (final Pos p : p2i.keySet())
			if (p2i.get(p) == iMax - 1) {
				added = p;
				break;
			}
		this.res = added.x + "," + added.y;

		return this;
	}
	
	private boolean found(final Map<Pos, Integer> p2i, final int i, final Pos s, final Pos e) {
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
					if (p2i.containsKey(nn) && p2i.get(nn) <= i)
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
		assertEquals("44,64", ((Part2v2) new Part2v2().setW(70).setH(70).setBytes(1024)).compute("input.txt").res);
		// made 11 additional searches
	}

}
