package net.threetag.itfollows.item;

import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.level.Level;
import net.threetag.itfollows.entity.ThrownSplashPotionOfSpreading;
import org.jetbrains.annotations.NotNull;

public class SplashPotionOfSpreadingItem extends SplashPotionItem {

    public SplashPotionOfSpreadingItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        return new ItemStack(this);
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        return stack.getComponents().getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY);
    }

    @Override
    protected @NotNull AbstractThrownPotion createPotion(Level level, Position position, ItemStack stack) {
        return new ThrownSplashPotionOfSpreading(level, position.x(), position.y(), position.z(), stack);
    }

    @Override
    protected @NotNull AbstractThrownPotion createPotion(ServerLevel level, LivingEntity entity, ItemStack stack) {
        return new ThrownSplashPotionOfSpreading(level, entity, stack);
    }
}
