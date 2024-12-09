package aha.aoc2024.day08;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;
import aha.aoc2024.Utils.CharMap;
import aha.aoc2024.Utils.Pos;
import aha.aoc2024.Utils.Symbol;

public class Part1 extends Part {

	@Override
	public Part compute(final String file) {
		final CharMap cm = new CharMap(Utils.readLines(this.dir + file));
		
		final List<Character> chars = cm.getChars();
		chars.remove((Character) '.');

		final Collection<Pos> antinodes = new HashSet<>();

		for (final char c : chars) {
			final List<Symbol> antennas = cm.getAll(c);
			for (final Pos p : getAntinodes(antennas))
				if (!cm.isOutside(p.x, p.y))
					antinodes.add(p);
		}

		this.res = antinodes.size();

		return this;
	}
	
	private List<Pos> getAntinodes(final List<Symbol> antennas) {
		final List<Pos> ret = new LinkedList<>();
		final Symbol[] as = antennas.toArray(new Symbol[] {});
		for (int a1 = 0; a1 < as.length; a1++)
			for (int a2 = 0; a2 < as.length; a2++)
				if (a1 != a2)
					ret.add(getAntinode(as[a1], as[a2]));
		return ret;
	}

	private Pos getAntinode(final Symbol a1, final Symbol a2) {
		return new Pos(a2.x + a2.x - a1.x, a2.y + a2.y - a1.y);
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
