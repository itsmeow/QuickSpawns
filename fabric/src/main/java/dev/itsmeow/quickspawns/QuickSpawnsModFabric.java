package dev.itsmeow.quickspawns;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class QuickSpawnsModFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, registryAccess, environment) -> QuickSpawnsMod.registerCommands(commandDispatcher));
    }
}
