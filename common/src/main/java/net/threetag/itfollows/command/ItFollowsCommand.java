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
            commandContext.getSource().sendFailure(Component.literal("Curse already started!!!"));
            return 0;
        } else {
            handler.startCurse();
            commandContext.getSource().sendSuccess(() -> Component.literal("curse started!!"), true);
            return 1;
        }
    }

    private static int stopCurse(CommandContext<CommandSourceStack> commandContext, ServerPlayer player) {
        var handler = CursePlayerHandler.get(player);

        if (!handler.isCurseActive()) {
            commandContext.getSource().sendFailure(Component.literal("No Curse active!!!"));
            return 0;
        } else {
            handler.stopCurse();
            commandContext.getSource().sendSuccess(() -> Component.literal("curse stopped!!"), true);
            return 1;
        }
    }

}
