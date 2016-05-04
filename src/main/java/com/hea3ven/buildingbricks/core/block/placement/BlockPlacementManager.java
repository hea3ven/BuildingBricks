package com.hea3ven.buildingbricks.core.block.placement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumActionResult;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class BlockPlacementManager {

	private static BlockPlacementManager instance;

	public static BlockPlacementManager getInstance() {
		return instance;
	}

	private final ArrayList<PlacementOverrideEntry> handlerEntries = new ArrayList<>();
	private List<IBlockPlacementHandler> handlers;

	static {
		instance = new BlockPlacementManager();
		instance.add(20, new SlabCombinePlacementHandler());
		instance.add(20, new StepCombinePlacementHandler());
		instance.add(20, new CornerCombinePlacementHandler());
		instance.add(69, new SlabPlacementHandler());
		instance.add(70, new FirstFallbackPlacementHandler());
		instance.add(200, new FallbackPlacementHandler());
	}

	public void add(int priority, IBlockPlacementHandler handler) {
		handlerEntries.add(new PlacementOverrideEntry(priority, handler));
		handlerEntries.sort((e1, e2) -> Integer.compare(e1.priority, e2.priority));
		handlers = handlerEntries.stream().map(input -> input.handler).collect(Collectors.toList());
	}

	public List<IBlockPlacementHandler> getHandlers() {
		return handlers;
	}

	@SubscribeEvent
	public void onItemBlockCanPlaceBlockOnSide(ItemBlockCanPlaceBlockOnSideEvent event) {
		for (IBlockPlacementHandler handler : getHandlers()) {
			if (!handler.isHandled(event.getStack()))
				continue;

			event.setCanceled(true);
			if (handler.canPlaceBlockOnSide(event.getWorld(), event.getPos(), event.getSide(),
					event.getPlayer(), event.getStack()))
				event.setCanPlaceBlockOnSide(true);
		}
	}

	@SubscribeEvent
	public void onItemBlockUse(ItemBlockUseEvent event) {
		Material mat = MaterialRegistry.getMaterialForStack(event.getStack());
		IBlockState state = event.getWorld().getBlockState(event.getPlaceParams().pos);
		for (IBlockPlacementHandler handler : getHandlers()) {
			if (handler.isHandled(event.getStack())) {
				event.setActionResult(EnumActionResult.FAIL);
				EnumActionResult result =
						handler.place(event.getWorld(), event.getStack(), event.getPlayer(), mat, state,
								event.getPlaceParams());
				if (result != EnumActionResult.PASS) {
					event.setCanceled(true);
					event.setActionResult(result);
					break;
				}
			}
		}
	}

	private static class PlacementOverrideEntry {
		public int priority;
		public IBlockPlacementHandler handler;

		public PlacementOverrideEntry(int priority, IBlockPlacementHandler handler) {
			this.priority = priority;
			this.handler = handler;
		}
	}
}
