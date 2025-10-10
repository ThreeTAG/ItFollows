package net.threetag.itfollows.mixin.client;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.server.packs.resources.ResourceManager;
import net.threetag.itfollows.client.renderer.disguise.DisguiseRendererRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Inject(method = "onResourceManagerReload", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onResourceManagerReload(ResourceManager resourceManager, CallbackInfo ci,  EntityRendererProvider.Context context) {
        DisguiseRendererRegistry.reload(context);
    }

}
