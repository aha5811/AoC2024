package aha.aoc2024.day03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {

	@Override
	public Part compute(final String file) {
		final Pattern p = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
		for (final String line : Utils.readLines(this.dir + file)) {
			final Matcher m = p.matcher(line);
			while (m.find())
				doMul(m);
		}
		return this;
	}
	
	protected final long doMul(final Matcher m) {
		return this.res += Long.parseLong(m.group(1)) * Long.parseLong(m.group(2));
	}

	@Override
	public void aTest() {
		assertEquals(161, new Part1().compute("test1.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals(174960292, new Part1().compute("input.txt").res);
	}

}