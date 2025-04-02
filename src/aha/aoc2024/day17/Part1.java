package aha.aoc2024.day17;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import aha.aoc2024.Part;
import aha.aoc2024.Utils;

public class Part1 extends Part {
	
	// https://adventofcode.com/2024/day/17
	
	public String res;
	
	public static BigInteger EIGHT = BigInteger.valueOf(8);

	@Override
	public Part1 compute(final String file) {
		final Machine m = read(file);
		
		m.run();

		this.res = o2s(m);
		
		return this;
	}

	protected String o2s(final Machine m) {
		String s = "";
		for (final BigInteger i : m.out)
			s += "," + i;
		if (!s.isEmpty())
			s = s.substring(1);
		return s;
	}

	static class Machine {
		BigInteger a;
		BigInteger b;
		BigInteger c;

		int p = 0;

		int[] ops;

		List<BigInteger> out = new LinkedList<>();
		
		public Machine(final int a, final int b, final int c, final int[] ops) {
			this.a = BigInteger.valueOf(a);
			this.b = BigInteger.valueOf(b);
			this.c = BigInteger.valueOf(b);
			this.ops = ops;
		}

		@Override
		public String toString() {
			String ret = "A: " + this.a + "\nB: " + this.b + "\nC: " + this.c + "\n";
			for (int i = 0; i < this.ops.length; i++)
				ret += (this.p == i ? ">" : " ") + this.ops[i];
			return ret;
		}

		void run() {
			// System.out.println(this);
			while (true) {
				next();
				// System.out.println(this);
				if (this.p >= this.ops.length)
					break;
			}
		}
		
		void next() {
			final int op = this.ops[this.p];
			final int operand = this.ops[this.p + 1];
			
			if (op == 0)
				adv(operand);
			else if (op == 1)
				bxl(operand);
			else if (op == 2)
				bst(operand);
			else if (op == 3)
				jnz(operand);
			else if (op == 4)
				bxc(operand);
			else if (op == 5)
				out(operand);
			else if (op == 6)
				bdv(operand);
			else if (op == 7)
				cdv(operand);

			if (op != 3) // jnz
				this.p += 2;
		}
		
		BigInteger combo(final int operand) {
			if (operand >= 0 && operand <= 3)
				return BigInteger.valueOf(operand);
			if (operand == 4)
				return this.a;
			if (operand == 5)
				return this.b;
			if (operand == 6)
				return this.c;
			throw new RuntimeException("illegal combo operand " + operand);
		}
		
		void adv(final int operand) {
			// The adv instruction (opcode 0) performs division. The numerator is the value
			// in the A register. The denominator is found by raising 2 to the power of the
			// instruction's combo operand. (So, an operand of 2 would divide A by 4 (2^2);
			// an operand of 5 would divide A by 2^B.) The result of the division operation
			// is truncated to an integer and then written to the A register.
			this.a = dv(operand);
		}
		
		void bxl(final int operand) {
			// The bxl instruction (opcode 1) calculates the bitwise XOR of register B and
			// the instruction's literal operand, then stores the result in register B.
			this.b = this.b.xor(BigInteger.valueOf(operand));
		}
		
		void bst(final int operand) {
			// The bst instruction (opcode 2) calculates the value of its combo operand
			// modulo 8 (thereby keeping only its lowest 3 bits), then writes that value to
			// the B register.
			this.b = combo(operand).mod(EIGHT);
		}
		
		void jnz(final int operand) {
			// The jnz instruction (opcode 3) does nothing if the A register is 0. However,
			// if the A register is not zero, it jumps by setting the instruction pointer to
			// the value of its literal operand; if this instruction jumps, the instruction
			// pointer is not increased by 2 after this instruction.
			if (this.a == BigInteger.ZERO)
				this.p += 2;
			else
				this.p = operand;
		}
		
		void bxc(final int operand) {
			// The bxc instruction (opcode 4) calculates the bitwise XOR of register B and
			// register C, then stores the result in register B. (For legacy reasons, this
			// instruction reads an operand but ignores it.)
			this.b = this.b.xor(this.c);
		}
		
		void out(final int operand) {
			// The out instruction (opcode 5) calculates the value of its combo operand
			// modulo 8, then outputs that value. (If a program outputs multiple values,
			// they are separated by commas.)
			this.out.add(combo(operand).mod(EIGHT));
		}
		
		void bdv(final int operand) {
			// The bdv instruction (opcode 6) works exactly like the adv instruction except
			// that the result is stored in the B register. (The numerator is still read
			// from the A register.)
			this.b = dv(operand);
		}
		
		void cdv(final int operand) {
			// The cdv instruction (opcode 7) works exactly like the adv instruction except
			// that the result is stored in the C register. (The numerator is still read
			// from the A register.)
			this.c = dv(operand);
		}

		private BigInteger dv(final int operand) {
			return this.a.divide(BigInteger.TWO.pow(combo(operand).intValue()));
		}
	}
	
	protected Machine read(final String file) {
		Machine m = null;
		{
			int a = 0, b = 0, c = 0;
			for (final String line : Utils.readLines(this.dir + file))
				if (line.contains("A:"))
					a = n(line);
				else if (line.contains("B:"))
					b = n(line);
				else if (line.contains("C:"))
					c = n(line);
				else if (line.contains("Program:")) {
					final List<Integer> is = Utils.toIs(line.substring(line.indexOf(":") + 1).trim().replace(",", " "));
					final int[] ops = new int[is.size()];
					for (int i = 0; i < ops.length; i++)
						ops[i] = is.remove(0);
					m = new Machine(a, b, c, ops);
				}
		}
		return m;
	}

	private int n(final String s) {
		return Integer.parseInt(s.replaceAll("\\D", "").trim());
	}

	@Override
	public void aTest() {
		assertEquals("4,6,3,5,6,3,5,2,1,0", new Part1().compute("test1.txt").res);
	}
	
	@Override
	public void main() {
		assertEquals("7,5,4,3,4,5,3,4,6", new Part1().compute("input.txt").res);
	}

}
