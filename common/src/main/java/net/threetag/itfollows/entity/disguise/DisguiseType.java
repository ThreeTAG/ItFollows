package net.threetag.itfollows.entity.disguise;

import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.entity.TheEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class DisguiseType {

    public static final Registrar<DisguiseType> REGISTRY = RegistrarManager.get(ItFollows.MOD_ID).<DisguiseType>builder(ItFollows.id("disguise_type"))
            .syncToClients()
            .build();

    public static final Codec<DisguiseType> CODEC = ResourceLocation.CODEC.xmap(REGISTRY::get, REGISTRY::getId);
    public static final StreamCodec<ByteBuf, DisguiseType> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(REGISTRY::get, REGISTRY::getId);

    public abstract boolean isValid(TheEntity entity);

    public abstract int getWeight();

    public static DisguiseType findDisguise(TheEntity entity, RandomSource random) {
        List<DisguiseType> applicable = new ArrayList<>();

        for (DisguiseType disguiseType : REGISTRY) {
            if (disguiseType.isValid(entity)) {
                for (int i = 0; i < disguiseType.getWeight(); i++) {
                    applicable.add(disguiseType);
                }
            }
        }

        var currentDisguise = entity.getDisguiseType();
        var randomNew = applicable.isEmpty() ? null : applicable.get(random.nextInt(applicable.size()));

        if (currentDisguise == null) {
            return randomNew != null ? randomNew : DisguiseTypes.PIG.get();
        }

        if (randomNew == null) {
            return DisguiseTypes.PIG.get();
        }

        if (applicable.contains(currentDisguise)) {
            return currentDisguise;
        } else {
            return randomNew;
        }
    }

}