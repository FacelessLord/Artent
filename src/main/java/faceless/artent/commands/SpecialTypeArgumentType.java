package faceless.artent.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import faceless.artent.leveling.api.SpecialMobType;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpecialTypeArgumentType
  implements ArgumentType<SpecialMobType> {
	private static final Collection<String> EXAMPLES = Stream.of(
		SpecialMobType.Common,
		SpecialMobType.Cursed,
		SpecialMobType.Demonic,
		SpecialMobType.Eldritch,
		SpecialMobType.Wounded)
	  .map(t -> t.name().toLowerCase()).collect(Collectors.toList());
	private static final SpecialMobType[] VALUES = SpecialMobType.values();
	private static final DynamicCommandExceptionType INVALID_SPECIAL_MOB_TYPE_EXCEPTION = new DynamicCommandExceptionType(specialMobType -> Text.stringifiedTranslatable("artent.argument.specialMobType.invalid", specialMobType));

	@Override
	public SpecialMobType parse(StringReader stringReader) throws CommandSyntaxException {
		String string = stringReader.readUnquotedString();
		SpecialMobType specialMobType = SpecialMobType.fromName(string);
		if (specialMobType == null) {
			throw INVALID_SPECIAL_MOB_TYPE_EXCEPTION.createWithContext(stringReader, string);
		}
		return specialMobType;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		if (context.getSource() instanceof CommandSource) {
			return CommandSource.suggestMatching(Arrays.stream(VALUES).map(t -> t.name().toLowerCase()), builder);
		}
		return Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

	public static SpecialTypeArgumentType specialType() {
		return new SpecialTypeArgumentType();
	}

	public static SpecialMobType getSpecialMobType(CommandContext<ServerCommandSource> context, String name) throws CommandSyntaxException {
		return context.getArgument(name, SpecialMobType.class);
	}
}
