package aha.aoc2024.day03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {
	
	protected final static String MUL_OP = "mul\\((\\d+),(\\d+)\\)";
	private final static Pattern P = Pattern.compile(MUL_OP);

	@Override
	public Part compute(final String file) {
		for (final String line : Utils.readLines(this.dir + file)) {
			final Matcher m = P.matcher(line);
			while (m.find())
				doMul(m);
		}
		return this;
	}

	protected final void doMul(final Matcher m) {
		this.res += Long.parseLong(m.group(1)) * Long.parseLong(m.group(2));
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
