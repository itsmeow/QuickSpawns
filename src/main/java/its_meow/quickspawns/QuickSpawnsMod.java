package its_meow.quickspawns;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod(QuickSpawnsMod.MOD_ID)
@Mod.EventBusSubscriber(modid = QuickSpawnsMod.MOD_ID)
public class QuickSpawnsMod {

    public static final String MOD_ID = "quickspawns";

    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        registerCommands(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        registerCommands(event.getServer().getCommandManager().getDispatcher());
    }

    public static void registerCommands(CommandDispatcher<CommandSource> commandDispatcher) {
        // spawn
        commandDispatcher.register(Commands.literal("spawn").requires(source -> {
            try {
                return source.asPlayer() != null;
            } catch(CommandSyntaxException e) {
                return false;
            }
        }).executes(command -> {
            ServerPlayerEntity player = command.getSource().asPlayer();
            CompoundNBT data = QSWorldStorage.get(player.getEntityWorld()).data;
            if(data == null || !data.contains("dimension") || !data.contains("x") || !data.contains("y") || !data.contains("z") || !data.contains("yaw") || !data.contains("pitch")) {
                player.sendMessage(new StringTextComponent("No spawn has been set!").setStyle(Style.EMPTY.withColor(TextFormatting.RED)), Util.NIL_UUID);
                return 0;
            } else {
                ServerWorld destWorld = player.getEntityWorld().getServer().getWorld(RegistryKey.of(Registry.DIMENSION, new ResourceLocation(data.getString("dimension"))));
                player.teleport(destWorld, data.getDouble("x"), data.getDouble("y"), data.getDouble("z"), data.getFloat("yaw"), data.getFloat("pitch"));
            }
            return 1;
        }));

        // setspawn
        commandDispatcher.register(Commands.literal("setspawn").requires(source -> {
            try {
                return source.asPlayer() != null;
            } catch(CommandSyntaxException e) {
                return false;
            }
        }).requires(s -> s.hasPermissionLevel(3)).executes(command -> {
            ServerPlayerEntity player = command.getSource().asPlayer();
            QSWorldStorage sd = QSWorldStorage.get(player.world);
            sd.data.putDouble("x", player.getX());
            sd.data.putDouble("y", player.getY());
            sd.data.putDouble("z", player.getZ());
            sd.data.putFloat("yaw", player.rotationYawHead);
            sd.data.putFloat("pitch", player.rotationPitch);
            sd.data.putString("dimension", player.getEntityWorld().getRegistryKey().getValue().toString());
            sd.markDirty();
            player.sendMessage(new StringTextComponent("Spawn set.").setStyle(Style.EMPTY.withColor(TextFormatting.GREEN)), Util.NIL_UUID);
            return 1;
        }));
    }

    public static class QSWorldStorage extends WorldSavedData {
        private static final String DATA_NAME = QuickSpawnsMod.MOD_ID + "_SpawnData";
        public CompoundNBT data = new CompoundNBT();

        public QSWorldStorage() {
            super(DATA_NAME);
        }

        public QSWorldStorage(String s) {
            super(s);
        }

        public static QSWorldStorage get(World world) {
            return world.getServer().getWorld(World.OVERWORLD).getSavedData().getOrCreate(QSWorldStorage::new, DATA_NAME);
        }

        @Override
        public void read(CompoundNBT nbt) {
            data = nbt;
        }

        @Override
        public CompoundNBT write(CompoundNBT compound) {
            compound = data;
            return compound;
        }
    }

}
