package dev.buildtool.kurretsfabric.screenhandlers;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.turrets.BulletTurret;
import dev.buildtool.satako.gui.BetterScreenHandler;
import dev.buildtool.satako.gui.BetterSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;

public class BulletTurretScreenHandler extends BetterScreenHandler {
    public BulletTurretScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf packetByteBuf) {
        super(KTurrets.BULLET_TURRET_HANDLER, syncId);
        BulletTurret bulletTurret = (BulletTurret) inventory.player.world.getEntityById(packetByteBuf.readInt());
        int index = 0;
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 9; k++) {
                addSlot(new BetterSlot(bulletTurret.ammo, index++, k * 18, j * 18));
            }
        }
        addPlayerInventory(0, 4 * 18, inventory);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack stack = getSlot(index).getStack();
        if (index > 26) {
            if ((stack.getItem() == Items.GOLD_NUGGET || stack.getItem() == Items.IRON_NUGGET) && !insertItem(stack, 0, 27, false))
                return ItemStack.EMPTY;
        } else if (!insertItem(stack, 27, 63, false)) {
            return ItemStack.EMPTY;
        }
        return super.transferSlot(player, index);
    }
}
