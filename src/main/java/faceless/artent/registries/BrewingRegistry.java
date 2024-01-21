package faceless.artent.registries;

import faceless.artent.api.math.Color;
import faceless.artent.brewing.BrewingAutomata;
import faceless.artent.brewing.api.BrewingIngredient;

import java.util.Hashtable;
import java.util.List;

import static faceless.artent.objects.AlchemicalPotions.*;
import static faceless.artent.objects.BrewingRecipes.*;

public class BrewingRegistry implements IRegistry {
	public static final Hashtable<BrewingIngredient, Color> Ingredients = new Hashtable<>();

	@Override
	public void register() {
		registerIngredients();
		registerRecipes();
		registerAdditionalEdges();
	}

	private void registerIngredients() {
		Ingredients.put(Shroom, Color.Cobalt);
		Ingredients.put(Shadowveil, Color.Black);
		Ingredients.put(CrimsonwoodBerry, Color.Red);
		Ingredients.put(CrimsonLeaf, Color.Red);
		Ingredients.put(Blackberry, Color.Purple);
		Ingredients.put(Blueberry, Color.Blue);
		Ingredients.put(Cloudberry, Color.Gold);
		Ingredients.put(Raspberry, Color.Red);
		Ingredients.put(Bone, Color.White);
		Ingredients.put(GlowstoneDust, Color.Yellow);
		Ingredients.put(Redstone, Color.Red);
		Ingredients.put(Apple, Color.Red);
		Ingredients.put(BrownMushroom, Color.Brown);
		Ingredients.put(RedMushroom, Color.Red);
		Ingredients.put(ChorusPlant, Color.Purple);
		Ingredients.put(BlazePowder, Color.Gold);
		Ingredients.put(EnderPearl, Color.Cyan);
		Ingredients.put(GlisteningMelon, Color.Gold.add(Color.Red));
		Ingredients.put(Kelp, Color.Green);
		Ingredients.put(Slime, Color.Green);
		Ingredients.put(Snowball, Color.White);
		Ingredients.put(RabbitsFoot, Color.Brown);
		Ingredients.put(PufferFish, Color.Yellow);
		Ingredients.put(Map, Color.Orange);
		Ingredients.put(DragonBreath, Color.Pink.add(Color.Purple));
		Ingredients.put(NetherWart, Color.Red.add(Color.Purple));
		Ingredients.put(SpiderEye, Color.Red);
		Ingredients.put(FermentedSpiderEye, Color.Red.add(Color.Brown));
		Ingredients.put(MagmaCream, Color.Red.add(Color.Gold));
	}

	private void registerRecipes() {
		RecipeAutomata = new BrewingAutomata();
		RecipeAutomata.addRecipe(POISON, Blackberry, NetherWart, SpiderEye, Cloudberry, Kelp);
		RecipeAutomata.addRecipe(STRENGTH, Blackberry, NetherWart, Shadowveil, Raspberry, BlazePowder);
		RecipeAutomata.addRecipe(VAMPIRISM, Blackberry, Redstone, Shroom, Blueberry, CrimsonwoodBerry);
		RecipeAutomata.addRecipe(HOLY_WATER, Blackberry, Redstone, Bone, Raspberry, Kelp, PufferFish);
		RecipeAutomata.addRecipe(BERSERK, Blackberry, Redstone, Shadowveil, Cloudberry, GlowstoneDust, FermentedSpiderEye);

		RecipeAutomata.addRecipe(STONE_SKIN, Raspberry, CrimsonLeaf, Cloudberry, Blackberry, PufferFish);
		RecipeAutomata.addRecipe(FIRE_RESISTANCE, Raspberry, CrimsonLeaf, Kelp, Redstone, MagmaCream);
		RecipeAutomata.addRecipe(FREEZING, Raspberry, CrimsonLeaf, Slime, GlowstoneDust, Snowball);
		RecipeAutomata.addRecipe(LIQUID_FLAME, Raspberry, CrimsonLeaf, Shadowveil, Blackberry, BlazePowder);
		RecipeAutomata.addRecipe(HEALING, Raspberry, Apple, Shroom, Raspberry, FermentedSpiderEye);
		RecipeAutomata.addRecipe(ANTIDOTE, Raspberry, Apple, SpiderEye, Cloudberry, Kelp);

		RecipeAutomata.addRecipe(FAST_SWIMMING, Cloudberry, Shadowveil, Blueberry, Kelp, Snowball, Shroom);
		RecipeAutomata.addRecipe(WATER_BREATHING, Cloudberry, Shadowveil, Blueberry, Slime, Kelp, Shroom);
		RecipeAutomata.addRecipe(JUMP_BOOST, Cloudberry, Shadowveil, Blueberry, NetherWart, CrimsonLeaf, Cloudberry);
		RecipeAutomata.addRecipe(FEATHER_FALLING, Cloudberry, Redstone, PufferFish, BrownMushroom, Shadowveil, Bone);
		RecipeAutomata.addRecipe(NIGHT_VISION, Cloudberry, Redstone, PufferFish, NetherWart, CrimsonLeaf, GlowstoneDust);

		RecipeAutomata.addRecipe(FLIGHT, Blueberry, Shadowveil, Shroom, EnderPearl, NetherWart, ChorusPlant);
		RecipeAutomata.addRecipe(LUCK, Blueberry, Shadowveil, Shroom, ChorusPlant, CrimsonwoodBerry, EnderPearl);
		RecipeAutomata.addRecipe(SATURATION, Blueberry, Shadowveil, CrimsonLeaf, BlazePowder, Slime, PufferFish);
		RecipeAutomata.addRecipe(SURFACE_TELEPORTATION, Blueberry, BrownMushroom, GlowstoneDust, Cloudberry, BlazePowder, EnderPearl);
		RecipeAutomata.addRecipe(LUMBERJACK, Blueberry, BrownMushroom, Kelp, Bone, Cloudberry, CrimsonLeaf);
		RecipeAutomata.addRecipe(HASTE, Blueberry, BrownMushroom, Kelp, Bone, Cloudberry, CrimsonLeaf, FermentedSpiderEye, Shadowveil, NetherWart, GlowstoneDust);
	}

	private void registerAdditionalEdges() {
		RecipeAutomata.addEdge(List.of(Blackberry, NetherWart, SpiderEye), List.of(Raspberry, Apple, SpiderEye, Cloudberry), Shroom);
		RecipeAutomata.addEdge(List.of(Blackberry, NetherWart, Shadowveil, Raspberry), List.of(Blackberry, Redstone, Shroom, Blueberry), Redstone);
		RecipeAutomata.addEdge(List.of(Blackberry, Redstone, Bone, Raspberry), List.of(Blackberry, Redstone, Shroom), CrimsonwoodBerry);
		RecipeAutomata.addEdge(List.of(Blackberry, Redstone, Bone), List.of(Raspberry), Kelp);
		RecipeAutomata.addEdge(List.of(Blackberry, Redstone, Shadowveil), List.of(Raspberry), Raspberry);
		RecipeAutomata.addEdge(List.of(Raspberry, CrimsonLeaf, Cloudberry), List.of(Blackberry, Redstone, Shadowveil, Cloudberry), Allium);
		RecipeAutomata.addEdge(List.of(Raspberry, CrimsonLeaf, Kelp), List.of(Raspberry, CrimsonLeaf, Slime, GlowstoneDust), GlowstoneDust);
		RecipeAutomata.addEdge(List.of(Raspberry, Apple, Shroom, Raspberry), List.of(Raspberry, CrimsonLeaf, Cloudberry, Blackberry), Blackberry);
		RecipeAutomata.addEdge(List.of(Raspberry, Apple, SpiderEye), List.of(Raspberry, CrimsonLeaf, Shadowveil), Blueberry);
		RecipeAutomata.addEdge(List.of(Raspberry, Apple, Shroom), List.of(Cloudberry, Shadowveil), Blackberry);
		RecipeAutomata.addEdge(List.of(Raspberry, CrimsonLeaf), List.of(Cloudberry, Shadowveil, Blueberry), Blackberry);
		RecipeAutomata.addEdge(List.of(Cloudberry, Shadowveil, Blueberry, Kelp), List.of(Raspberry, Apple, SpiderEye), FermentedSpiderEye);
		RecipeAutomata.addEdge(List.of(Cloudberry), List.of(Raspberry, Apple), Apple);
		RecipeAutomata.addEdge(List.of(Cloudberry, Shadowveil, Blueberry, Kelp), List.of(Cloudberry, Shadowveil, Blueberry, Slime), Raspberry);
		RecipeAutomata.addEdge(List.of(Cloudberry, Shadowveil, Blueberry, NetherWart), List.of(Cloudberry, Redstone), BlazePowder);
		RecipeAutomata.addEdge(List.of(Cloudberry, Shadowveil), List.of(Cloudberry, Redstone, PufferFish), Bone);
		RecipeAutomata.addEdge(List.of(Cloudberry, Redstone, PufferFish, BrownMushroom), List.of(Cloudberry, Shadowveil, Blueberry), Allium);
		RecipeAutomata.addEdge(List.of(Blueberry, Shadowveil), List.of(Cloudberry, Redstone), Cloudberry);
		RecipeAutomata.addEdge(List.of(Blueberry, Shadowveil, Shroom, ChorusPlant), List.of(Cloudberry, Redstone, PufferFish), BrownMushroom);
		RecipeAutomata.addEdge(List.of(Cloudberry, Redstone, PufferFish, NetherWart, CrimsonLeaf), List.of(Blueberry, Shadowveil, CrimsonLeaf, BlazePowder, Slime), BlazePowder);
		RecipeAutomata.addEdge(List.of(Blueberry, Shadowveil, Shroom, EnderPearl, NetherWart), List.of(Blueberry, Shadowveil, Shroom, ChorusPlant, CrimsonwoodBerry), CrimsonwoodBerry);
		RecipeAutomata.addEdge(List.of(Blueberry, Shadowveil, CrimsonLeaf, BlazePowder), List.of(Blueberry, Shadowveil, Shroom, ChorusPlant), ChorusPlant);
		RecipeAutomata.addEdge(List.of(Blueberry, Shadowveil, CrimsonLeaf, BlazePowder), List.of(Blueberry, BrownMushroom, GlowstoneDust, Cloudberry), GlowstoneDust);
		RecipeAutomata.addEdge(List.of(Blueberry, BrownMushroom, Kelp, Bone), List.of(Blueberry, BrownMushroom, GlowstoneDust, Cloudberry, BlazePowder), GlowstoneDust);
		RecipeAutomata.addEdge(List.of(Blueberry, BrownMushroom, Kelp), List.of(Blackberry, NetherWart), GlowstoneDust);
		RecipeAutomata.addEdge(List.of(Blackberry, NetherWart, SpiderEye), List.of(Blueberry, NetherWart, Kelp, Bone), Shadowveil);
	}

}
