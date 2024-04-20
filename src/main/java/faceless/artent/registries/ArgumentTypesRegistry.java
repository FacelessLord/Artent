package faceless.artent.registries;

import faceless.artent.Artent;
import faceless.artent.commands.SpecialTypeArgumentType;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;

public class ArgumentTypesRegistry implements IRegistry {
	@Override
	public void register() {
		ArgumentTypeRegistry.registerArgumentType(new Identifier(Artent.MODID, "special_mob_type"), SpecialTypeArgumentType.class, ConstantArgumentSerializer.of(SpecialTypeArgumentType::specialType));
	}

}
