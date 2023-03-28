package dev.buildtool.kurretsfabric.screenhandlers;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.turrets.ArrowTurret;
import dev.buildtool.satako.gui.BetterScreenHandler;
import dev.buildtool.satako.gui.BetterSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.Slot;

public class ArrowTurretScreenHandler extends BetterScreenHandler {

    public ArrowTurretScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(KTurrets.ARROW_TURRET_HANDLER, syncId);
        ArrowTurret arrowTurret = (ArrowTurret) playerInventory.player.world.getEntityById(buf.readInt());
        addSlot(new BetterSlot(arrowTurret.weapon, 0, 4 * 18, 0));
        int slot = 0;
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 9; k++) {
                addSlot(new Slot(arrowTurret.ammo, slot++, k * 18, j * 18 + 18 * 2));
            }
        }
        addPlayerInventory(0, 5 * 18, playerInventory);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = getSlot(index).getStack();
        if (index > 27) {
            if ((itemStack.getItem() instanceof BowItem || itemStack.getItem() instanceof CrossbowItem) && !this.insertItem(itemStack, 0, 1, false))
                return ItemStack.EMPTY;
            else if (itemStack.getItem() instanceof ArrowItem && !insertItem(itemStack, 1, 28, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!insertItem(itemStack, 28, 64, false))
            return ItemStack.EMPTY;
        return super.transferSlot(player, index);
    }
}
