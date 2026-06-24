package com.randowand.main;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // 1. Config option for Max Capacity
    public static final ModConfigSpec.IntValue MAX_PALETTE_SIZE = BUILDER

            .comment("The maximum number of unique block stacks the wand can hold at one time.")

            .defineInRange("maxPaletteSize", 9, 1, 54);

    // 2. Config option for Break Behavior
    public static final ModConfigSpec.BooleanValue DROP_ITEMS_ON_BREAK = BUILDER

            .comment("Should the wand drop its stored items safely on the ground when its durability runs out?")

            .define("dropItemsOnBreak", true);

    static final ModConfigSpec SPEC = BUILDER.build();


    static void onLoad(final ModConfigEvent event) {
        // This fires automatically when the config file loads or updates
    }
}