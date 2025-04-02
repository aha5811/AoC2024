package aha.aoc2024.day22;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part2 extends Part1 {
	
	// https://adventofcode.com/2024/day/22#part2
	
	private int it = -1;

	public Part2 setIt(final int it) {
		this.it = it;
		return this;
	}

	@Override
	public Part compute(final String file) {
		
		final Map<Integer, Map<String, Integer>> b2s2n = new HashMap<>();
		final Collection<String> seqs = new HashSet<>();
		
		final int theIt = this.it == -1 ? IT : this.it;
		
		{
			int n = 0;
			for (final String s : Utils.readLines(this.dir + file)) {
				final List<Integer> changes = new LinkedList<>();
				long secret = Integer.parseInt(s);
				for (int i = 0; i < theIt; i++) {
					final long next = getSecret(secret);
					final int sd = lastDigit(secret);
					final int nd = lastDigit(next);
					final int change = nd - sd;
					changes.add(0, change);

					// System.out.println(next + ": " + nd + " (" + change + ")");

					if (changes.size() >= 4) {
						final String seq = changes.get(3) + "," + changes.get(2) + "," + changes.get(1) + ","
								+ changes.get(0);
						seqs.add(seq);
						if (!b2s2n.containsKey(n))
							b2s2n.put(n, new HashMap<>());
						if (!b2s2n.get(n).containsKey(seq))
							b2s2n.get(n).put(seq, nd);
					}
					secret = next;
				}
				n++;
			}
		}
		
		long bananas = 0;
		String winningSeq = null;
		for (final String seq : seqs) {
			long sbs = 0;
			for (final Integer n : b2s2n.keySet())
				if (b2s2n.get(n).containsKey(seq))
					sbs += b2s2n.get(n).get(seq);
			if (sbs > bananas) {
				bananas = sbs;
				winningSeq = seq;
			}
		}
		
		// System.out.println(winningSeq + " -> " + bananas);
		this.res = bananas;

		return this;
	}
	
	private int lastDigit(final long secret) {
		return (int) (secret - secret / 10 * 10);
	}
	
	@Override
	public void aTest() {
		assertEquals(6, new Part2().setIt(10).compute("test3.txt").res);
		assertEquals(23, new Part2().compute("test2.txt").res);
	}

	@Override
	public void main() {
		assertEquals(2044, new Part2().compute("input.txt").res); // 6.7s
	}

}
