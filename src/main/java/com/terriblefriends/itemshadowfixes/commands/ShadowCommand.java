package com.terriblefriends.itemshadowfixes.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class ShadowCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(literal("shadow")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(ctx -> discord(ctx.getSource())));
    }

    private static int discord(ServerCommandSource source) throws CommandSyntaxException {
        int slotId = source.getPlayer().getInventory().getEmptySlot();
        if (slotId != -1) {
            source.getPlayer().getInventory().setStack(slotId,source.getPlayer().getMainHandStack());
        }
        return 1;
    }
}
