package com.hea3ven.buildingbricks.core.load;

import java.util.Set;

import com.google.common.collect.Sets;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.LabelNode;

import com.hea3ven.tools.asmtweaks.ASMMod;
import com.hea3ven.tools.asmtweaks.ASMTweak;
import com.hea3ven.tools.asmtweaks.ASMTweaksConfig.ASMTweakConfig;
import com.hea3ven.tools.asmtweaks.editors.MethodEditor;
import com.hea3ven.tools.asmtweaks.tweaks.ASMMethodModEditCode;

public class ItemBlockPlaceOverride implements ASMTweak {
	private static Set<ASMMod> modifications = Sets.newHashSet();

	@Override
	public void configure(ASMTweakConfig conf) {
	}

	@Override
	public String getName() {
		return "ItemBlockPlaceOverride";
	}

	@Override
	public Set<ASMMod> getModifications() {
		return modifications;
	}

	static {
		modifications.add(new ASMMethodModEditCode("net/minecraft/item/ItemBlock/onItemUse",
				"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Lnet/minecraft/util/EnumActionResult;") {
			@Override
			protected void handle(MethodEditor editor) {
//				ItemBlockUseEvent event =
// 						new ItemBlockUseEvent(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
//				if(MinecraftForge.EVENT_BUS.post(event)) return event.getActionResult();

				editor.addImport("net/minecraft/item/ItemStack");
				editor.addImport("net/minecraft/entity/player/EntityPlayer");
				editor.addImport("net/minecraft/util/math/BlockPos");
				editor.addImport("net/minecraft/util/EnumHand");
				editor.addImport("net/minecraft/util/EnumFacing");
				editor.addImport("net/minecraft/util/EnumActionResult");
				editor.addImport("net/minecraft/world/World");
				editor.addImport("net/minecraftforge/common/MinecraftForge");
				editor.addImport("net/minecraftforge/fml/common/eventhandler/Event");
				editor.addImport("net/minecraftforge/fml/common/eventhandler/EventBus");

				editor.addImport("com/hea3ven/buildingbricks/core/block/placement/ItemBlockUseEvent");

				LabelNode lbl = new LabelNode();

				editor.setInsertMode();
				editor.typeInsn(Opcodes.NEW, "ItemBlockUseEvent");
				editor.insn(Opcodes.DUP);
				editor.varInsn(Opcodes.ALOAD, 1);
				editor.varInsn(Opcodes.ALOAD, 2);
				editor.varInsn(Opcodes.ALOAD, 3);
				editor.varInsn(Opcodes.ALOAD, 4);
				editor.varInsn(Opcodes.ALOAD, 5);
				editor.varInsn(Opcodes.ALOAD, 6);
				editor.varInsn(Opcodes.FLOAD, 7);
				editor.varInsn(Opcodes.FLOAD, 8);
				editor.varInsn(Opcodes.FLOAD, 9);
				editor.methodInsn(Opcodes.INVOKESPECIAL, "ItemBlockUseEvent",
						"ItemBlockUseEvent/<init>",
						"(LItemStack;LEntityPlayer;LWorld;LBlockPos;LEnumHand;LEnumFacing;FFF)V");
				editor.varInsn(Opcodes.ASTORE, 10);
				editor.fieldInsn(Opcodes.GETSTATIC, "MinecraftForge", "MinecraftForge/EVENT_BUS",
						"LEventBus;");
				editor.varInsn(Opcodes.ALOAD, 10);
				editor.methodInsn(Opcodes.INVOKEVIRTUAL, "EventBus", "EventBus/post", "(LEvent;)Z");
				editor.jumpInsn(Opcodes.IFEQ, lbl);
				editor.varInsn(Opcodes.ALOAD, 10);
				editor.methodInsn(Opcodes.INVOKEVIRTUAL, "ItemBlockUseEvent",
						"ItemBlockUseEvent/getActionResult", "()LEnumActionResult;");
				editor.insn(Opcodes.ARETURN);
				editor.labelInsn(lbl);
			}
		});
		modifications.add(new ASMMethodModEditCode("net/minecraft/item/ItemBlock/canPlaceBlockOnSide",
				"(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)Z") {

			@Override
			protected void handle(MethodEditor editor) {

//				ItemBlockCanPlaceBlockOnSideEvent event =
//						new ItemBlockCanPlaceBlockOnSideEvent(world, pos, side, player, stack);
//				if(MinecraftForge.EVENT_BUS.post(event)) return event.isCanPlaceBlockOnSide();

				editor.addImport("net/minecraft/item/ItemStack");
				editor.addImport("net/minecraft/entity/player/EntityPlayer");
				editor.addImport("net/minecraft/util/math/BlockPos");
				editor.addImport("net/minecraft/util/EnumFacing");
				editor.addImport("net/minecraft/world/World");
				editor.addImport("net/minecraftforge/common/MinecraftForge");
				editor.addImport("net/minecraftforge/fml/common/eventhandler/Event");
				editor.addImport("net/minecraftforge/fml/common/eventhandler/EventBus");

				editor.addImport(
						"com/hea3ven/buildingbricks/core/block/placement/ItemBlockCanPlaceBlockOnSideEvent");

				LabelNode lbl = new LabelNode();

				editor.setInsertMode();
				editor.typeInsn(Opcodes.NEW, "ItemBlockCanPlaceBlockOnSideEvent");
				editor.insn(Opcodes.DUP);
				editor.varInsn(Opcodes.ALOAD, 1);
				editor.varInsn(Opcodes.ALOAD, 2);
				editor.varInsn(Opcodes.ALOAD, 3);
				editor.varInsn(Opcodes.ALOAD, 4);
				editor.varInsn(Opcodes.ALOAD, 5);
				editor.methodInsn(Opcodes.INVOKESPECIAL, "ItemBlockCanPlaceBlockOnSideEvent",
						"ItemBlockCanPlaceBlockOnSideEvent/<init>",
						"(LWorld;LBlockPos;LEnumFacing;LEntityPlayer;LItemStack;)V");
				editor.varInsn(Opcodes.ASTORE, 6);
				editor.fieldInsn(Opcodes.GETSTATIC, "MinecraftForge", "MinecraftForge/EVENT_BUS",
						"LEventBus;");
				editor.varInsn(Opcodes.ALOAD, 6);
				editor.methodInsn(Opcodes.INVOKEVIRTUAL, "EventBus", "EventBus/post", "(LEvent;)Z");
				editor.jumpInsn(Opcodes.IFEQ, lbl);
				editor.varInsn(Opcodes.ALOAD, 6);
				editor.methodInsn(Opcodes.INVOKEVIRTUAL, "ItemBlockCanPlaceBlockOnSideEvent",
						"ItemBlockCanPlaceBlockOnSideEvent/isCanPlaceBlockOnSide", "()Z");
				editor.insn(Opcodes.IRETURN);
				editor.labelInsn(lbl);
			}
		});
	}
}
