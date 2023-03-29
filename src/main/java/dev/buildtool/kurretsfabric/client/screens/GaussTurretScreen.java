package dev.buildtool.kurretsfabric.client.screens;

import dev.buildtool.kurretsfabric.screenhandlers.GaussTurretScreenHandler;
import dev.buildtool.satako.gui.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class GaussTurretScreen extends InventoryScreen<GaussTurretScreenHandler> {
    public GaussTurretScreen(GaussTurretScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text, true);
    }
}
