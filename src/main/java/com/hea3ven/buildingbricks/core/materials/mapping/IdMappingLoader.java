package com.hea3ven.buildingbricks.core.materials.mapping;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.google.common.base.Throwables;

import org.apache.commons.io.IOUtils;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class IdMappingLoader {
	private static Path idMappingFile;
	static byte[] checksum;

	public static void init(Path idMappingFile) {
		IdMappingLoader.idMappingFile = idMappingFile;

		MaterialIdMapping mapping = new MaterialIdMapping();
		if (Files.exists(idMappingFile)) {
			MaterialIdMapping.logger.info("Loading the material mapping");
			try (InputStream stream = Files.newInputStream(idMappingFile)) {
				NBTTagCompound nbt = CompressedStreamTools.readCompressed(stream);
				mapping.readFromNBT(nbt);
			} catch (IOException e) {
				Throwables.propagate(e);
			}
		}
		MaterialIdMapping.instance = mapping;
	}

	public static void save() {
		MaterialIdMapping.logger.info("Verifying the material mapping");
		MaterialIdMapping.instance.validate();
		MaterialIdMapping.logger.info("Saving the material mapping");
		try (OutputStream stream = Files.newOutputStream(idMappingFile, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING)) {
			NBTTagCompound nbt = new NBTTagCompound();
			MaterialIdMapping.instance.writeToNBT(nbt);
			CompressedStreamTools.writeCompressed(nbt, stream);
		} catch (IOException e) {
			Throwables.propagate(e);
		}
		try (InputStream stream = Files.newInputStream(idMappingFile)) {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] data = IOUtils.toByteArray(stream);
			md5.update(data);
			checksum = md5.digest();
		} catch (IOException e) {
			Throwables.propagate(e);
		} catch (NoSuchAlgorithmException e) {
			Throwables.propagate(e);
		}
	}

	public static boolean isInvalid(byte[] checksum) {
		return !Arrays.equals(IdMappingLoader.checksum, checksum);
	}
}
