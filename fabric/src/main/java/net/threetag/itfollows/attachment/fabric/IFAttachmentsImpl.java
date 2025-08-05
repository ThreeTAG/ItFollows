package net.threetag.itfollows.attachment.fabric;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.attachment.IFAttachments;

import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "UnstableApiUsage"})
public class IFAttachmentsImpl {

    public static <T> IFAttachments.AttachmentType<T> register(String id, Supplier<T> defaultValue, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return new TypeImpl<>(AttachmentRegistry.create(ItFollows.id(id), builder -> {
            builder.persistent(codec).syncWith(streamCodec, AttachmentSyncPredicate.all());
        }), defaultValue);
    }

    public static <T> void set(Entity entity, IFAttachments.AttachmentType<T> attachmentType, T value) {
        entity.setAttached((AttachmentType<T>) attachmentType.getPlatformType(), value);
    }

    public static <T> T get(Entity entity, IFAttachments.AttachmentType<T> attachmentType) {
        TypeImpl<T> type = (TypeImpl<T>) attachmentType;
        return entity.getAttachedOrGet((AttachmentType<T>) type.getPlatformType(), type.defaultValue);
    }

    private static class TypeImpl<T> extends IFAttachments.AttachmentType<T> {

        private final AttachmentType<T> attachmentType;
        private final Supplier<T> defaultValue;

        private TypeImpl(AttachmentType<T> attachmentType, Supplier<T> defaultValue) {
            this.attachmentType = attachmentType;
            this.defaultValue = defaultValue;
        }

        @Override
        public Object getPlatformType() {
            return this.attachmentType;
        }
    }

}
