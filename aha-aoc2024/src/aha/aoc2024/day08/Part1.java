package aha.aoc2024.day08;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Pos;
import aha.aoc2024.Utils.Symbol;

public class Part1 extends Part {

	// https://adventofcode.com/2024/day/8
	
	@Override
	public Part compute(final String file) {
		final CharMap cm = new CharMap(this.dir + file);
		
		final List<Character> chars = cm.getChars();
		chars.remove((Character) '.');

		final Collection<Pos> antinodes = new HashSet<>();

		for (final char c : chars)
			antinodes.addAll(getAntinodes(cm, c));

		this.res = antinodes.size();
		
		/*
		for (final Pos p : antinodes)
			cm.setChar(p.x, p.y, '#');
		System.out.println(cm);
		//*/

		return this;
	}
	
	private List<Pos> getAntinodes(final CharMap cm, final char c) {
		final List<Pos> ret = new LinkedList<>();

		final Symbol[] as = cm.getAll(c).toArray(new Symbol[] {});

		for (int a1 = 0; a1 < as.length; a1++)
			for (int a2 = 0; a2 < as.length; a2++)
				if (a1 != a2)
					ret.addAll(getAntinodes(cm, as[a1], as[a2]));
		return ret;
	}

	protected List<Pos> getAntinodes(final CharMap cm, final Symbol a1, final Symbol a2) {
		final List<Pos> ret = new LinkedList<>();

		final Pos p = getP(a1, a2, 1);
		if (!cm.isOutside(p.x, p.y))
			ret.add(p);

		return ret;
	}

	protected final Pos getP(final Symbol a1, final Symbol a2, final int dist) {
		return new Pos(a2.x + dist * (a2.x - a1.x), a2.y + dist * (a2.y - a1.y));
	}

	@Override
	public void aTest() {
		assertEquals(14, new Part1().compute("test.txt").res);
	}

	@Override
	public void main() {
		assertEquals(259, new Part1().compute("input.txt").res);
	}
	
}
