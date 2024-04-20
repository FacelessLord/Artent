package faceless.artent.objects;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import faceless.artent.commands.SpecialTypeArgumentType;
import faceless.artent.leveling.api.ILeveledMob;
import faceless.artent.leveling.api.ISpecialMob;
import faceless.artent.network.ArtentServerHook;
import faceless.artent.playerData.api.DataUtil;
import faceless.artent.spells.api.ICaster;
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
			  }))))
		.then(CommandManager.literal("setLevel")
		  .then(CommandManager.argument("entities", EntityArgumentType.entities())
			.then(CommandManager.argument("level", IntegerArgumentType.integer(1))
			  .executes(ctx -> {
				  var entities = EntityArgumentType.getEntities(ctx, "entities");
				  var level = IntegerArgumentType.getInteger(ctx, "level");

				  for (var entity : entities) {
					  if (!(entity instanceof ILeveledMob leveledMob))
						  continue;
					  leveledMob.setLevel(level);
				  }
				  return 0;
			  }))))
		.then(CommandManager.literal("restoreMana")
		  .then(CommandManager.argument("entities", EntityArgumentType.entities())
			.executes(ctx -> {
				var entities = EntityArgumentType.getEntities(ctx, "entities");

				for (var entity : entities) {
					if (!(entity instanceof ICaster caster))
						continue;
					caster.restoreMana();
				}
				return 0;
			})))
		.then(CommandManager.literal("setSpecialType")
		  .then(CommandManager.argument("entities", EntityArgumentType.entities())
			.then(CommandManager.argument("type", SpecialTypeArgumentType.specialType())
			  .executes(ctx -> {
				  var entities = EntityArgumentType.getEntities(ctx, "entities");
				  var type = SpecialTypeArgumentType.getSpecialMobType(ctx, "type");

				  for (var entity : entities) {
					  if (!(entity instanceof ISpecialMob specialMob))
						  continue;
					  specialMob.setSpecialMobType(type);
				  }
				  return 0;
			  }))));
}
