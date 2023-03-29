package dev.buildtool.kurretsfabric.client.screens;

import dev.buildtool.kurretsfabric.screenhandlers.BrickTurretScreenHandler;
import dev.buildtool.satako.gui.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class BrickTurretScreen extends InventoryScreen<BrickTurretScreenHandler> {
    public BrickTurretScreen(BrickTurretScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text, true);
    }
}
