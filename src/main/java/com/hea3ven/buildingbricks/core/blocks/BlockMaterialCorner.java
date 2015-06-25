package com.hea3ven.buildingbricks.core.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import com.hea3ven.buildingbricks.core.lib.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.tileentity.TileEntityMaterial;

public class BlockMaterialCorner extends BlockCorner {

	public BlockMaterialCorner(net.minecraft.block.material.Material material, String name) {
		super(material, name);
	}

	@Override
	protected BlockState createBlockState() {
		List<IProperty> props = new ArrayList<IProperty>();
		registerProperties(props);
		return new ExtendedBlockState(this, props.toArray(new IProperty[0]),
				new IUnlistedProperty[] {TileEntityMaterial.MATERIAL});
	}

	@Override
	protected void registerProperties(List<IProperty> props) {
		super.registerProperties(props);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityMaterial();
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && ((TileEntityMaterial) te).material() != null)
			return TileEntityMaterial.setStateMaterial((IExtendedBlockState) state,
					((TileEntityMaterial) te).material());
		else
			return state;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
			EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		if (stack.hasTagCompound()) {
			TileEntityMaterial teMat = (TileEntityMaterial) worldIn.getTileEntity(pos);
			teMat.setMaterial(MaterialRegistry.get(stack.getTagCompound().getString("material")));
		}
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (Material mat : MaterialRegistry.getAll()) {
			BlockDescription blockDesc = mat.getBlock(MaterialBlockType.CORNER);
			if (blockDesc != null && blockDesc.getBlock() == this) {
				ItemStack stack = new ItemStack(itemIn);
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("material", mat.materialId());
				stack.setTagCompound(nbt);
				list.add(stack);
			}
		}
	}
}
