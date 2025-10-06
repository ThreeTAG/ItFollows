package net.threetag.itfollows.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.entity.CursePlayerHandler;

public class OminousPotBlockEntity extends RandomizableContainerBlockEntity {

    public static final ResourceKey<LootTable> DEFAULT_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ItFollows.id("blocks/ominous_pot"));
    public static final Component CONTAINER_TITLE = Component.translatable(Util.makeDescriptionId("container", ItFollows.id("ominous_pot")));
    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

    protected OminousPotBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IFBlockEntities.OMINOUS_POT.get(), blockPos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return CONTAINER_TITLE;
    }

    private ItemStack getNextItem() {
        for (int i = 0; i < this.items.size(); i++) {
            if (!this.items.get(i).isEmpty()) {
                ItemStack itemStack = this.items.get(i);
                this.items.set(i, ItemStack.EMPTY);
                this.setChanged();
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public int givePlayerItem(ServerPlayer player, int usage) {
        var curseHandler = CursePlayerHandler.get(player);

        if (this.level != null && !this.level.isClientSide() && !curseHandler.isCurseActive()) {
            usage++;
            ItemStack itemStack = this.getNextItem();
            if (!itemStack.isEmpty()) {
                Containers.dropItemStack(this.level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1, this.worldPosition.getZ() + 0.5, itemStack);
                this.setChanged();
                float chance = 0.1F + (usage - 1) * 0.4F;

                if (this.level.random.nextFloat() < chance) {
                    CursePlayerHandler.get(player).startCurseFresh();
                    this.clearContent();
                    return 3;
                }

                return Math.min(3, usage);
            }
        }

        return usage;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (!this.trySaveLootTable(output)) {
            ContainerHelper.saveAllItems(output, this.items);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(input)) {
            ContainerHelper.loadAllItems(input, this.items);
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }
}
