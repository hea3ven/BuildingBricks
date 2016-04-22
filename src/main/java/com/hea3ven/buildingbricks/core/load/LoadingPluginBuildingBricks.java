package com.hea3ven.buildingbricks.core.load;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import com.hea3ven.tools.bootstrap.Bootstrap;

public class LoadingPluginBuildingBricks implements IFMLLoadingPlugin {
	static {
		Bootstrap.init();
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"com.hea3ven.buildingbricks.core.load.ClassTransformerBuildingBricks"};
	}

	@Override
	public String getModContainerClass() {
		return "com.hea3ven.buildingbricks.core.load.ModContainerBuildingBricks";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
