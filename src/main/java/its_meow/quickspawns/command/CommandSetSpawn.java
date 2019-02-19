package its_meow.quickspawns.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class CommandSetSpawn extends CommandBase {

	@Override
	public String getName() {
		return "setspawn";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "Use /setspawn to set home location.";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return sender instanceof EntityPlayer && sender.canUseCommand(2, "");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayer || sender instanceof EntityPlayerMP) {
			if (args.length == 0) {
				BlockPos pos = sender.getPosition();
				EntityPlayer senderP = (EntityPlayer) sender;

				QSWorldStorage sd = QSWorldStorage.get(senderP.world);

				int[] arrayToStore = new int[5]; 
				arrayToStore[0] = pos.getX();
				arrayToStore[1] = pos.getY();
				arrayToStore[2] = pos.getZ();
				arrayToStore[3] = (int) senderP.rotationYawHead;
				arrayToStore[4] = senderP.getEntityWorld().provider.getDimension();

				sd.data.setIntArray("spawn", arrayToStore);
				sd.markDirty();
				senderP.getEntityWorld().setSpawnPoint(pos);
				sender.sendMessage(new TextComponentString("Spawn set."));
			} else {
				throw new WrongUsageException("Command /setspawn does not take any arguments.");
			}
		}
	}

}
