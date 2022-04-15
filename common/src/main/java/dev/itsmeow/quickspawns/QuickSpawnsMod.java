package dev.itsmeow.quickspawns;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class QuickSpawnsMod {

    public static final String MOD_ID = "quickspawns";

    public static void registerCommands(CommandDispatcher d) {
        Predicate<CommandSourceStack> isPlayer = source -> {
            try {
                return source.getPlayerOrException() != null;
            } catch(CommandSyntaxException e) {
                return false;
            }
        };
        // spawn
        d.register(Commands.literal("spawn").requires(isPlayer).executes(command -> {
            ServerPlayer player = command.getSource().getPlayerOrException();
            QSWorldStorage data = QSWorldStorage.getOrCreate(player.getLevel());
            if(data != null && !data.spawnExists()) {
                player.sendMessage(new TextComponent("No spawn has been set!").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                return 0;
            } else {
                ServerLevel level = player.getLevel().getServer().getLevel(data.getSpawnLevel());
                Vec3 pos = data.getSpawnPos();
                player.teleportTo(level, pos.x(), pos.y(), pos.z(), (float) data.getSpawnYaw(), (float) data.getSpawnPitch());
            }
            return 1;
        }));

        // setspawn
        d.register(Commands.literal("setspawn").requires(isPlayer).requires(s -> s.hasPermission(3)).executes(command -> {
            ServerPlayer player = command.getSource().getPlayerOrException();
            QSWorldStorage.getOrCreate(player.getLevel()).setSpawn(player.position(), player.getYHeadRot(), player.xRot, player.getLevel().dimension());
            player.sendMessage(new TextComponent("Spawn set.").withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
            return 1;
        }));
    }

    public static class QSWorldStorage extends SavedData {
        private static final String DATA_NAME = QuickSpawnsMod.MOD_ID + "_SpawnData";
        private boolean exists = false;
        private Vec3 pos;
        private ResourceKey<Level> dim;
        private double yaw;
        private double pitch;

        public QSWorldStorage() {
            super(DATA_NAME);
        }

        public static QSWorldStorage getOrCreate(Level level) {
            return level.getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(QSWorldStorage::new, DATA_NAME);
        }

        @Override
        public void load(CompoundTag compoundTag) {
            if(compoundTag != null && compoundTag.contains("x") && compoundTag.contains("y") && compoundTag.contains("z") && compoundTag.contains("yaw") && compoundTag.contains("pitch") && compoundTag.contains("dimension")) {
                pos = new Vec3(compoundTag.getDouble("x"), compoundTag.getDouble("y"), compoundTag.getDouble("z"));
                yaw = compoundTag.getDouble("yaw");
                pitch = compoundTag.getDouble("pitch");
                dim = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(compoundTag.getString("dimension")));
                exists = true;
            }
        }

        @Override
        public CompoundTag save(CompoundTag compoundTag) {
            if(compoundTag == null) {
                compoundTag = new CompoundTag();
            }
            compoundTag.putDouble("x", pos.x());
            compoundTag.putDouble("y", pos.y());
            compoundTag.putDouble("z", pos.z());
            compoundTag.putDouble("yaw", yaw);
            compoundTag.putDouble("pitch", pitch);
            compoundTag.putString("dimension", dim.location().toString());
            return compoundTag;
        }

        public QSWorldStorage setSpawn(Vec3 pos, double yaw, double pitch, ResourceKey<Level> dim) {
            this.pos = pos;
            this.yaw = yaw;
            this.pitch = pitch;
            this.dim = dim;
            exists = true;
            this.setDirty();
            return this;
        }

        public boolean spawnExists() {
            return exists;
        }

        public Vec3 getSpawnPos() {
            return pos;
        }

        public double getSpawnYaw() {
            return yaw;
        }

        public double getSpawnPitch() {
            return pitch;
        }

        public ResourceKey<Level> getSpawnLevel() {
            return dim;
        }
    }
}
