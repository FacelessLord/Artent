package faceless.artent.registries;

import faceless.artent.objects.ModCommands;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class CommandRegistry implements IRegistry {
	@Override
	public void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(ModCommands.ArtentAdminCommand);
		});
	}
}
