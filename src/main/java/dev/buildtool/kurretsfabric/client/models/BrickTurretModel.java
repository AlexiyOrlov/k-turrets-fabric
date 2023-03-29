package dev.buildtool.kurretsfabric.client.models;

import dev.buildtool.kurretsfabric.turrets.BrickTurret;
import dev.buildtool.satako.Functions;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17+ for Yarn
public class BrickTurretModel extends EntityModel<BrickTurret> {
    private final ModelPart left;
    private final ModelPart right;
    private final ModelPart rotating;
    private final ModelPart bb_main;

    public BrickTurretModel(ModelPart root) {
        this.left = root.getChild("left");
        this.right = root.getChild("right");
        this.rotating = root.getChild("rotating");
        this.bb_main = root.getChild("bb_main");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData left = modelPartData.addChild("left", ModelPartBuilder.create().uv(16, 14).cuboid(-0.5F, -0.5F, -6.5F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 20).cuboid(-5.5F, 0.5F, -4.5F, 5.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(10, 20).cuboid(-5.5F, -0.5F, -5.5F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.5F, 20.5F, 3.5F));

        ModelPartData cube_r1 = left.addChild("cube_r1", ModelPartBuilder.create().uv(8, 25).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r2 = left.addChild("cube_r2", ModelPartBuilder.create().uv(12, 25).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -7.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData right = modelPartData.addChild("right", ModelPartBuilder.create().uv(8, 13).cuboid(-0.5F, -0.5F, -6.5F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(11, 0).cuboid(0.5F, 0.5F, -4.5F, 5.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(20, 3).cuboid(4.5F, -0.5F, -5.5F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.5F, 20.5F, 3.5F));

        ModelPartData cube_r3 = right.addChild("cube_r3", ModelPartBuilder.create().uv(8, 9).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r4 = right.addChild("cube_r4", ModelPartBuilder.create().uv(16, 13).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -7.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData rotating = modelPartData.addChild("rotating", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -4.0F, -6.0F, 2.0F, 2.0F, 7.0F, new Dilation(0.0F))
                .uv(11, 5).cuboid(1.0F, -3.0F, 0.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(12, 3).cuboid(3.0F, -5.0F, -3.0F, 1.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(11, 3).cuboid(-3.0F, -3.0F, 0.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 9).cuboid(-4.0F, -5.0F, -3.0F, 1.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 23).cuboid(-0.5F, -3.5F, 1.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(20, 21).cuboid(-1.5F, -5.5F, 4.0F, 3.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 20.0F, 0.0F));

        ModelPartData cube_r5 = rotating.addChild("cube_r5", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -4.0F, -1.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 9).cuboid(1.0F, -4.0F, -1.0F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, 0.0F, -0.3491F, 0.0F, 0.0F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(24, 11).cuboid(-1.0F, -6.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void setAngles(BrickTurret entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        rotating.yaw = Functions.getDefaultHeadYaw(netHeadYaw);
        rotating.pitch = Functions.getDefaultHeadPitch(headPitch);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        left.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        right.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotating.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}