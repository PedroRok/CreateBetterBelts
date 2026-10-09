package com.pedrorok.createBetterBelt.content.belt;

import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;

/**
 * @author Rok, Pedro Lucas nmm. 08/10/2026
 * @project create-better-belt
 */
public interface ReversibleBelt {

	boolean cbb$isReversed();

	void cbb$setReversed(boolean reversed);
}
