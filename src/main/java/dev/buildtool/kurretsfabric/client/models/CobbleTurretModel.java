package dev.buildtool.kurretsfabric.client.models;

import dev.buildtool.kurretsfabric.turrets.CobbleTurret;
import dev.buildtool.satako.Functions;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17+ for Yarn
public class CobbleTurretModel extends EntityModel<CobbleTurret> {
    private final ModelPart bone;
    private final ModelPart bone2;
    private final ModelPart bone3;
    private final ModelPart bone4;
    private final ModelPart rotating;
    private final ModelPart bb_main;

    public CobbleTurretModel(ModelPart root) {
        this.bone = root.getChild("bone");
        this.bone2 = root.getChild("bone2");
        this.bone3 = root.getChild("bone3");
        this.bone4 = root.getChild("bone4");
        this.rotating = root.getChild("rotating");
        this.bb_main = root.getChild("bb_main");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create().uv(30, 12).cuboid(-2.0F, -2.0F, -4.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(28, 5).cuboid(-1.0F, -1.0F, -7.0F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        modelPartData.addChild("bone2", ModelPartBuilder.create().uv(30, 9).cuboid(-2.0F, -2.0F, 3.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(21, 6).cuboid(-1.0F, -1.0F, 4.0F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        modelPartData.addChild("bone3", ModelPartBuilder.create().uv(26, 26).cuboid(3.0F, -2.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(8, 30).cuboid(4.0F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        modelPartData.addChild("bone4", ModelPartBuilder.create().uv(16, 26).cuboid(-4.0F, -2.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(30, 0).cuboid(-7.0F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        modelPartData.addChild("rotating", ModelPartBuilder.create().uv(0, 27).cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 17).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(14, 18).cuboid(-1.5F, -6.25F, 4.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(8, 27).cuboid(-2.0F, -6.0F, -3.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(20, 18).cuboid(-1.0F, -7.0F, -8.0F, 2.0F, 2.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, -5.0F, -4.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 26).cuboid(-1.0F, -10.0F, 0.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(15, 10).cuboid(-5.0F, -10.0F, -2.0F, 4.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(0, 9).cuboid(1.0F, -10.0F, -2.0F, 4.0F, 1.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 20.0F, 0.0F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.0F))
                .uv(18, 0).cuboid(-2.0F, -4.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(CobbleTurret entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        rotating.yaw = Functions.getDefaultHeadYaw(netHeadYaw);
        rotating.pitch = Functions.getDefaultHeadPitch(headPitch);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        bone2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        bone3.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        bone4.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotating.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}