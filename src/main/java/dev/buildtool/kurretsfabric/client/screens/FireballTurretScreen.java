package dev.buildtool.kurretsfabric.client.screens;

import dev.buildtool.kurretsfabric.screenhandlers.FireChargeTurretScreenHandler;
import dev.buildtool.satako.gui.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class FireballTurretScreen extends InventoryScreen<FireChargeTurretScreenHandler> {
    public FireballTurretScreen(FireChargeTurretScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text, true);
    }
}
