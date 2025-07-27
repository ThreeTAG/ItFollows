package net.threetag.itfollows.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.threetag.itfollows.entity.CursePlayerHandler;

public class ItFollowsCommand {

    public static final String ERROR_CURSE_ALREADY_STARTED = "commands.it_follows.start_curse.error.already_started";
    public static final String START_CURSE_SUCCESS = "commands.it_follows.start_curse.success";
    public static final String ERROR_NO_ACTIVE_CURSE = "commands.it_follows.stop_curse.error.no_active_curse";
    public static final String STOP_CURSE_SUCCESS = "commands.it_follows.stop_curse.success";

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(
                Commands.literal("it_follows")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                        .then(
                                Commands.literal("start_curse")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(commandContext ->
                                                        startCurse(commandContext, EntityArgument.getPlayer(commandContext, "player"))))

                        )
                        .then(
                                Commands.literal("stop_curse")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(commandContext ->
                                                        stopCurse(commandContext, EntityArgument.getPlayer(commandContext, "player"))))

                        )
        );
    }

    private static int startCurse(CommandContext<CommandSourceStack> commandContext, ServerPlayer player) {
        var handler = CursePlayerHandler.get(player);

        if (handler.isCurseActive()) {
            commandContext.getSource().sendFailure(Component.translatableEscape(ERROR_CURSE_ALREADY_STARTED, player.getDisplayName()));
            return 0;
        } else {
            handler.startCurse();
            commandContext.getSource().sendSuccess(() -> Component.translatableEscape(START_CURSE_SUCCESS, player.getDisplayName()), true);
            return 1;
        }
    }

    private static int stopCurse(CommandContext<CommandSourceStack> commandContext, ServerPlayer player) {
        var handler = CursePlayerHandler.get(player);

        if (!handler.isCurseActive()) {
            commandContext.getSource().sendFailure(Component.translatableEscape(ERROR_NO_ACTIVE_CURSE, player.getDisplayName()));
            return 0;
        } else {
            handler.stopCurse();
            commandContext.getSource().sendSuccess(() -> Component.translatableEscape(STOP_CURSE_SUCCESS, player.getDisplayName()), true);
            return 1;
        }
    }

}
