package com.hea3ven.buildingbricks.core.load;

import net.minecraft.launchwrapper.IClassTransformer;

import com.hea3ven.tools.asmtweaks.ASMTweaksBuilder;
import com.hea3ven.tools.asmtweaks.ASMTweaksRunner;

public class ClassTransformerBuildingBricks implements IClassTransformer {

	ASMTweaksRunner tweaksRunner = ASMTweaksBuilder.create()
			.addMthdSrg("net/minecraft/item/ItemBlock.onItemUse",
					"net/minecraft/item/ItemBlock.func_180614_a",
					"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Lnet/minecraft/util/EnumActionResult;")
			.addMthdSrg("net/minecraft/item/ItemBlock.canPlaceBlockOnSide",
					"net/minecraft/item/ItemBlock.func_179222_a",
					"(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)Z")
			.addTweak(new ItemBlockPlaceOverride())
			.build();

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		return tweaksRunner.transform(name, transformedName, basicClass);
	}
}
