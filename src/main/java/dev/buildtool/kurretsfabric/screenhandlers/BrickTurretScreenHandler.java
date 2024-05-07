package dev.buildtool.kurretsfabric.screenhandlers;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.turrets.BrickTurret;
import dev.buildtool.satako.gui.BetterScreenHandler;
import dev.buildtool.satako.gui.BetterSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;

public class BrickTurretScreenHandler extends BetterScreenHandler {
    public BrickTurretScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf packetByteBuf) {
        super(KTurrets.BRICK_TURRET_HANDLER, syncId);
        BrickTurret brickTurret = (BrickTurret) inventory.player.world.getEntityById(packetByteBuf.readInt());
        int index = 0;
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 9; k++) {
                addSlot(new BetterSlot(brickTurret.bricks, index++, k * 18, j * 18) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return stack.isOf(Items.BRICK) || stack.isOf(Items.NETHER_BRICK);
                    }
                });
            }
        }

        addPlayerInventory(0, 4 * 18, inventory);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack stack = getSlot(index).getStack();
        if (index > 26) {
            if ((stack.getItem() == Items.BRICK || stack.getItem() == Items.NETHER_BRICK) && !insertItem(stack, 0, 27, false))
                return ItemStack.EMPTY;
        } else if (!insertItem(stack, 27, 63, false)) {
            return ItemStack.EMPTY;
        }
        return super.transferSlot(player, index);
    }
}
