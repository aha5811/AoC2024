package aha.aoc2024.day23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part2 extends Part1 {

	// https://adventofcode.com/2024/day/23#part2

	private String res;

	@Override
	public Part compute(final String file) {

		final List<EGroup> egs = new LinkedList<>();
		
		for (final String s : Utils.readLines(this.dir + file)) {
			final String[] ss = s.split("\\-");
			final Edge e = new Edge(ss[0], ss[1]);
			final List<EGroup> add = new LinkedList<>();
			for (final EGroup eg : egs)
				if (eg.accept(e))
					add.add(eg.add(e));
			if (!add.isEmpty())
				egs.addAll(add);
			egs.add(new EGroup().add(e));
		}
		
		EGroup biggest = null;
		
		for (final EGroup eg : egs)
			if (eg.isClosed() && (biggest == null || eg.edges.size() > biggest.edges.size()))
				biggest = eg;
		
		if (biggest == null)
			this.res = "";
		else {
			final Collection<String> nodes = new HashSet<>();
			for (final Edge e : biggest.edges) {
				nodes.add(e.n1);
				nodes.add(e.n2);
			}
			final List<String> tmp = new LinkedList<>(nodes);
			Collections.sort(tmp);
			this.res = String.join(",", tmp);
		}

		return this;
	}

	private static class EGroup {
		List<Edge> edges = new LinkedList<>();
		Collection<String> nodes = new HashSet<>();

		boolean accept(final Edge e) {
			return this.nodes.contains(e.n1) || this.nodes.contains(e.n2);
		}

		public boolean isClosed() {
			for (final String n1 : this.nodes)
				for (final String n2 : this.nodes)
					if (n1 != n2 && !isConnected(n1, n2))
						return false;
			return true;
		}

		private boolean isConnected(final String n1, final String n2) {
			for (final Edge e : this.edges)
				if (e.n1.equals(n1) && e.n2.equals(n2) || e.n1.equals(n2) && e.n2.equals(n1))
					return true;
			return false;
		}

		EGroup add(final Edge e) {
			final EGroup ret = new EGroup();
			ret.edges = new LinkedList<>(this.edges);
			ret.nodes = new HashSet<>(this.nodes);
			ret.edges.add(e);
			ret.nodes.add(e.n1);
			ret.nodes.add(e.n2);
			return ret;
		}
	}
	
	@Override
	public void aTest() {
		assertEquals("co,de,ka,ta", ((Part2) new Part2().compute("test.txt")).res);
	}
	
	@Override
	public void main() {
		assertEquals("", ((Part2) new Part2().compute("input.txt")).res);
	}
	
}
