package dev.buildtool.kurretsfabric.screenhandlers;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.turrets.CobbleTurret;
import dev.buildtool.satako.gui.BetterScreenHandler;
import dev.buildtool.satako.gui.BetterSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.ItemTags;

public class CobbleScreenHandler extends BetterScreenHandler {
    public CobbleScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
        super(KTurrets.COBBLE_TURRET_HANDLER, syncId);
        CobbleTurret cobbleTurret = (CobbleTurret) playerInventory.player.world.getEntityById(packetByteBuf.readInt());
        int index = 0;
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 9; k++) {
                addSlot(new BetterSlot(cobbleTurret.ammo, index++, k * 18, j * 18));
            }
        }
        addPlayerInventory(0, 4 * 18, playerInventory);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack stack = getSlot(index).getStack();
        if (index > 26) {
            if (stack.streamTags().anyMatch(itemTagKey -> itemTagKey.id().equals(ItemTags.STONE_TOOL_MATERIALS.id())) && !insertItem(stack, 0, 27, false))
                return ItemStack.EMPTY;
        } else if (!insertItem(stack, 27, 63, false))
            return ItemStack.EMPTY;
        return super.transferSlot(player, index);
    }
}
