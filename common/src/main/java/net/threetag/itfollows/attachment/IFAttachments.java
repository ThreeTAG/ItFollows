package net.threetag.itfollows.attachment;

import com.mojang.serialization.Codec;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.threetag.itfollows.entity.disguise.DisguiseType;

import java.util.UUID;
import java.util.function.Supplier;

public class IFAttachments {

    public static final AttachmentType<DisguiseType> DISGUISE_TYPE = register("disguise_type", () -> null, DisguiseType.CODEC, DisguiseType.STREAM_CODEC);
    public static final AttachmentType<UUID> TARGET_ID = register("target_id", () -> null, UUIDUtil.CODEC, UUIDUtil.STREAM_CODEC);
    public static final AttachmentType<UUID> TARGET_INFECTOR_ID = register("target_infector_id", () -> null, UUIDUtil.CODEC, UUIDUtil.STREAM_CODEC);

    @ExpectPlatform
    public static <T> IFAttachments.AttachmentType<T> register(String id, Supplier<T> defaultValue, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T> void set(Entity entity, AttachmentType<T> attachmentType, T value) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T> T get(Entity entity, AttachmentType<T> attachmentType) {
        throw new AssertionError();
    }

    public static abstract class AttachmentType<T> {

        public abstract Object getPlatformType();

        public void set(Entity entity, T value) {
            IFAttachments.set(entity, this, value);
        }

        public T get(Entity entity) {
            return IFAttachments.get(entity, this);
        }

    }

}
