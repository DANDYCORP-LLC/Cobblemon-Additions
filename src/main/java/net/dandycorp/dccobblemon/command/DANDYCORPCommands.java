package net.dandycorp.dccobblemon.command;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.dandycorp.dccobblemon.util.ScreenShakeController;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;

import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class DANDYCORPCommands {

    private static final SuggestionProvider<ServerCommandSource> FADE_TYPE_SUGGESTIONS = (context, builder) ->
            CommandSource.suggestMatching(
                    List.of("linear", "l", "exponential", "exp", "e", "reverse_exponential", "reverse", "rev", "re", "r"),
                    builder
            );

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            var screenshakeCommand = buildScreenshakeCommand();
            var screenshakeNode = dispatcher.register(screenshakeCommand);

            dispatcher.register(literal("shakescreen").redirect(screenshakeNode));
            dispatcher.register(literal("shake").redirect(screenshakeNode));
        });
    }

    private static LiteralArgumentBuilder<ServerCommandSource> buildScreenshakeCommand() {
        return literal("screenshake")
                .requires(source -> source.hasPermissionLevel(2))

                // 1) No arguments -> Shake self with defaults
                .executes(ctx -> {
                    ServerPlayerEntity self = ctx.getSource().getPlayerOrThrow();
                    return doShake(ctx, List.of(self), 0.5f, 20, 40, "linear");
                })

                // 2) Branch: <intensity> -> Shake self
                .then(argument("intensity", floatArg(0)) // e.g. /screenshake 1.2
                        .executes(ctx -> {
                            ServerPlayerEntity self = ctx.getSource().getPlayerOrThrow();
                            float intensity = FloatArgumentType.getFloat(ctx, "intensity");
                            return doShake(ctx, List.of(self), intensity, 20, 40, "linear");
                        })
                        // /screenshake <intensity> <maxDuration>
                        .then(argument("maxDuration", integer(0))
                                .executes(ctx -> {
                                    ServerPlayerEntity self = ctx.getSource().getPlayerOrThrow();
                                    float intensity = FloatArgumentType.getFloat(ctx, "intensity");
                                    int maxDuration = IntegerArgumentType.getInteger(ctx, "maxDuration");
                                    return doShake(ctx, List.of(self), intensity, maxDuration, 40, "linear");
                                })
                                // /screenshake <intensity> <maxDuration> <fadeDuration>
                                .then(argument("fadeDuration", integer(0))
                                        .executes(ctx -> {
                                            ServerPlayerEntity self = ctx.getSource().getPlayerOrThrow();
                                            float intensity = FloatArgumentType.getFloat(ctx, "intensity");
                                            int maxDuration = IntegerArgumentType.getInteger(ctx, "maxDuration");
                                            int fadeDuration = IntegerArgumentType.getInteger(ctx, "fadeDuration");
                                            return doShake(ctx, List.of(self), intensity, maxDuration, fadeDuration, "linear");
                                        })
                                        // /screenshake <intensity> <maxDuration> <fadeDuration> <fadeType>
                                        .then(argument("fadeType", StringArgumentType.word())
                                                .suggests(FADE_TYPE_SUGGESTIONS)
                                                .executes(ctx -> {
                                                    ServerPlayerEntity self = ctx.getSource().getPlayerOrThrow();
                                                    float intensity = FloatArgumentType.getFloat(ctx, "intensity");
                                                    int maxDuration = IntegerArgumentType.getInteger(ctx, "maxDuration");
                                                    int fadeDuration = IntegerArgumentType.getInteger(ctx, "fadeDuration");
                                                    String fadeType = StringArgumentType.getString(ctx, "fadeType");
                                                    return doShake(ctx, List.of(self), intensity, maxDuration, fadeDuration, fadeType);
                                                })
                                        )
                                )
                        )
                )

                // 3) Branch: <targets> -> Shake target(s)
                .then(argument("targets", EntityArgumentType.players()) // e.g. /screenshake @p
                        .executes(ctx -> {
                            Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(ctx, "targets");
                            return doShake(ctx, targets, 0.5f, 20, 40, "linear");
                        })
                        .then(argument("intensity", floatArg(0))
                                .executes(ctx -> {
                                    Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(ctx, "targets");
                                    float intensity = FloatArgumentType.getFloat(ctx, "intensity");
                                    return doShake(ctx, targets, intensity, 20, 40, "linear");
                                })
                                .then(argument("maxDuration", integer(0))
                                        .executes(ctx -> {
                                            Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(ctx, "targets");
                                            float intensity = FloatArgumentType.getFloat(ctx, "intensity");
                                            int maxDuration = IntegerArgumentType.getInteger(ctx, "maxDuration");
                                            return doShake(ctx, targets, intensity, maxDuration, 40, "linear");
                                        })
                                        .then(argument("fadeDuration", integer(0))
                                                .executes(ctx -> {
                                                    Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(ctx, "targets");
                                                    float intensity = FloatArgumentType.getFloat(ctx, "intensity");
                                                    int maxDuration = IntegerArgumentType.getInteger(ctx, "maxDuration");
                                                    int fadeDuration = IntegerArgumentType.getInteger(ctx, "fadeDuration");
                                                    return doShake(ctx, targets, intensity, maxDuration, fadeDuration, "linear");
                                                })
                                                .then(argument("fadeType", StringArgumentType.word())
                                                        .suggests(FADE_TYPE_SUGGESTIONS)
                                                        .executes(ctx -> {
                                                            Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(ctx, "targets");
                                                            float intensity = FloatArgumentType.getFloat(ctx, "intensity");
                                                            int maxDuration = IntegerArgumentType.getInteger(ctx, "maxDuration");
                                                            int fadeDuration = IntegerArgumentType.getInteger(ctx, "fadeDuration");
                                                            String fadeType = StringArgumentType.getString(ctx, "fadeType");
                                                            return doShake(ctx, targets, intensity, maxDuration, fadeDuration, fadeType);
                                                        })
                                                )
                                        )
                                )
                        )
                );
    }


    private static int doShake(
            CommandContext<ServerCommandSource> ctx,
            Collection<ServerPlayerEntity> targets,
            float intensity,
            int maxDuration,
            int fadeDuration,
            String fadeTypeString
    ) {
        ScreenShakeController.FadeType fadeType = parseFadeType(fadeTypeString);

        for (ServerPlayerEntity player : targets) {
            ScreenShakeController.sendShakeToClient(player, intensity, maxDuration, fadeDuration, fadeType);
        }

        ctx.getSource().sendFeedback(
                () -> Text.literal(String.format(
                        "Shaking %d player(s): intensity=%.2f, duration=%d, fade=%d, type=%s",
                        targets.size(),
                        intensity,
                        maxDuration,
                        fadeDuration,
                        fadeType
                )),
                false
        );
        return 1;
    }

    private static ScreenShakeController.FadeType parseFadeType(String s) {
        if (s == null) {
            return ScreenShakeController.FadeType.LINEAR;
        }
        s = s.toLowerCase();
        return switch (s) {
            case "exponential", "exp", "e" -> ScreenShakeController.FadeType.EXPONENTIAL;
            case "reverse_exponential", "reverse", "rev", "re", "r" -> ScreenShakeController.FadeType.REVERSE_EXPONENTIAL;
            default -> ScreenShakeController.FadeType.LINEAR;
        };
    }
}
