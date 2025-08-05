package net.threetag.itfollows.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.threetag.itfollows.entity.CursePlayerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.level.ServerPlayer;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements CursePlayerHandler.Curseable {

    @Unique
    private CursePlayerHandler it_follows$cursePlayerHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        this.it_follows$cursePlayerHandler = new CursePlayerHandler((ServerPlayer) (Object) this);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void read(ValueInput valueInput, CallbackInfo ci) {
        this.it_follows$cursePlayerHandler.read(valueInput.childOrEmpty("it_follows:curse_handler"));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void write(ValueOutput valueOutput, CallbackInfo ci) {
        this.it_follows$cursePlayerHandler.write(valueOutput.child("it_follows:curse_handler"));
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
