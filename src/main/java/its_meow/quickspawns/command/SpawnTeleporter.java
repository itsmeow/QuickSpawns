package its_meow.quickspawns.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class SpawnTeleporter extends Teleporter{

	public SpawnTeleporter(WorldServer worldIn) {
		super(worldIn);
	}

	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw){
		EntityPlayerMP senderMP = (EntityPlayerMP) entityIn;
		MinecraftServer server = entityIn.getServer();
		File dataDir = new File(server.getDataDirectory().getAbsolutePath());
		EntityPlayer senderP = (EntityPlayer) entityIn;

		QSWorldStorage sd = QSWorldStorage.get(senderP.world);

		int[] data = sd.data.getIntArray("spawn");
		float posX = data[0];
		float posY = data[1];
		float posZ = data[2];
		float yaw = data[3];

		senderMP.setLocationAndAngles(posX + 0.5, posY + 0.5, posZ + 0.5, yaw, 0.0F);
		senderMP.setPositionAndUpdate(posX + 0.5, posY + 0.5, posZ + 0.5);
		senderMP.setRotationYawHead(yaw);
		entityIn.motionX = 0.0D;
		entityIn.motionY = 0.0D;
		entityIn.motionZ = 0.0D;
	}

	@Override
	public boolean placeInExistingPortal(Entity entityIn, float p_180620_2_) {
		return false;
	}

}