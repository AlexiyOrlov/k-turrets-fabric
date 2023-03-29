package dev.buildtool.kurretsfabric.client;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.client.models.BulletModel;
import dev.buildtool.kurretsfabric.projectiles.Bullet;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BulletRenderer extends EntityRenderer<Bullet> {
    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(new Identifier(KTurrets.ID, "bullet"), "main");
    private final Identifier texture1 = new Identifier(KTurrets.ID, "textures/entity/gold_bullet.png");
    private final Identifier texture2 = new Identifier(KTurrets.ID, "textures/entity/iron_bullet.png");
    private BulletModel model;

    public BulletRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        model = new BulletModel(ctx.getPart(MODEL_LAYER));
    }

    @Override
    public Identifier getTexture(Bullet entity) {
        return entity.getDamage() == KTurrets.CONFIGURATION.goldBulletDamage() ? texture1 : texture2;
    }

    @Override
    public void render(Bullet entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        matrices.translate(0, -1.4, 0);
        model.render(matrices, vertexConsumers.getBuffer(model.getLayer(getTexture(entity))), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        matrices.pop();
    }
}
