package dev.buildtool.kurretsfabric.client;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.client.models.*;
import dev.buildtool.kurretsfabric.client.screens.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KTurretsClient implements ClientModInitializer {

    public static KeyBinding showDrones;
    public static boolean noDronesNearby;

    @Override
    public void onInitializeClient() {
        HandledScreens.register(KTurrets.ARROW_TURRET_HANDLER, ArrowTurretScreen::new);
        HandledScreens.register(KTurrets.BRICK_TURRET_HANDLER, BrickTurretScreen::new);
        HandledScreens.register(KTurrets.BULLET_TURRET_HANDLER, BulletTurretScreen::new);
        HandledScreens.register(KTurrets.GAUSS_TURRET_HANDLER, GaussTurretScreen::new);
        HandledScreens.register(KTurrets.FIRE_CHARGE_TURRET_HANDLER, FireballTurretScreen::new);
        HandledScreens.register(KTurrets.COBBLE_TURRET_HANDLER, CobbleTurretScreen::new);
        HandledScreens.register(KTurrets.ARROW_DRONE_HANDLER, ArrowDroneScreen::new);
        HandledScreens.register(KTurrets.BRICK_DRONE_HANDLER, BrickDroneScreen::new);
        HandledScreens.register(KTurrets.BULLET_DRONE_HANDLER, BulletDroneScreen::new);
        HandledScreens.register(KTurrets.COBBLE_DRONE_HANDLER, CobbleDroneScreen::new);
        HandledScreens.register(KTurrets.FIREBALL_DRONE_HANDLER, FireballDroneScreen::new);
        HandledScreens.register(KTurrets.GAUSS_DRONE_HANDLER, GaussDroneScreen::new);

        EntityModelLayer arrowTurretLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "arrow_turret"), "main");
        EntityRendererRegistry.register(KTurrets.ARROW_TURRET, ctx -> new EntityRenderer2<>(ctx, new ArrowTurretModel(ctx.getPart(arrowTurretLayer)), KTurrets.ID, "arrow_turret2", 0.4f));
        EntityModelLayerRegistry.registerModelLayer(arrowTurretLayer, ArrowTurretModel::getTexturedModelData);

        EntityModelLayer brickTurretLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "brick_turret"), "main");
        EntityRendererRegistry.register(KTurrets.BRICK_TURRET, ctx -> new EntityRenderer2<>(ctx, new BrickTurretModel(ctx.getPart(brickTurretLayer)), KTurrets.ID, "brick_turret", 0.4f));
        EntityModelLayerRegistry.registerModelLayer(brickTurretLayer, BrickTurretModel::getTexturedModelData);

        EntityModelLayer cobbleTurretLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "cobble_turret"), "main");
        EntityRendererRegistry.register(KTurrets.COBBLE_TURRET, ctx -> new EntityRenderer2<>(ctx, new CobbleTurretModel(ctx.getPart(cobbleTurretLayer)), KTurrets.ID, "cobble_turret2", 0.2f));
        EntityModelLayerRegistry.registerModelLayer(cobbleTurretLayer, CobbleTurretModel::getTexturedModelData);

        EntityModelLayer bulletTurretLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "bullet_turret"), "main");
        EntityRendererRegistry.register(KTurrets.BULLET_TURRET, ctx -> new EntityRenderer2<>(ctx, new BulletTurretModel(ctx.getPart(bulletTurretLayer)), KTurrets.ID, "bullet_turret4", 0.4f));
        EntityModelLayerRegistry.registerModelLayer(bulletTurretLayer, BulletTurretModel::getTexturedModelData);

        EntityModelLayer firechargeTurretLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "firecharge_turret"), "main");
        EntityRendererRegistry.register(KTurrets.FIRE_CHARGE_TURRET, ctx -> new EntityRenderer2<>(ctx, new FirechargeTurretModel(ctx.getPart(firechargeTurretLayer)), KTurrets.ID, "firecharge_turret", 0.3f));
        EntityModelLayerRegistry.registerModelLayer(firechargeTurretLayer, FirechargeTurretModel::getTexturedModelData);

        EntityModelLayer gaussTurretLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "gauss_turret"), "main");
        EntityRendererRegistry.register(KTurrets.GAUSS_TURRET, ctx -> new EntityRenderer2<>(ctx, new GaussTurretModel(ctx.getPart(gaussTurretLayer)), KTurrets.ID, "gauss_turret", 0.2f));
        EntityModelLayerRegistry.registerModelLayer(gaussTurretLayer, GaussTurretModel::getTexturedModelData);

        EntityModelLayer arrowDroneLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "arrow_drone"), "main");
        EntityRendererRegistry.register(KTurrets.ARROW_DRONE, ctx -> new EntityRenderer2<>(ctx, new ArrowDroneModel(ctx.getPart(arrowDroneLayer)), KTurrets.ID, "arrow_drone", 0.2f));
        EntityModelLayerRegistry.registerModelLayer(arrowDroneLayer, ArrowDroneModel::getTexturedModelData);

        EntityModelLayer brickDroneLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "brick_drone"), "main");
        EntityRendererRegistry.register(KTurrets.BRICK_DRONE, ctx -> new EntityRenderer2<>(ctx, new BrickDroneModel(ctx.getPart(brickDroneLayer)), KTurrets.ID, "brick_drone", 0.2f));
        EntityModelLayerRegistry.registerModelLayer(brickDroneLayer, BrickDroneModel::getTexturedModelData);

        EntityModelLayer bulletDroneLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "bullet_drone"), "main");
        EntityRendererRegistry.register(KTurrets.BULLET_DRONE, ctx -> new EntityRenderer2<>(ctx, new BulletDroneModel(ctx.getPart(bulletDroneLayer)), KTurrets.ID, "bullet_drone", 0.2f));
        EntityModelLayerRegistry.registerModelLayer(bulletDroneLayer, BulletDroneModel::getTexturedModelData);

        EntityModelLayer cobbleDroneLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "cobble_drone"), "main");
        EntityRendererRegistry.register(KTurrets.COBBLE_DRONE, ctx -> new EntityRenderer2<>(ctx, new CobbleDroneModel(ctx.getPart(cobbleDroneLayer)), KTurrets.ID, "cobble_drone", 0.2f));
        EntityModelLayerRegistry.registerModelLayer(cobbleDroneLayer, CobbleDroneModel::getTexturedModelData);

        EntityModelLayer fireballDroneLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "fireball_drone"), "main");
        EntityRendererRegistry.register(KTurrets.FIREBALL_DRONE, ctx -> new EntityRenderer2<>(ctx, new FireballDroneModel(ctx.getPart(fireballDroneLayer)), KTurrets.ID, "firecharge_drone", 0.2f));
        EntityModelLayerRegistry.registerModelLayer(fireballDroneLayer, FireballDroneModel::getTexturedModelData);

        EntityModelLayer gaussDroneLayer = new EntityModelLayer(new Identifier(KTurrets.ID, "gauss_drone"), "main");
        EntityRendererRegistry.register(KTurrets.GAUSS_DRONE, ctx -> new EntityRenderer2<>(ctx, new GaussDroneModel(ctx.getPart(gaussDroneLayer)), KTurrets.ID, "gauss_drone", 0.2f));
        EntityModelLayerRegistry.registerModelLayer(gaussDroneLayer, GaussDroneModel::getTexturedModelData);

        EntityRendererRegistry.register(KTurrets.BULLET, BulletRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(BulletRenderer.MODEL_LAYER, BulletModel::getTexturedModelData);

        EntityRendererRegistry.register(KTurrets.BRICK, BrickRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(BrickRenderer.LAYER, BrickModel::getTexturedModelData);

        EntityRendererRegistry.register(KTurrets.COBBLESTONE, CobblestoneRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(CobblestoneRenderer.LAYER, CobblestoneModel::getTexturedModelData);

        EntityRendererRegistry.register(KTurrets.GAUSS_BULLET, GaussBulletRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(GaussBulletRenderer.LAYER, GaussBulletModel::getTexturedModelData);

        showDrones = KeyBindingHelper.registerKeyBinding(new KeyBinding("k_turrets.show.drones", GLFW.GLFW_KEY_K, "K-Turrets"));

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            if (showDrones.isPressed()) {

            }
        });
    }
}
