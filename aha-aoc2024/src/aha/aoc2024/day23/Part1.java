package aha.aoc2024.day23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {
	
	// https://adventofcode.com/2024/day/23

	@Override
	public Part compute(final String file) {

		final List<EGroup> egs = new LinkedList<>();

		for (final String s : Utils.readLines(this.dir + file)) {
			final String[] ss = s.split("\\-");
			final Edge e = new Edge(ss[0], ss[1]);
			final List<EGroup> add = new LinkedList<>();
			for (final EGroup eg : egs)
				if (eg.edges.size() < 3 && eg.accept(e)) {
					final EGroup egNew = eg.copy();
					egNew.add(e);
					add.add(egNew);
				}
			if (!add.isEmpty())
				egs.addAll(add);
			egs.add(new EGroup(e));
		}

		for (final EGroup eg : egs)
			if (eg.edges.size() == 3 && eg.open.isEmpty())
				for (final Edge e : eg.edges)
					if (e.n1.startsWith("t") || e.n2.startsWith("t")) {
						this.res++;
						break;
					}
		
		return this;
	}

	private static class EGroup {
		List<Edge> edges = new LinkedList<>();
		List<String> open = new LinkedList<>();

		boolean accept(final Edge e) {
			return this.open.contains(e.n1) || this.open.contains(e.n2);
		}

		void add(final Edge e) {
			this.edges.add(e);
			if (this.open.contains(e.n1)) {
				this.open.remove(e.n1);
				this.open.add(e.n2);
			} else if (this.open.contains(e.n2)) {
				this.open.remove(e.n2);
				this.open.add(e.n1);
			}
			if (this.open.getFirst().equals(this.open.getLast()))
				this.open.clear();
		}

		EGroup(final Edge e) {
			this.edges.add(e);
			this.open.add(e.n1);
			this.open.add(e.n2);
		}

		private EGroup() {
		}

		EGroup copy() {
			final EGroup e = new EGroup();
			e.edges = new LinkedList<>(this.edges);
			e.open = new LinkedList<>(this.open);
			return e;
		}
		
		@Override
		public String toString() {
			return this.edges.toString() + " / " + this.open;
		}
	}

	protected static class Edge {
		String n1;
		String n2;

		public Edge(final String n1, final String n2) {
			this.n1 = n1;
			this.n2 = n2;
		}
		
		@Override
		public String toString() {
			return this.n1 + "<->" + this.n2;
		}
	}

	@Override
	public void aTest() {
		assertEquals(7, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(0, new Part1().compute("input.txt").res);
	}

}
