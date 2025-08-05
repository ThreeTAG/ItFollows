package net.threetag.itfollows.attachment.neoforge;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.attachment.IFAttachments;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class IFAttachmentsImpl {

    public static final DeferredRegister<net.neoforged.neoforge.attachment.AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ItFollows.MOD_ID);

    public static <T> IFAttachments.AttachmentType<T> register(String id, Supplier<T> defaultValue, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return new TypeImpl<>(ATTACHMENT_TYPES.register(id, () -> {
            net.neoforged.neoforge.attachment.AttachmentType.Builder<T> builder =
                    net.neoforged.neoforge.attachment.AttachmentType.builder(defaultValue)
                            .serialize(codec.fieldOf(id))
                            .sync(streamCodec);
            return builder.build();
        }));
    }

    public static <T> void set(Entity entity, IFAttachments.AttachmentType<T> attachmentType, T value) {
        entity.setData((net.neoforged.neoforge.attachment.AttachmentType<T>) attachmentType.getPlatformType(), value);
    }

    public static <T> T get(Entity entity, IFAttachments.AttachmentType<T> attachmentType) {
        return entity.getData((net.neoforged.neoforge.attachment.AttachmentType<T>) attachmentType.getPlatformType());
    }

    private static class TypeImpl<T> extends IFAttachments.AttachmentType<T> {

        private final DeferredHolder<net.neoforged.neoforge.attachment.AttachmentType<?>, net.neoforged.neoforge.attachment.AttachmentType<T>> holder;

        public TypeImpl(DeferredHolder<net.neoforged.neoforge.attachment.AttachmentType<?>, net.neoforged.neoforge.attachment.AttachmentType<T>> holder) {
            this.holder = holder;
        }

        @Override
        public Object getPlatformType() {
            return this.holder.get();
        }
    }

}
