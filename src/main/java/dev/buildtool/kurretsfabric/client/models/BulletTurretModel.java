package dev.buildtool.kurretsfabric.client.models;

import dev.buildtool.kurretsfabric.turrets.BulletTurret;
import dev.buildtool.satako.Functions;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17+ for Yarn
public class BulletTurretModel extends EntityModel<dev.buildtool.kurretsfabric.turrets.BulletTurret> {
    private final ModelPart bone;
    private final ModelPart rotating;
    private final ModelPart vertical;
    private final ModelPart bb_main;

    public BulletTurretModel(ModelPart root) {
        this.bone = root.getChild("bone");
        this.rotating = root.getChild("rotating");
        this.bb_main = root.getChild("bb_main");
        vertical = rotating.getChild("vertical");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData rotating = modelPartData.addChild("rotating", ModelPartBuilder.create().uv(28, 10).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 18).cuboid(-3.0F, -4.0F, -2.0F, 6.0F, 2.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 3).cuboid(2.0F, -5.0F, -2.0F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(7, 3).cuboid(-6.0F, -5.0F, -2.0F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(6, 54).cuboid(-2.0F, -5.0F, -1.0F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(18, 12).cuboid(6.0F, -11.0F, -2.0F, 1.0F, 6.0F, 6.0F, new Dilation(0.0F))
                .uv(18, 24).cuboid(-7.0F, -11.0F, -2.0F, 1.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.0F, 0.0F));

        ModelPartData vertical = rotating.addChild("vertical", ModelPartBuilder.create().uv(22, 36).cuboid(-6.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 34).cuboid(-4.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 3).cuboid(2.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-3.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(28, 24).cuboid(-2.0F, -3.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(10, 42).cuboid(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(30, 32).cuboid(2.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(9, 26).cuboid(4.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(10, 30).cuboid(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(34, 16).cuboid(-1.0F, -1.0F, -9.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 1.0F));

        ModelPartData cube_r1 = vertical.addChild("cube_r1", ModelPartBuilder.create().uv(32, 9).cuboid(-5.5F, -2.5F, 1.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(32, 9).cuboid(-0.5F, -2.5F, 1.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.5F, -6.5F, 0.5F, -0.829F, 0.0F, 0.0F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(21, 44).cuboid(-3.0F, -3.0F, -5.0F, 6.0F, 3.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(BulletTurret entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        rotating.yaw = Functions.getDefaultHeadYaw(netHeadYaw);
        vertical.pitch = Functions.getDefaultHeadPitch(headPitch);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotating.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}