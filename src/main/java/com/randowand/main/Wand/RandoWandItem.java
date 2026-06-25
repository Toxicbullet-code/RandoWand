package com.randowand.main.Wand;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import java.util.ArrayList;
import java.util.List;
import static com.randowand.main.ModItems.WAND_PALETTE;

public class RandoWandItem extends Item {
    public RandoWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        ItemStack wand = context.getItemInHand();

        if (player == null) return InteractionResult.PASS;

        // 1. SHIFT + RIGHT CLICK (Adding or Removing)
        if (player.isShiftKeyDown()) {
            ItemStack offhand = player.getOffhandItem();

            // Adding blocks: Now consumes in BOTH Creative and Survival
            if (!offhand.isEmpty() && offhand.getItem() instanceof BlockItem) {
                List<ItemStack> list = new ArrayList<>(wand.getOrDefault(WAND_PALETTE, List.of()));

                ItemStack toStore = offhand.copy();
                // Always shrink the offhand, regardless of Creative mode
                offhand.shrink(offhand.getCount());

                list.add(toStore);
                wand.set(WAND_PALETTE, list);
                player.displayClientMessage(Component.literal("Stored stack!"), true);
                return InteractionResult.SUCCESS;
            }
            // Removing blocks: Enabled for all
            else if (offhand.isEmpty()) {
                List<ItemStack> palette = new ArrayList<>(wand.getOrDefault(WAND_PALETTE, List.of()));
                if (!palette.isEmpty()) {
                    ItemStack removedStack = palette.remove(palette.size() - 1);
                    wand.set(WAND_PALETTE, palette);

                    if (!player.getInventory().add(removedStack)) {
                        player.drop(removedStack, false);
                    }

                    player.displayClientMessage(Component.literal("Removed: " + removedStack.getHoverName().getString()), true);
                    return InteractionResult.SUCCESS;
                } else {
                    player.displayClientMessage(Component.literal("Wand is already empty!"), true);
                    return InteractionResult.FAIL;
                }
            }
            return InteractionResult.PASS;
        }

        // 2. PLACING: Normal Right Click
        List<ItemStack> palette = new ArrayList<>(wand.getOrDefault(WAND_PALETTE, List.of()));
        if (!palette.isEmpty()) {
            int index = context.getLevel().getRandom().nextInt(palette.size());
            ItemStack toPlace = palette.get(index);

            if (toPlace.getItem() instanceof BlockItem block) {
                BlockHitResult hit = new BlockHitResult(
                        context.getClickLocation(),
                        context.getClickedFace(),
                        context.getClickedPos(),
                        false
                );

                UseOnContext newContext = new UseOnContext(
                        context.getLevel(),
                        player,
                        context.getHand(),
                        toPlace,
                        hit
                );

                InteractionResult res = block.useOn(newContext);

                // Consumption logic: Infinite in Creative
                if (res.consumesAction()) {
                    if (!player.isCreative()) {
                        toPlace.shrink(1);
                        if (toPlace.isEmpty()) {
                            palette.remove(index);
                        }
                        wand.set(WAND_PALETTE, palette);
                        wand.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    }
                    // If Creative, we simply do nothing to the palette, making it infinite
                }
                return res;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§e[Controls]§r"));
        tooltip.add(Component.literal("- Shift + Right-Click: Store/Undo"));
        tooltip.add(Component.literal("- Right-Click: Place random block"));
        tooltip.add(Component.literal("- Creative: Uses items to store, then infinite placement"));

        List<ItemStack> palette = stack.get(WAND_PALETTE);
        if (palette != null && !palette.isEmpty()) {
            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal("§6Contents:§r"));
            for (ItemStack s : palette) {
                tooltip.add(Component.literal("- " + s.getCount() + "x " + s.getHoverName().getString()));
            }
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}