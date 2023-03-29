package dev.buildtool.kurretsfabric.screenhandlers;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.drones.BrickDrone;
import dev.buildtool.satako.gui.BetterScreenHandler;
import dev.buildtool.satako.gui.BetterSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;

public class BrickDroneScreenHandler extends BetterScreenHandler {
    public BrickDroneScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf packetByteBuf) {
        super(KTurrets.BRICK_DRONE_HANDLER, syncId);
        BrickDrone brickDrone = (BrickDrone) inventory.player.world.getEntityById(packetByteBuf.readInt());
        int index = 0;
        for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 9; k++) {
                addSlot(new BetterSlot(brickDrone.ammo, index++, k * 18, j * 18));
            }
        }

        addPlayerInventory(0, 4 * 18, inventory);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack stack = getSlot(index).getStack();
        if (index > 17) {
            if ((stack.getItem() == Items.BRICK || stack.getItem() == Items.NETHER_BRICK) && !insertItem(stack, 0, 18, false))
                return ItemStack.EMPTY;
        } else if (!insertItem(stack, 18, 54, false)) {
            return ItemStack.EMPTY;
        }
        return super.transferSlot(player, index);
    }
}
