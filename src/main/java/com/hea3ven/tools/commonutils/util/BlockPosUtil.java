package com.hea3ven.tools.commonutils.util;

import java.util.Iterator;

import com.google.common.collect.AbstractIterator;

import net.minecraft.util.BlockPos;

public class BlockPosUtil {

	public static Iterable<ModifiableBlockPos> getBox(final BlockPos posStart, int xOff, int yOff,
			int zOff) {
		return getBox(posStart, posStart.add(xOff - 1, yOff - 1, zOff - 1));
	}

	public static Iterable<ModifiableBlockPos> getBox(final BlockPos posStart,
			final BlockPos posEnd) {
		return new Iterable<ModifiableBlockPos>() {

			@Override
			public Iterator<ModifiableBlockPos> iterator() {
				return new AbstractIterator<ModifiableBlockPos>() {
					private int currX = posStart.getX();
					private int currY = posStart.getY();
					private int currZ = posStart.getZ();

					private ModifiableBlockPos pos;

					@Override
					protected ModifiableBlockPos computeNext() {
						if (pos == null)
							return (pos = new ModifiableBlockPos(posStart));
						if (currX == posEnd.getX() && currY == posEnd.getY()
								&& currZ == posEnd.getZ())
							return this.endOfData();

						if (currX < posEnd.getX())
							currX++;
						else if (currZ < posEnd.getZ()) {
							currX = posStart.getX();
							currZ++;
						} else if (currY < posEnd.getY()) {
							currX = posStart.getX();
							currZ = posStart.getZ();
							currY++;
						}
						pos.set(currX, currY, currZ);
						return pos;
					}
				};
			}
		};
	}

}
