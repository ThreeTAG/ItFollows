package net.threetag.itfollows.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.threetag.itfollows.entity.CursePlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements CursePlayerHandler.Curseable {

    @Unique
    private CursePlayerHandler it_follows$cursePlayerHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        this.it_follows$cursePlayerHandler = new CursePlayerHandler((ServerPlayer) (Object) this);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void read(CompoundTag compoundTag, CallbackInfo ci) {
        var nbt = compoundTag.getCompoundOrEmpty("it_follows:curse_handler");
        this.it_follows$cursePlayerHandler.read(nbt);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void write(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.put("it_follows:curse_handler", this.it_follows$cursePlayerHandler.write());
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        this.it_follows$cursePlayerHandler.tick();
    }

    @Override
    public CursePlayerHandler it_follows$getCurseHandler() {
        return this.it_follows$cursePlayerHandler;
    }
}
