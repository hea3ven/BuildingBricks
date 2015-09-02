package com.hea3ven.buildingbricks;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.hea3ven.buildingbricks.core.ProxyCommonBuildingBricks;
import com.hea3ven.buildingbricks.core.items.ItemTrowel;
import com.hea3ven.buildingbricks.core.materials.MaterialResourceLoader;
import com.hea3ven.buildingbricks.core.network.TrowelRotateBlockTypeMessage;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;

@Mod(modid = ModBuildingBricks.MODID, name = "Building Bricks", version = ModBuildingBricks.VERSION)
public class ModBuildingBricks {
	public static final String MODID = "buildingbricks";
	public static final String VERSION = "1.0.0";

	@SidedProxy(serverSide = "com.hea3ven.buildingbricks.core.ProxyCommonBuildingBricks",
			clientSide = "com.hea3ven.buildingbricks.core.ProxyClientBuildingBricks")
	private static ProxyCommonBuildingBricks proxy;

	public static SimpleNetworkWrapper netChannel;

	public static ItemTrowel trowel;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		netChannel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		netChannel.registerMessage(TrowelRotateBlockTypeMessage.Handler.class,
				TrowelRotateBlockTypeMessage.class, 0, Side.SERVER);

		GameRegistry.registerTileEntity(TileMaterial.class, "tile.material");

		MaterialResourceLoader.discoverMaterials();

		trowel = new ItemTrowel();
		GameRegistry.registerItem(trowel, "trowel");

		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
}
