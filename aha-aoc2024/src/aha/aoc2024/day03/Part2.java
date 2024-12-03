package aha.aoc2024.day03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part2 extends Part1 {
	
	@Override
	public Part compute(final String file) {
		final Pattern p = Pattern.compile("mul\\((\\d+),(\\d+)\\)|do(?:n't)?\\(\\)");
		boolean ignoreMul = false;
		for (final String line : Utils.readLines(this.dir + file)) {
			final Matcher m = p.matcher(line);
			while (m.find()) {
				final String op = m.group(0);
				if (op.startsWith("do"))
					ignoreMul = "don't()".equals(op);
				else if (!ignoreMul)
					doMul(m);
			}
		}
		return this;
	}

	@Override
	public void aTest() {
		assertEquals(48, new Part2().compute("test2.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(56275602, new Part2().compute("input.txt").res);
	}
	
}
