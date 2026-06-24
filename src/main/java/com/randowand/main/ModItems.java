package com.randowand.main;

import com.randowand.main.Wand.RandoWandItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.List;

public class ModItems {
    // Standard Registry for Items
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, RandoWand.MODID);

    // Registry for Data Components
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, RandoWand.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<net.minecraft.world.item.ItemStack>>> WAND_PALETTE =

            COMPONENTS.register("wand_palette", () -> DataComponentType.<List<net.minecraft.world.item.ItemStack>>builder()

                    .persistent(com.mojang.serialization.Codec.list(net.minecraft.world.item.ItemStack.CODEC))

                    .networkSynchronized(net.minecraft.network.codec.StreamCodec.composite(

                            net.minecraft.world.item.ItemStack.
                                    STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs.list()),
                            i -> i, i -> i))

                    .build());

    public static final DeferredHolder<Item, Item> RANDO_WAND = ITEMS.register("randowand",
            () -> new RandoWandItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(1000) // Set your desired durability here
                    .component(WAND_PALETTE, List.of())));
}