package dev.buildtool.kurretsfabric.client;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.client.models.CobblestoneModel;
import dev.buildtool.kurretsfabric.projectiles.Cobblestone;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CobblestoneRenderer extends EntityRenderer<Cobblestone> {
    private final CobblestoneModel model;
    private final Identifier texture = new Identifier(KTurrets.ID, "texture/entity/cobblestone.png");
    public static final EntityModelLayer LAYER = new EntityModelLayer(new Identifier(KTurrets.ID, "cobblestone"), "main");

    public CobblestoneRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        model = new CobblestoneModel(ctx.getPart(LAYER));
    }

    @Override
    public Identifier getTexture(Cobblestone entity) {
        return texture;
    }

    @Override
    public void render(Cobblestone entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        matrices.translate(0, -1.3, 0);
        model.render(matrices, vertexConsumers.getBuffer(model.getLayer(getTexture(entity))), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        matrices.pop();
    }
}
