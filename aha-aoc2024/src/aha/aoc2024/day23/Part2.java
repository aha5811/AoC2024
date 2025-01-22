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

		final List<Node> nodes = new LinkedList<>();
		final List<Edge> edges = new LinkedList<>();
		{
			for (final String s : Utils.readLines(this.dir + file)) {
				final String[] ss = s.split("\\-");
				final String na1 = ss[0], na2 = ss[1];
				
				Node n1 = getNode(nodes, na1);
				if (n1 == null) {
					n1 = new Node(na1);
					nodes.add(n1);
				}
				Node n2 = getNode(nodes, na2);
				if (n2 == null) {
					n2 = new Node(na2);
					nodes.add(n2);
				}
				final Edge e = new Edge(n1, n2);
				n1.edges.add(e);
				n2.edges.add(e);
				edges.add(e);
			}
		}

		final List<Node> mc = getMaximalClique(nodes, edges);

		if (mc == null)
			this.res = "";
		else {
			final List<String> names = new LinkedList<>();
			for (final Node n : mc)
				names.add(n.name);
			Collections.sort(names);
			this.res = String.join(",", names);
		}

		return this;
	}

	private static Node getNode(final List<Node> nodes, final String name) {
		for (final Node n : nodes)
			if (n.name.equals(name))
				return n;
		return null;
	}
	
	private static class Node {
		String name;
		List<Edge> edges = new LinkedList<>();
		
		public Node(final String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			final List<String> names = getNodes();
			return names.removeFirst() + " -> " + names;
		}
		
		private List<String> getNodes() {
			final List<String> ret = new LinkedList<>();
			ret.add(this.name);
			for (final Edge e : this.edges)
				ret.add((e.n1 == this ? e.n2 : e.n1).name);
			return ret;
		}
		
		@Override
		public boolean equals(final Object obj) {
			if (obj == this)
				return true;
			if (obj == null || !(obj instanceof Node))
				return false;
			return ((Node) obj).name.equals(this.name);
		}

	}
	
	private static class Edge {
		Node n1;
		Node n2;
		
		public Edge(final Node n1, final Node n2) {
			this.n1 = n1;
			this.n2 = n2;
		}
		
		@Override
		public String toString() {
			return this.n1.name + "<->" + this.n2.name;
		}
	}

	// maximum clique https://en.wikipedia.org/wiki/Clique_problem
	
	// implementation of Bron Kerbosch like
	// https://github.com/liziliao/Bron-Kerbosch/blob/master/Bron-Kerbosch.java

	private List<Node> getMaximalClique(final List<Node> nodes, final List<Edge> edges) {
		Collection<Node> biggest = new LinkedList<>();
		{
			final List<Collection<Node>> allMaxCliques = new LinkedList<>();
			
			final List<Node>
			potClique = new LinkedList<>(),
			cands = new LinkedList<>(nodes),
			founds = new LinkedList<>();
			
			findCliques(allMaxCliques, potClique, cands, founds);

			for (final Collection<Node> clique : allMaxCliques)
				if (clique.size() > biggest.size())
					biggest = clique;
		}
		return new LinkedList<>(biggest);
	}
	
	private void findCliques(final List<Collection<Node>> all,
			final List<Node> potClique, final List<Node> cands, final List<Node> founds)
	{
		if (!end(cands, founds))
			for (final Node c : new LinkedList<>(cands)) {
				final List<Node>
				newCands = new LinkedList<>(),
				newFounds = new LinkedList<>();

				// move candidate to potential clique
				cands.remove(c);
				potClique.add(c);

				// add all that are connected
				for (final Node newC : cands)
					if (hasEdge(c, newC))
						newCands.add(newC);
				
				// ?
				for (final Node newF : founds)
					if (hasEdge(c, newF))
						newFounds.add(newF);
				
				if (newCands.isEmpty() && newFounds.isEmpty())
					all.add(new HashSet<>(potClique));
				else
					findCliques(all, potClique, newCands, newFounds);

				// remove from potential clique to found
				potClique.remove(c);
				founds.add(c);
			}
	}
	
	private boolean end(final List<Node> cands, final List<Node> founds) {
		for (final Node f : founds) {
			int edgeCnt = 0;
			for (final Node c : cands)
				if (hasEdge(c, f))
					edgeCnt++;
			if (edgeCnt == cands.size())
				return true;
		}
		return false;
	}

	private boolean hasEdge(final Node n1, final Node n2) {
		for (final Edge e : n1.edges)
			if (e.n1 == n2 || e.n2 == n2)
				return true;
		return false;
	}
	
	@Override
	public void aTest() {
		assertEquals("co,de,ka,ta", ((Part2) new Part2().compute("test.txt")).res);
	}
	
	@Override
	public void main() {
		assertEquals("da,do,gx,ly,mb,ns,nt,pz,sc,si,tp,ul,vl", ((Part2) new Part2().compute("input.txt")).res);
	}
	
}
