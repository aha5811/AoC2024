package aha.aoc2024.day09;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {
	
	@Override
	public Part compute(final String file) {
		final String line = Utils.readLines(this.dir + file).get(0);

		final int[] fs = read(line);
		int end = fs.length - 1;
		
		end = compact(fs, end);

		for (int p = 0; p <= end; p++) {
			final int v = fs[p];
			if (v == -1)
				continue;
			this.res += p * v;
		}
		
		return this;
	}
	
	protected int compact(final int[] fs, int end) {
		int spacePos = 0;
		while (fs[spacePos] != -1)
			spacePos++;

		while (true) {
			while (fs[end] == -1)
				end--;

			final int i = fs[end];
			end--;

			fs[spacePos] = i;

			while (spacePos < end && fs[spacePos] != -1)
				spacePos++;
			if (spacePos == end)
				break;
		}
		return end;
	}
	
	private int[] read(final String line) {
		final List<Integer> fs = new LinkedList<>();

		int id = 0;
		boolean isFile = true;
		for (final char c : line.toCharArray()) {
			final int i = Integer.parseInt("" + c);
			if (isFile) {
				add(fs, i, id);
				id++;
			} else
				add(fs, i, -1);
			isFile = !isFile;
		}
		
		final int[] fss = new int[fs.size()];
		
		int i = 0;
		while (!fs.isEmpty())
			fss[i++] = fs.remove(0);
		return fss;
	}

	private void add(final List<Integer> fs, final int length, final int id) {
		for (int i = 0; i < length; i++)
			fs.add(id);
	}

	@Override
	public void aTest() {
		assertEquals(1928, new Part1().compute("test.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(6283170117911l, new Part1().compute("input.txt").res);
	}

}