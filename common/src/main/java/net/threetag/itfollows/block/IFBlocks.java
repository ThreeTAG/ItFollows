package net.threetag.itfollows.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.threetag.itfollows.ItFollows;

public class IFBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ItFollows.MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<Block> OMINOUS_POT = BLOCKS.register("ominous_pot", () ->
            new OminousPotBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, ItFollows.id("ominous_pot")))
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .strength(0.0F, 0.0F)
                    .pushReaction(PushReaction.DESTROY)
                    .noOcclusion()
                    .noLootTable())
    );

}
