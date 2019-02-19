package its_meow.quickspawns;

import its_meow.quickspawns.command.CommandSetSpawn;
import its_meow.quickspawns.command.CommandSpawn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Ref.MOD_ID, name = Ref.NAME, version = Ref.VERSION, acceptedMinecraftVersions = Ref.acceptedMCV, acceptableRemoteVersions = "*")
public class QuickSpawnsMod {

	@Instance(Ref.MOD_ID) 
	public static QuickSpawnsMod mod;
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandSetSpawn());
		event.registerServerCommand(new CommandSpawn());
	}
	
}
