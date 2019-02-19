package its_meow.quickspawns.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandSpawn extends CommandBase {

	@Override
	public String getName() {
		return "spawn";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/spawn";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return sender instanceof EntityPlayer;
	}

	public static void print(String in) {
		System.out.println(in);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0)
		{
			int dimId = sender.getEntityWorld().provider.getDimension();
			EntityPlayerMP senderMP = (EntityPlayerMP) sender;
			PlayerList list = server.getPlayerList();

			BlockPos pos = sender.getPosition();
			EntityPlayer senderP = (EntityPlayer) sender;
			
			QSWorldStorage sd = QSWorldStorage.get(senderP.world);
			
			int[] data = sd.data.getIntArray("spawn");
			if(data.length <= 0) {
				throw new CommandException("There is no spawn set!");
			} else {
				int destWorldId = data[4];
				WorldServer destWorld = server.getWorld(destWorldId);
				if(destWorldId != dimId){
					list.transferPlayerToDimension(senderMP, destWorldId, new SpawnTeleporter(destWorld));
				} else {
					float posX = data[0];
					float posY = data[1];
					float posZ = data[2];
					float yaw = data[3];

					senderMP.setLocationAndAngles(posX + 0.5, posY + 0.5, posZ + 0.5, yaw, 0.0F);
					senderMP.setPositionAndUpdate(posX + 0.5, posY + 0.5, posZ + 0.5);
					senderMP.setRotationYawHead(yaw);
					senderMP.motionX = 0.0D;
					senderMP.motionY = 0.0D;
					senderMP.motionZ = 0.0D;
				}
			}
		} else {
			throw new WrongUsageException("Command /spawn does not take any arguments.");
		}
	}

}
