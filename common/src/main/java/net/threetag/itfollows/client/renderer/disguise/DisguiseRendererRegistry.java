package net.threetag.itfollows.client.renderer.disguise;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.threetag.itfollows.entity.disguise.DisguiseType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DisguiseRendererRegistry {

    private static final Map<Supplier<? extends DisguiseType>, DisguiseRendererProvider> RENDERER_PROVIDER_MAP = new HashMap<>();
    private static final Map<DisguiseType, DisguiseRenderer<?, ?>> RENDERER_MAP = new HashMap<>();

    public static void register(Supplier<? extends DisguiseType> disguise, DisguiseRendererProvider rendererProvider) {
        RENDERER_PROVIDER_MAP.put(disguise, rendererProvider);
    }

    @SuppressWarnings("unchecked")
    public static <S extends LivingEntityRenderState, M extends EntityModel<? super S>> DisguiseRenderer<S, M> getRenderer(DisguiseType disguiseType) {
        return (DisguiseRenderer<S, M>) RENDERER_MAP.get(disguiseType);
    }

    public static void reload(EntityRendererProvider.Context context) {
        RENDERER_MAP.clear();

        RENDERER_PROVIDER_MAP.forEach((supplier, disguiseRendererProvider) -> {
            RENDERER_MAP.put(supplier.get(), disguiseRendererProvider.create(context));
        });
    }

    public interface DisguiseRendererProvider {

        DisguiseRenderer<?, ?> create(EntityRendererProvider.Context context);

    }

}
