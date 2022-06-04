package com.terriblefriends.itemshadowfixes;

import com.terriblefriends.itemshadowfixes.commands.ShadowCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class ItemShadowFixes implements ModInitializer {
    @Override
    public void onInitialize() {

        CommandRegistrationCallback.EVENT.register(ShadowCommand::register);

    }
}
