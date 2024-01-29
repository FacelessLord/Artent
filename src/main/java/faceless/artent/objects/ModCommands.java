package faceless.artent.objects;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import faceless.artent.network.ArtentServerHook;
import faceless.artent.playerData.api.DataUtil;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ModCommands {
	public static LiteralArgumentBuilder<ServerCommandSource> ArtentAdminCommand =
		CommandManager.literal("artent").requires(source -> source.hasPermissionLevel(2))
			.then(CommandManager.literal("setMoney")
					  .then(CommandManager.argument("player", EntityArgumentType.players())
								.then(CommandManager.argument("money", IntegerArgumentType.integer(0))
										  .executes(ctx -> {
											  var players = EntityArgumentType.getPlayers(ctx, "player");
											  var money = IntegerArgumentType.getInteger(ctx, "money");

											  for (var player : players) {
												  var handler = DataUtil.getHandler(player);
												  handler.setMoney(money);
												  ArtentServerHook.packetSyncPlayerData(player);
											  }
											  return 0;
										  }))
					  ))
			.then(CommandManager.literal("addMoney")
					  .then(CommandManager.argument("player", EntityArgumentType.players())
								.then(CommandManager.argument("money", IntegerArgumentType.integer(0))
										  .executes(ctx -> {
											  var players = EntityArgumentType.getPlayers(ctx, "player");
											  var money = IntegerArgumentType.getInteger(ctx, "money");

											  for (var player : players) {
												  var handler = DataUtil.getHandler(player);
												  handler.addMoney(money);
												  ArtentServerHook.packetSyncPlayerData(player);
											  }
											  return 0;
										  }))));
}
