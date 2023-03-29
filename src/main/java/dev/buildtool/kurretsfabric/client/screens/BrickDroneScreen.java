package dev.buildtool.kurretsfabric.client.screens;

import dev.buildtool.kurretsfabric.screenhandlers.BrickDroneScreenHandler;
import dev.buildtool.satako.gui.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class BrickDroneScreen extends InventoryScreen<BrickDroneScreenHandler> {
    public BrickDroneScreen(BrickDroneScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text, true);
    }
}
