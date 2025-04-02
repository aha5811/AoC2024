package aha.aoc2024.day25;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {
	
	// https://adventofcode.com/2024/day/_

	@Override
	public Part compute(final String file) {

		final List<int[]> locks = new LinkedList<>();
		final List<int[]> keys = new LinkedList<>();

		{
			final List<List<String>> blocks = new LinkedList<>();
			{
				List<String> block = new LinkedList<>();
				for (final String line : Utils.readLines(this.dir + file))
					if (line.isBlank() && !block.isEmpty()) {
						blocks.add(block);
						block = new LinkedList<>();
					} else
						block.add(line);
				if (!block.isEmpty())
					blocks.add(block);
			}

			for (final List<String> block : blocks) {
				final boolean isLock = block.getFirst().replace("#", "").isEmpty();
				if (isLock)
					block.removeFirst();
				else
					block.removeLast();
				final int[] heights = new int[block.getFirst().length()];
				for (final String line : block) {
					int i = 0;
					for (final char c : line.toCharArray())
						heights[i++] += c == '.' ? 0 : 1;
				}
				(isLock ? locks : keys).add(heights);
			}
		}

		for (final int[] lock : locks)
			for (final int[] key : keys)
				if (fits(lock, key))
					this.res++;

		return this;
	}

	private boolean fits(final int[] lock, final int[] key) {
		for (int i = 0; i < lock.length; i++)
			if (lock[i] + key[i] > 5)
				return false;
		return true;
	}
	
	@Override
	public void aTest() {
		assertEquals(3, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(2815, new Part1().compute("input.txt").res);
	}

}
