package faceless.artent.registries;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import faceless.artent.transmutations.api.Transmutation;
import faceless.artent.transmutations.actions.*;

public class TransmutationRegistry implements IRegistry {
	public static final Hashtable<String, Transmutation> registry = new Hashtable<>();

	/**
	 * Creates all binary strings from "nnnnn" to "rrrrr"
	 *
	 * @param d        covered size
	 * @param old      already built string
	 * @param size     binary string size
	 * @param variants list of all created variants
	 */
	public static void getVariants(int d, String old, int size, List<String> variants) {
		for (int i = 0; i < 2; i++) {
			if (d == size - 1) {
				variants.add(old + (i == 0 ? "n" : "r"));
			}
			if (d < size - 1) {
				getVariants(d + 1, old + (i == 0 ? "n" : "r"), size, variants);
			}
		}
	}

	@Override
	public void register() {
		registerForReversed("C1n", new Transmutation("circle.empty", (facing, e, p) -> {
			if (p != null)
				p.damage(p.getDamageSources().magic(), 1f);
		}), "C1");

		registerForReversed("C1nD1rT1n", new ActionClearGrass(2), "C1");
		registerForReversed("C2nD2rT2n", new ActionClearGrass(1), "C2");
		registerForReversed("C3nD3rT3n", new ActionClearGrass(0), "C3");

		registerForReversed("C1nS1nS1r", new ActionCollectItems(2), "C1");
		registerForReversed("C2nS2nS2r", new ActionCollectItems(1), "C2");
		registerForReversed("C3nS3nS3r", new ActionCollectItems(0), "C3");

		registerForReversed("C1nT1nS1n", new ActionBurningAir(), "C1");

		registerForReversed("C1nD1rS1r", new ActionSeed(2), "C1");
		registerForReversed("C2nD2rS2r", new ActionSeed(1), "C2");
		registerForReversed("C3nD3rS3r", new ActionSeed(0), "C3");

		registerForReversed("C1nD1rT1nS1r", new ActionHarvest(2), "C1");
		registerForReversed("C2nD2rT2nS2r", new ActionHarvest(1), "C2");
		registerForReversed("C3nD3rT3nS3r", new ActionHarvest(0), "C3");

		registerForReversed("C1nL1nD1r", new ActionFertilize(2), "C1");
		registerForReversed("C2nL2nD2r", new ActionFertilize(1), "C2");
		registerForReversed("C3nL3nD3r", new ActionFertilize(0), "C3");

		registerForReversed("C1nR1rR2n", new ActionPole(2), "C1");
		registerForReversed("C2nR2rR3n", new ActionPole(1), "C2");
	}

	/**
	 * Registers transmutation as-is for only formula
	 *
	 * @param formula - string of circle part tokens
	 * @param action  action to run on circle activated
	 */
	public Transmutation register(String formula, Transmutation action) {
		registry.put(formula, action);
		System.out.println("Circle formula " + formula + " has been registered");
		return action;
	}

	/**
	 * Registers transmutation for every variant of your formula
	 *
	 * @param formula       formula for the circle you register
	 * @param transmutation Transmutaion data object
	 * @param rev           circleStrings for reversable parts
	 */
	public void registerForReversed(String formula, Transmutation transmutation, String... rev) {
		List<String> variants = new ArrayList<>((int) Math.pow(2, rev.length));
		getVariants(0, "", rev.length, variants);

		for (String str : variants) {
			String s = formula;
			for (int i = 0; i < rev.length; i++) {
				s = s.replaceFirst(rev[i] + s.charAt(s.indexOf(rev[i]) + 2), rev[i] + str.charAt(i));
			}
			register(s, transmutation);
		}
		if (variants.size() == 0)
			register(formula, transmutation);

		variants.clear();
	}
}
