package com.dylangolan3sys.heavycoredetector;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HeavyCoreDetectorClient implements ClientModInitializer {
    private static final int SCAN_RADIUS = 16;
    private static final long TICK_COOLDOWN = 1L; // 1 tick minimum between checks per vault
    private long lastCheckTick = 0;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(MinecraftClient client) {
        if (client.world == null || client.player == null) {
            return;
        }

        // Minimal cooldown - only prevent exact same tick
        long currentTick = client.world.getTime();
        if (currentTick == lastCheckTick) {
            return;
        }
        lastCheckTick = currentTick;

        World world = client.world;
        BlockPos playerPos = client.player.getBlockPos();

        // Scan nearby vaults
        for (int x = playerPos.getX() - SCAN_RADIUS; x <= playerPos.getX() + SCAN_RADIUS; x++) {
            for (int y = playerPos.getY() - SCAN_RADIUS; y <= playerPos.getY() + SCAN_RADIUS; y++) {
                for (int z = playerPos.getZ() - SCAN_RADIUS; z <= playerPos.getZ() + SCAN_RADIUS; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockEntity blockEntity = world.getBlockEntity(pos);

                    if (blockEntity != null && 
                        world.getBlockState(pos).getBlock() == Blocks.VAULT && 
                        hasHeavyCore(blockEntity)) {
                        
                        tryOpenVault(client, pos);
                    }
                }
            }
        }
    }

    private boolean hasHeavyCore(BlockEntity blockEntity) {
        try {
            NbtCompound tag = blockEntity.createNbt();
            if (tag.contains("items")) {
                NbtList itemsList = tag.getList("items", 10); // 10 = compound tag
                for (int i = 0; i < itemsList.size(); i++) {
                    NbtCompound itemTag = itemsList.getCompound(i);
                    ItemStack stack = ItemStack.fromNbt(itemTag);
                    if (!stack.isEmpty() && stack.getItem() == Items.HEAVY_CORE) {
                        HeavyCoreDetectorMod.LOGGER.info("Heavy Core detected!");
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // Silently handle errors
        }
        return false;
    }

    private void tryOpenVault(MinecraftClient client, BlockPos pos) {
        if (client.interactionManager != null && client.player != null) {
            client.interactionManager.interactBlock(client.player, net.minecraft.util.Hand.MAIN_HAND, 
                new net.minecraft.util.hit.BlockHitResult(
                    net.minecraft.util.math.Vec3d.ofCenter(pos),
                    net.minecraft.util.Direction.UP,
                    pos,
                    false
                ));
        }
    }
