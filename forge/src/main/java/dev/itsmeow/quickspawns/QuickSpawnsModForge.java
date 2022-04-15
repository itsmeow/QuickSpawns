package dev.itsmeow.quickspawns;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

@Mod(QuickSpawnsMod.MOD_ID)
@Mod.EventBusSubscriber(modid = QuickSpawnsMod.MOD_ID)
public class QuickSpawnsModForge {

    public QuickSpawnsModForge() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (s, b) -> true));
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        QuickSpawnsMod.registerCommands(event.getDispatcher());
    }

}
