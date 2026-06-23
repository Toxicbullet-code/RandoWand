package com.randowand.main;

import com.mojang.logging.LogUtils;
import com.randowand.main.Wand.RandoWandItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import static net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion.MOD_ID;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(RandoWand.MODID)
public class RandoWand {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "randowand";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);



    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RANDOWAND_TAB =
            CREATIVE_MODE_TABS.register("randowand_tab",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.randowand"))
                            .icon(() -> new ItemStack(ModItems.RANDOM_WAND.get()))
                            .displayItems((parameters, output) -> {
                                         output.accept(ModItems.RANDOM_WAND.get());

                            })
                            .build());






    public RandoWand(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ModItems.ITEMS.register(modEventBus);
        ModItems.COMPONENTS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

      ;

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }





    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
