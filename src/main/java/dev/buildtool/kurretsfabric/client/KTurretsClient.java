package dev.buildtool.kurretsfabric.client;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.client.models.ArrowTurretModel;
import dev.buildtool.kurretsfabric.client.screens.ArrowTurretScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class KTurretsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(KTurrets.ARROW_TURRET_HANDLER, ArrowTurretScreen::new);

        EntityModelLayer arrowTurretLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "arrow_turret"), "main");
        EntityRendererRegistry.register(KTurrets.ARROW_TURRET, ctx -> new EntityRenderer2<>(ctx, new ArrowTurretModel(ctx.getPart(arrowTurretLayer)), KTurrets.ID, "arrow_turret2", 0.4f));
        EntityModelLayerRegistry.registerModelLayer(arrowTurretLayer, ArrowTurretModel::getTexturedModelData);
    }
}
