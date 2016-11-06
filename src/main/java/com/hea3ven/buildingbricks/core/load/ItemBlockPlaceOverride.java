package com.hea3ven.buildingbricks.core.load;

import java.util.Set;

import com.google.common.collect.Sets;

import com.hea3ven.tools.asmtweaks.ASMMod;
import com.hea3ven.tools.asmtweaks.ASMTweak;
import com.hea3ven.tools.asmtweaks.ASMTweaksConfig.ASMTweakConfig;
import com.hea3ven.tools.asmtweaks.editors.ASMContext;
import com.hea3ven.tools.asmtweaks.editors.LabelRef;
import com.hea3ven.tools.asmtweaks.editors.MethodEditor;
import com.hea3ven.tools.asmtweaks.editors.opcodes.*;
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
		modifications.add(new ASMMethodModEditCode("net/minecraft/item/ItemBlock.onItemUse",
				"(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Lnet/minecraft/util/EnumActionResult;") {
			@Override
			protected void handle(MethodEditor editor) {
				// ItemBlockUseEvent event =
				// 		new ItemBlockUseEvent(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
				// if(MinecraftForge.EVENT_BUS.post(event)) return event.getActionResult();

				ASMContext ctx = new ASMContext();
				ctx.addImport("net/minecraft/item/ItemStack");
				ctx.addImport("net/minecraft/entity/player/EntityPlayer");
				ctx.addImport("net/minecraft/util/math/BlockPos");
				ctx.addImport("net/minecraft/util/EnumHand");
				ctx.addImport("net/minecraft/util/EnumFacing");
				ctx.addImport("net/minecraft/util/EnumActionResult");
				ctx.addImport("net/minecraft/world/World");
				ctx.addImport("net/minecraftforge/common/MinecraftForge");
				ctx.addImport("net/minecraftforge/fml/common/eventhandler/Event");
				ctx.addImport("net/minecraftforge/fml/common/eventhandler/EventBus");

				ctx.addImport("com/hea3ven/buildingbricks/core/block/placement/ItemBlockUseEvent");

				LabelRef lbl = editor.createLabel();

				editor.setInsertMode();
				editor.apply(
						editor.newInstructionBuilder(ctx)
								.typeInsn(TypeInsnOpcodes.NEW, "LItemBlockUseEvent;")
								.insn(InsnOpcodes.DUP)
								.varInsn(VarInsnOpcodes.ALOAD, 1)
								.varInsn(VarInsnOpcodes.ALOAD, 2)
								.varInsn(VarInsnOpcodes.ALOAD, 3)
								.varInsn(VarInsnOpcodes.ALOAD, 4)
								.varInsn(VarInsnOpcodes.ALOAD, 5)
								.varInsn(VarInsnOpcodes.ALOAD, 6)
								.varInsn(VarInsnOpcodes.FLOAD, 7)
								.varInsn(VarInsnOpcodes.FLOAD, 8)
								.varInsn(VarInsnOpcodes.FLOAD, 9)
								.methodInsn(MethodInsnOpcodes.INVOKESPECIAL, "ItemBlockUseEvent",
										"ItemBlockUseEvent.<init>",
										"(LItemStack;LEntityPlayer;LWorld;LBlockPos;LEnumHand;LEnumFacing;FFF)V")
								.varInsn(VarInsnOpcodes.ASTORE, 10)
								.fieldInsn(FieldInsnOpcodes.GETSTATIC, "MinecraftForge",
										"MinecraftForge.EVENT_BUS", "LEventBus;")
								.varInsn(VarInsnOpcodes.ALOAD, 10)
								.methodInsn(MethodInsnOpcodes.INVOKEVIRTUAL, "EventBus", "EventBus.post",
										"(LEvent;)Z")
								.jumpInsn(JumpInsnOpcodes.IFEQ, lbl)
								.varInsn(VarInsnOpcodes.ALOAD, 10)
								.methodInsn(MethodInsnOpcodes.INVOKEVIRTUAL, "ItemBlockUseEvent",
										"ItemBlockUseEvent.getActionResult", "()LEnumActionResult;")
								.insn(InsnOpcodes.ARETURN)
								.label(lbl));
			}
		});
		modifications.add(new ASMMethodModEditCode("net/minecraft/item/ItemBlock.canPlaceBlockOnSide",
				"(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)Z") {

			@Override
			public boolean isClientSideOnly() {
				return true;
			}

			@Override
			protected void handle(MethodEditor editor) {
				handle(editor, true);
			}

			protected boolean handle(MethodEditor editor, boolean asd) {
//				ItemBlockCanPlaceBlockOnSideEvent event =
//						new ItemBlockCanPlaceBlockOnSideEvent(world, pos, side, player, stack);
//				if (MinecraftForge.EVENT_BUS.post(event))
//					return event.isCanPlaceBlockOnSide();

				ASMContext ctx = new ASMContext();
				ctx.addImport("net/minecraft/item/ItemStack");
				ctx.addImport("net/minecraft/entity/player/EntityPlayer");
				ctx.addImport("net/minecraft/util/math/BlockPos");
				ctx.addImport("net/minecraft/util/EnumFacing");
				ctx.addImport("net/minecraft/world/World");
				ctx.addImport("net/minecraftforge/common/MinecraftForge");
				ctx.addImport("net/minecraftforge/fml/common/eventhandler/Event");
				ctx.addImport("net/minecraftforge/fml/common/eventhandler/EventBus");

				ctx.addImport(
						"com/hea3ven/buildingbricks/core/block/placement/ItemBlockCanPlaceBlockOnSideEvent");

				LabelRef lbl = editor.createLabel();

				editor.setInsertMode();
				editor.apply(editor.newInstructionBuilder(ctx)
						.typeInsn(TypeInsnOpcodes.NEW, "LItemBlockCanPlaceBlockOnSideEvent;")
						.insn(InsnOpcodes.DUP)
						.varInsn(VarInsnOpcodes.ALOAD, 1)
						.varInsn(VarInsnOpcodes.ALOAD, 2)
						.varInsn(VarInsnOpcodes.ALOAD, 3)
						.varInsn(VarInsnOpcodes.ALOAD, 4)
						.varInsn(VarInsnOpcodes.ALOAD, 5)
						.methodInsn(MethodInsnOpcodes.INVOKESPECIAL, "ItemBlockCanPlaceBlockOnSideEvent",
								"ItemBlockCanPlaceBlockOnSideEvent.<init>",
								"(LWorld;LBlockPos;LEnumFacing;LEntityPlayer;LItemStack;)V")
						.varInsn(VarInsnOpcodes.ASTORE, 7)
						.fieldInsn(FieldInsnOpcodes.GETSTATIC, "MinecraftForge", "MinecraftForge.EVENT_BUS",
								"LEventBus;")
						.varInsn(VarInsnOpcodes.ALOAD, 7)
						.methodInsn(MethodInsnOpcodes.INVOKEVIRTUAL, "EventBus", "EventBus.post",
								"(LEvent;)Z")
						.jumpInsn(JumpInsnOpcodes.IFEQ, lbl)
						.varInsn(VarInsnOpcodes.ALOAD, 7)
						.methodInsn(MethodInsnOpcodes.INVOKEVIRTUAL, "ItemBlockCanPlaceBlockOnSideEvent",
								"ItemBlockCanPlaceBlockOnSideEvent.isCanPlaceBlockOnSide", "()Z")
						.insn(InsnOpcodes.IRETURN)
						.label(lbl));
				return true;
			}
		});
	}
}

