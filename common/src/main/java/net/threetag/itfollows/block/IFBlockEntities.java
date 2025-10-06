package net.threetag.itfollows.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.threetag.itfollows.ItFollows;

import java.util.Set;

public class IFBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ItFollows.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final DeferredSupplier<BlockEntityType<OminousPotBlockEntity>> OMINOUS_POT = BLOCK_ENTITIES.register("ominous_pot", () ->
            new BlockEntityType<>(OminousPotBlockEntity::new, Set.of(IFBlocks.OMINOUS_POT.get()))
    );

}
