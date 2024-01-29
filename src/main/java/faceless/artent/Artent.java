package faceless.artent;

import faceless.artent.network.ArtentServerHook;
import faceless.artent.registries.*;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Artent implements ModInitializer {
	public static final String MODID = "artent";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("artent");
	public static ItemRegistry Items = new ItemRegistry();
	public static BlockRegistry Blocks = new BlockRegistry();
	public static BlockEntityRegistry BlockEntities = new BlockEntityRegistry();
	public static TransmutationRegistry Transmutations = new TransmutationRegistry();
	public static ItemGroupRegistry ItemGroups = new ItemGroupRegistry();
	public static ScreenHandlerRegistry ScreenHandlers = new ScreenHandlerRegistry();
	public static ArtentServerHook ServerHook = new ArtentServerHook();
	public static EnhancerRegistry Enhancers = new EnhancerRegistry();

	public static EntityRegistry Entities = new EntityRegistry();
	public static AlchemicalPotionRegistry Potions = new AlchemicalPotionRegistry();
	public static BrewingRegistry Brewing = new BrewingRegistry();
	public static StatusEffectsRegistry StatusEffects = new StatusEffectsRegistry();
	public static CommandRegistry Commands = new CommandRegistry();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		registerAll();
	}

	public void registerAll() {
		StatusEffects.register();
		Potions.register();
		Blocks.register();
		Items.register();
		BlockEntities.register();
		Entities.register();
		Transmutations.register();
		ScreenHandlers.register();
		Enhancers.register();
		Brewing.register();

		Commands.register();
		ServerHook.load();

		ItemGroups.register();
	}
}