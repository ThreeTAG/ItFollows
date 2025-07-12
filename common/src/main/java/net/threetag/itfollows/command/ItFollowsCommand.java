package net.threetag.itfollows.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;
import net.threetag.itfollows.entity.TheEntity;

public class ItFollowsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(
                Commands.literal("it_follows")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                        .then(
                                Commands.literal("summon")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(commandContext ->
                                                        summon(commandContext, EntityArgument.getPlayer(commandContext, "player"))))

                        )
        );
    }

    private static int summon(CommandContext<CommandSourceStack> commandContext, Player player) {
        try {
            var entity = new TheEntity(player, 20);
            player.level().addFreshEntity(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }

}
