package dev.buildtool.kurretsfabric.client.models;

import dev.buildtool.kurretsfabric.turrets.GaussTurret;
import dev.buildtool.satako.Functions;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17+ for Yarn
public class GaussTurretModel extends EntityModel<GaussTurret> {
    private final ModelPart turret_base;
    private final ModelPart turret_angler;

    public GaussTurretModel(ModelPart root) {
        this.turret_base = root.getChild("turret_base");
        turret_angler = turret_base.getChild("turret_angler");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData turret_base = modelPartData.addChild("turret_base", ModelPartBuilder.create().uv(0, 12).cuboid(-2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 22.0F, 1.0F));

        ModelPartData turret_angler = turret_base.addChild("turret_angler", ModelPartBuilder.create().uv(29, 23).cuboid(-1.5F, -3.0F, -2.5F, 3.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

        ModelPartData turret_piece = turret_angler.addChild("turret_piece", ModelPartBuilder.create().uv(20, 6).cuboid(-2.0F, -1.0F, -3.0F, 4.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        ModelPartData turret_neck = turret_piece.addChild("turret_neck", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.0F, 0.0F));

        turret_neck.addChild("cube_r1", ModelPartBuilder.create().uv(0, 30).cuboid(-1.0F, -4.0F, -2.0F, 2.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData turret_head = turret_neck.addChild("turret_head", ModelPartBuilder.create().uv(16, 16).cuboid(-1.5F, -3.5F, -1.0F, 3.0F, 6.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-3.0F, -3.0F, -4.0F, 6.0F, 5.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.0F, 2.5F));

        turret_head.addChild("turret_barrel", ModelPartBuilder.create().uv(15, 28).cuboid(-1.5F, -2.0F, -4.0F, 3.0F, 4.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 23).cuboid(-2.5F, -1.0F, -8.0F, 5.0F, 2.0F, 5.0F, new Dilation(0.0F))
                .uv(28, 14).cuboid(-1.0F, 0.0F, -12.0F, 2.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.5F, -4.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(GaussTurret entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        turret_angler.yaw = Functions.getDefaultHeadYaw(netHeadYaw);
        turret_angler.pitch = Functions.getDefaultHeadPitch(headPitch) - 0.2618f;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        turret_base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}