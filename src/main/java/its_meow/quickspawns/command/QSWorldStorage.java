package its_meow.quickspawns.command;

import its_meow.quickspawns.Ref;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class QSWorldStorage extends WorldSavedData {
	private static final String DATA_NAME = Ref.MOD_ID + "_SpawnData";
	public NBTTagCompound data = new NBTTagCompound();
	
	public QSWorldStorage() {
		super(DATA_NAME);
	}
	
	public QSWorldStorage(String s) {
		super(s);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		data = nbt;
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = data;
		return compound;
	}
	
	
	public static QSWorldStorage get(World world) {
		QSWorldStorage save = (QSWorldStorage) world.getMapStorage().getOrLoadData(QSWorldStorage.class, DATA_NAME);
		if(save == null) {
			save = new QSWorldStorage();
			world.getMapStorage().setData(DATA_NAME, save);
		}
		return save;
	}
}

