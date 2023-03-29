package dev.buildtool.kurretsfabric.client;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.client.models.ArrowTurretModel;
import dev.buildtool.kurretsfabric.client.models.BrickModel;
import dev.buildtool.kurretsfabric.client.models.BrickTurretModel;
import dev.buildtool.kurretsfabric.client.models.BulletModel;
import dev.buildtool.kurretsfabric.client.screens.ArrowTurretScreen;
import dev.buildtool.kurretsfabric.client.screens.BrickTurretScreen;
import dev.buildtool.kurretsfabric.client.screens.BulletTurretScreen;
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
        HandledScreens.register(KTurrets.BRICK_TURRET_HANDLER, BrickTurretScreen::new);
        HandledScreens.register(KTurrets.BULLET_TURRET_HANDLER, BulletTurretScreen::new);

        EntityModelLayer arrowTurretLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "arrow_turret"), "main");
        EntityRendererRegistry.register(KTurrets.ARROW_TURRET, ctx -> new EntityRenderer2<>(ctx, new ArrowTurretModel(ctx.getPart(arrowTurretLayer)), KTurrets.ID, "arrow_turret2", 0.4f));
        EntityModelLayerRegistry.registerModelLayer(arrowTurretLayer, ArrowTurretModel::getTexturedModelData);

        EntityModelLayer brickTurretLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "brick_turret"), "main");
        EntityRendererRegistry.register(KTurrets.BRICK_TURRET, ctx -> new EntityRenderer2<>(ctx, new BrickTurretModel(ctx.getPart(brickTurretLayer)), KTurrets.ID, "brick_turret", 0.4f));
        EntityModelLayerRegistry.registerModelLayer(brickTurretLayer, BrickTurretModel::getTexturedModelData);

        EntityRendererRegistry.register(KTurrets.BULLET, BulletRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(BulletRenderer.MODEL_LAYER, BulletModel::getTexturedModelData);

        EntityRendererRegistry.register(KTurrets.BRICK, BrickRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(BrickRenderer.LAYER, BrickModel::getTexturedModelData);
    }
}
