package dev.buildtool.kurretsfabric.client;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.client.models.BrickModel;
import dev.buildtool.kurretsfabric.projectiles.Brick;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BrickRenderer extends EntityRenderer<Brick> {
    public static final EntityModelLayer LAYER = new EntityModelLayer(new Identifier(KTurrets.ID, "brick"), "main");
    final Identifier texture1 = new Identifier(KTurrets.ID, "textures/entity/brick.png");
    final Identifier texture2 = new Identifier(KTurrets.ID, "textures/entity/netherbrick.png");
    private final BrickModel model;

    public BrickRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        model = new BrickModel(ctx.getPart(LAYER));
    }

    @Override
    public Identifier getTexture(Brick entity) {
        return entity.getDamage() == KTurrets.CONFIGURATION.brickDamage() ? texture1 : texture2;
    }

    @Override
    public void render(Brick entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        matrices.translate(0, -1.3, 0);
        model.render(matrices, vertexConsumers.getBuffer(model.getLayer(getTexture(entity))), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        matrices.pop();
    }
}
