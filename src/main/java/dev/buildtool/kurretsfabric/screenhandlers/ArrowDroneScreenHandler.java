package dev.buildtool.kurretsfabric.screenhandlers;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.drones.ArrowDrone;
import dev.buildtool.satako.gui.BetterScreenHandler;
import dev.buildtool.satako.gui.BetterSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class ArrowDroneScreenHandler extends BetterScreenHandler {
    public ArrowDroneScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        super(KTurrets.ARROW_DRONE_HANDLER, syncId);
        ArrowDrone arrowDrone = (ArrowDrone) inventory.player.world.getEntityById(buf.readInt());
        addSlot(new BetterSlot(arrowDrone.weapon, 0, 4 * 18, 0));
        int slot = 0;
        for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 9; k++) {
                addSlot(new BetterSlot(arrowDrone.ammo, slot++, k * 18, j * 18 + 18 * 2) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return stack.getItem() instanceof ArrowItem;
                    }
                });
            }
        }
        addPlayerInventory(0, 6 * 18, inventory);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = getSlot(index).getStack();
        if (index > 18) {
            if ((itemStack.getItem() instanceof BowItem || itemStack.getItem() instanceof CrossbowItem) && !this.insertItem(itemStack, 0, 1, false))
                return ItemStack.EMPTY;
            else if (itemStack.getItem() instanceof ArrowItem && !insertItem(itemStack, 1, 19, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!insertItem(itemStack, 19, 55, false))
            return ItemStack.EMPTY;
        return super.transferSlot(player, index);
    }
}
