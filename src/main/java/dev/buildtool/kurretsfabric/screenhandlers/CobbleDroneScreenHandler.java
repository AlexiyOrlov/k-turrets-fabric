package dev.buildtool.kurretsfabric.screenhandlers;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.drones.CobbleDrone;
import dev.buildtool.satako.gui.BetterScreenHandler;
import dev.buildtool.satako.gui.BetterSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.ItemTags;

public class CobbleDroneScreenHandler extends BetterScreenHandler {
    public CobbleDroneScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(KTurrets.COBBLE_TURRET_HANDLER, syncId);
        CobbleDrone cobbleDrone = (CobbleDrone) playerInventory.player.world.getEntityById(buf.readInt());
        int index = 0;
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 9; k++) {
                addSlot(new BetterSlot(cobbleDrone.ammo, index++, k * 18, j * 18));
            }
        }
        addPlayerInventory(0, 4 * 18, playerInventory);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack stack = getSlot(index).getStack();
        if (index > 17) {
            if (stack.streamTags().anyMatch(itemTagKey -> itemTagKey.id().equals(ItemTags.STONE_TOOL_MATERIALS.id())) && !insertItem(stack, 0, 18, false))
                return ItemStack.EMPTY;
        } else if (!insertItem(stack, 18, 54, false))
            return ItemStack.EMPTY;
        return super.transferSlot(player, index);
    }
}
