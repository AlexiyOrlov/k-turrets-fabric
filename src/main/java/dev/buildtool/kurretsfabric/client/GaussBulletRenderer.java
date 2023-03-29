package dev.buildtool.kurretsfabric.client;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.client.models.GaussBulletModel;
import dev.buildtool.kurretsfabric.projectiles.GaussBullet;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class GaussBulletRenderer extends EntityRenderer<GaussBullet> {
    public static final EntityModelLayer LAYER = new EntityModelLayer(new Identifier(KTurrets.ID, "gauss_bullet"), "main");
    private final GaussBulletModel gaussBulletModel;
    private final Identifier texture = new Identifier(KTurrets.ID, "texture/entity/gauss_bullet.png");

    public GaussBulletRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        shadowOpacity = 0;
        gaussBulletModel = new GaussBulletModel(ctx.getPart(LAYER));
    }

    @Override
    public Identifier getTexture(GaussBullet entity) {
        return texture;
    }

    @Override
    public void render(GaussBullet entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        matrices.translate(0, -1.4, 0);
        gaussBulletModel.render(matrices, vertexConsumers.getBuffer(gaussBulletModel.getLayer(getTexture(entity))), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        matrices.pop();
    }
}
