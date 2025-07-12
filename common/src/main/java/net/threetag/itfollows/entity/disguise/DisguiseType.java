package net.threetag.itfollows.entity.disguise;

import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.entity.TheEntity;

public abstract class DisguiseType {

    public static final Registrar<DisguiseType> REGISTRY = RegistrarManager.get(ItFollows.MOD_ID).<DisguiseType>builder(ItFollows.id("disguise_type"))
            .syncToClients()
            .build();

    public static final StreamCodec<ByteBuf, DisguiseType> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(REGISTRY::get, REGISTRY::getId);
    public static final EntityDataSerializer<DisguiseType> ENTITY_DATA = EntityDataSerializer.forValueType(STREAM_CODEC);

    static {
        if (Platform.isNeoForge()) {
            EntityDataSerializers.registerSerializer(ENTITY_DATA);
        }
    }

    public abstract boolean isValid(TheEntity entity);

    public abstract int getPriority();

}