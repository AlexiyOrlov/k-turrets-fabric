package dev.buildtool.kurretsfabric.screenhandlers;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.drones.GaussDrone;
import dev.buildtool.satako.gui.BetterScreenHandler;
import dev.buildtool.satako.gui.BetterSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class GaussDroneScreenHandler extends BetterScreenHandler {
    public GaussDroneScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf byteBuf) {
        super(KTurrets.GAUSS_DRONE_HANDLER, syncId);
        GaussDrone gaussDrone = (GaussDrone) inventory.player.world.getEntityById(byteBuf.readInt());
        int ind = 0;
        for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 9; k++) {
                addSlot(new BetterSlot(gaussDrone.ammo, ind++, k * 18, j * 18) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return stack.isOf(KTurrets.gaussBullet);
                    }
                });
            }
        }

        addPlayerInventory(0, 4 * 18, inventory);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack stack = getSlot(index).getStack();
        if (stack.isOf(KTurrets.gaussBullet) && index > 17) {
            if (!insertItem(stack, 0, 18, false))
                return ItemStack.EMPTY;

        } else if (index < 18) {
            if (!insertItem(stack, 18, 54, false))
                return ItemStack.EMPTY;
        }
        return super.transferSlot(player, index);
    }
}
