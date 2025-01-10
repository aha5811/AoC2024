package aha.aoc2024.day24;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {
	
	// https://adventofcode.com/2024/day/24

	@Override
	public Part compute(final String file) {
		final List<Node> nodes = new LinkedList<>();
		final Map<String, Node> n2n = new HashMap<>();
		
		for (String line : Utils.readLines(this.dir + file))
			if (line.contains(":")) {
				// x00: 1
				final String[] s = line.split(":");
				final Node n = new Node(s[0]);
				n.val = 1 == Integer.parseInt(s[1].trim());
				nodes.add(n);
			} else if (line.contains("->")) {
				// x00 AND y00 -> z00
				line = line.replace("-> ", "");
				final String[] s = line.split("\\s");
				nodes.add(new Node2(s[3], s[0], s[2], s[1]));
			}
		for (final Node n : nodes)
			n2n.put(n.name, n);
		
		{
			String bits = "";
			int n = 0;
			while (true) {
				final String nn = "z" + (n < 10 ? "0" : "") + n;
				if (!n2n.containsKey(nn))
					break;
				bits = (getValue(n2n, nn) ? 1 : 0) + bits;
				n++;
			}
			this.res = Long.parseLong(bits, 2);
		}

		return this;
	}
	
	private boolean getValue(final Map<String, Node> n2n, final String nn) {
		final Node n = n2n.get(nn);
		if (n.val != null)
			return n.val;
		else {
			final Node2 n2 = (Node2) n;
			final boolean b1 = getValue(n2n, n2.parent1);
			final boolean b2 = getValue(n2n, n2.parent2);
			boolean ret = false;
			if (n2.type.equals("AND"))
				ret = b1 && b2;
			else if (n2.type.equals("OR"))
				ret = b1 || b2;
			else if (n2.type.equals("XOR"))
				ret = b1 ^ b2;
			n2.val = ret;
			return ret;
		}
	}
	
	private static class Node {
		String name;
		Boolean val;

		public Node(final String name) {
			this.name = name;
		}
	}
	
	private static class Node2 extends Node {
		String parent1;
		String parent2;
		String type;

		public Node2(final String name, final String parent1, final String parent2, final String type) {
			super(name);
			this.parent1 = parent1;
			this.parent2 = parent2;
			this.type = type;
		}
	}

	@Override
	public void aTest() {
		assertEquals(4, new Part1().compute("test1.txt").res);
		assertEquals(2024, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(56278503604006l, new Part1().compute("input.txt").res);
	}

}
