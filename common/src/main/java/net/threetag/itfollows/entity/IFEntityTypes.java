package net.threetag.itfollows.entity;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.client.renderer.entity.TheEntityRenderer;

import java.util.function.Supplier;

public class IFEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ItFollows.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<TheEntity>> THE_ENTITY = register("the_entity",() -> EntityType.Builder.<TheEntity>of(TheEntity::new, MobCategory.MISC).noLootTable().clientTrackingRange(8).sized(0.6F, 1.8F));

    public static void init() {
        EntityAttributeRegistry.register(THE_ENTITY, Mob::createMobAttributes);
    }

    @Environment(EnvType.CLIENT)
    public static void initRenderers() {
        EntityRendererRegistry.register(THE_ENTITY, TheEntityRenderer::new);
    }

    private static <T extends Entity> RegistrySupplier<EntityType<T>> register(String id, Supplier<EntityType.Builder<T>> builderSupplier) {
        return ENTITY_TYPES.register(id, () -> builderSupplier.get().build(ResourceKey.create(Registries.ENTITY_TYPE, ItFollows.id(id))));
    }

}
