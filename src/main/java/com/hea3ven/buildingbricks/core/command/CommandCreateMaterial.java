package com.hea3ven.buildingbricks.core.command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

import com.google.common.base.Throwables;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public class CommandCreateMaterial extends CommandBase {
	@Override
	public String getCommandName() {
		return "material";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "buildingbricks.commands.material.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args)
			throws CommandException {
		EntityPlayer player = (EntityPlayer) sender;

		if (args.length < 1)
			throw new WrongUsageException("buildingbricks.commands.material.usage");

		if (Objects.equals(args[0], "generate")) {
			if (args.length < 2)
				throw new WrongUsageException("buildingbricks.commands.material.usage");

			String structMatName = args[1];
			StructureMaterial structMat = StructureMaterial.valueOf(structMatName);

			ItemStack blockStack = player.inventory.getStackInSlot(0);
			if (blockStack == null)
				return;

			Block block = ((ItemBlock) blockStack.getItem()).block;

			String id = Block.REGISTRY.getNameForObject(block).toString();
			if (args.length > 2)
				id = id.split(":")[0] + ":" + args[2];

			JsonObject mat = new JsonObject();
			mat.addProperty("name", blockStack.getDisplayName());
			mat.addProperty("id", id);
			mat.addProperty("type", structMat.getName());
			mat.addProperty("hardness", block.getBlockHardness(block.getDefaultState(), null, null));
			mat.addProperty("resistance", (block.getExplosionResistance(null) * 5f) / 3f);
			mat.add("textures", new JsonObject());

			JsonObject blocks = new JsonObject();
			for (int i = 0; i < MaterialBlockType.values().length; i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (stack == null)
					continue;

				if (!stack.getHasSubtypes()) {
					blocks.addProperty(MaterialBlockType.values()[i].toString().toLowerCase(),
							Item.REGISTRY.getNameForObject(stack.getItem()).toString());
				} else {
					JsonObject blockInfoJson = new JsonObject();
					blockInfoJson.addProperty("id",
							Item.REGISTRY.getNameForObject(stack.getItem()).toString());
					blockInfoJson.addProperty("meta", stack.getMetadata());
					blocks.add(MaterialBlockType.values()[i].toString().toLowerCase(), blockInfoJson);
				}
			}
			mat.add("blocks", blocks);
			String matString = new GsonBuilder().setPrettyPrinting()
					.serializeNulls()
					.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
					.create()
					.toJson(mat)
					.replace("  ", "\t");
			Path outputPath = Paths.get(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), "config",
					"BuildingBricks", "resources", "assets", id.split(":")[0], "materials");
			if (!Files.exists(outputPath))
				try {
					Files.createDirectories(outputPath);
				} catch (IOException e) {
					Throwables.propagate(e);
				}
			outputPath = outputPath.resolve(id.split(":")[1] + ".json");
			try (BufferedWriter stream = Files.newBufferedWriter(outputPath, StandardOpenOption.CREATE_NEW)) {
				stream.write(matString);
			} catch (FileAlreadyExistsException e) {
				player.addChatMessage(
						new TextComponentTranslation("buildingbricks.commands.material.errorFileExists"));
			} catch (IOException e) {
				Throwables.propagate(e);
			}
		}
	}
}
