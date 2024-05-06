package dev.buildtool.kurretsfabric.screenhandlers;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.turrets.FireChargeTurret;
import dev.buildtool.satako.gui.BetterScreenHandler;
import dev.buildtool.satako.gui.BetterSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class FireChargeTurretScreenHandler extends BetterScreenHandler {
    public FireChargeTurretScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf byteBuf) {
        super(KTurrets.FIRE_CHARGE_TURRET_HANDLER, syncId);
        FireChargeTurret firechargeTurret = (FireChargeTurret) inventory.player.world.getEntityById(byteBuf.readInt());
        int index = 0;
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 9; k++) {
                addSlot(new BetterSlot(firechargeTurret.ammo, index++, k * 18, j * 18));
            }
        }
        addPlayerInventory(0, 4 * 18, inventory);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack stack = getSlot(index).getStack();
        if (index > 26) {
            if (stack.isOf(KTurrets.explosivePowder) && !insertItem(stack, 0, 27, false))
                return ItemStack.EMPTY;
            else if (!insertItem(stack, 27, 63, false)) {
                return ItemStack.EMPTY;
            }
        }
        return super.transferSlot(player, index);
    }
}
