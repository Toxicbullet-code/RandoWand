package com.randowand.main;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(RandoWand.MODID)
public class RandoWand {
    public static final String MODID = "randowand";
    public static final Logger LOGGER = LogUtils.getLogger();


    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RANDOWAND_TAB =
            CREATIVE_MODE_TABS.register("randowand_tab",
                    () -> CreativeModeTab.builder()

                            .title(Component.translatable("itemGroup.randowand"))

                            .icon(() -> new ItemStack(ModItems.RANDO_WAND.get()))

                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.RANDO_WAND.get());
                            })
                            .build());

    public RandoWand(IEventBus modEventBus, ModContainer container) {

        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);



        ModItems.ITEMS.register(modEventBus);

        ModItems.COMPONENTS.register(modEventBus);

        CREATIVE_MODE_TABS.register(modEventBus);


        modEventBus.addListener(Config::onLoad);


    }
}