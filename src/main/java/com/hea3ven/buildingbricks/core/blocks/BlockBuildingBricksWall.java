package com.hea3ven.buildingbricks.core.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public class BlockBuildingBricksWall extends BlockBuildingBricksNonSolid {

	public BlockBuildingBricksWall(StructureMaterial structureMaterial, String name) {
		super(structureMaterial, name);

		IBlockState state = this.blockState.getBaseState();
		state = BlockProperties.setConnectionNorth(state, false);
		state = BlockProperties.setConnectionEast(state, false);
		state = BlockProperties.setConnectionSouth(state, false);
		state = BlockProperties.setConnectionWest(state, false);
		setDefaultState(state);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta |= (BlockProperties.getConnectionNorth(state)) ? 0x1 : 0;
		meta |= (BlockProperties.getConnectionEast(state)) ? 0x2 : 0;
		meta |= (BlockProperties.getConnectionSouth(state)) ? 0x4 : 0;
		meta |= (BlockProperties.getConnectionWest(state)) ? 0x8 : 0;
		return meta;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = this.getDefaultState();
		state = BlockProperties.setConnectionNorth(state, (meta & 0x1) > 0);
		state = BlockProperties.setConnectionEast(state, (meta & 0x2) > 0);
		state = BlockProperties.setConnectionSouth(state, (meta & 0x4) > 0);
		state = BlockProperties.setConnectionWest(state, (meta & 0x8) > 0);
		return state;
	}

	@Override
	protected void registerProperties(List<IProperty> props) {
		super.registerProperties(props);
		props.add(BlockProperties.CONNECT_NORTH);
		props.add(BlockProperties.CONNECT_EAST);
		props.add(BlockProperties.CONNECT_SOUTH);
		props.add(BlockProperties.CONNECT_WEST);
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX,
			float hitY, float hitZ, int meta, EntityLivingBase placer) {
		IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);
		if (!world.isAirBlock(pos.north()))
			state = BlockProperties.setConnectionNorth(state, true);
		if (!world.isAirBlock(pos.east()))
			state = BlockProperties.setConnectionEast(state, true);
		if (!world.isAirBlock(pos.south()))
			state = BlockProperties.setConnectionSouth(state, true);
		if (!world.isAirBlock(pos.west()))
			state = BlockProperties.setConnectionWest(state, true);
		return state;
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state,
			Block neighborBlock) {
		super.onNeighborBlockChange(world, pos, state, neighborBlock);
		state = BlockProperties.setConnectionNorth(state, !world.isAirBlock(pos.north()));
		state = BlockProperties.setConnectionEast(state, !world.isAirBlock(pos.east()));
		state = BlockProperties.setConnectionSouth(state, !world.isAirBlock(pos.south()));
		state = BlockProperties.setConnectionWest(state, !world.isAirBlock(pos.west()));
		world.setBlockState(pos, state, 3);
	}
}
