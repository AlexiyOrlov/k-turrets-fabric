package dev.buildtool.kurretsfabric.screenhandlers;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.turrets.GaussTurret;
import dev.buildtool.satako.gui.BetterScreenHandler;
import dev.buildtool.satako.gui.BetterSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class GaussTurretScreenHandler extends BetterScreenHandler {
    public GaussTurretScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        super(KTurrets.GAUSS_TURRET_HANDLER, syncId);
        GaussTurret gaussTurret = (GaussTurret) inventory.player.world.getEntityById(buf.readInt());
        int ind = 0;
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 9; k++) {
                addSlot(new BetterSlot(gaussTurret.ammo, ind++, k * 18, j * 18) {
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
        if (stack.isOf(KTurrets.gaussBullet) && index > 26) {
            if (!insertItem(stack, 0, 27, false))
                return ItemStack.EMPTY;

        } else if (index < 27) {
            if (!insertItem(stack, 27, 63, false))
                return ItemStack.EMPTY;
        }
        return super.transferSlot(player, index);
    }
}
