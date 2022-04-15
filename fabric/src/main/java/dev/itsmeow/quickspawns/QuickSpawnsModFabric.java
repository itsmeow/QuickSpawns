package dev.itsmeow.quickspawns;

import dev.itsmeow.quickspawns.QuickSpawnsMod;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class QuickSpawnsModFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, dedicated) -> QuickSpawnsMod.registerCommands(commandDispatcher));
    }
}
