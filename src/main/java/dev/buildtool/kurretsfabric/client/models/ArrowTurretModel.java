package dev.buildtool.kurretsfabric.client.models;

import dev.buildtool.kurretsfabric.turrets.ArrowTurret;
import dev.buildtool.satako.Functions;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17+ for Yarn
public class ArrowTurretModel extends EntityModel<ArrowTurret> {
    private final ModelPart rotating_base;
    private final ModelPart rotating_gun;
    private final ModelPart bb_main;

    public ArrowTurretModel(ModelPart root) {
        this.rotating_base = root.getChild("rotating_base");
        this.bb_main = root.getChild("bb_main");
        rotating_gun = rotating_base.getChild("rotating_gun");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData rotating_base = modelPartData.addChild("rotating_base", ModelPartBuilder.create().uv(0, 14).cuboid(-5.0F, -3.0F, -5.0F, 10.0F, 2.0F, 10.0F, new Dilation(0.0F))
                .uv(36, 27).cuboid(3.0F, -13.0F, -1.0F, 2.0F, 10.0F, 4.0F, new Dilation(0.0F))
                .uv(27, 27).cuboid(-5.0F, -13.0F, -1.0F, 2.0F, 10.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData rotating_gun = rotating_base.addChild("rotating_gun", ModelPartBuilder.create().uv(0, 5).cuboid(1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 27).cuboid(-1.0F, -1.0F, -6.5F, 2.0F, 2.0F, 11.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-3.0F, -1.0F, -0.5F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, 1.0F, 3.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-3.0F, -1.0F, 3.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(1.0F, -1.0F, 3.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, -3.0F, 3.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -10.0F, -0.5F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -1.0F, -6.0F, 12.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(ArrowTurret entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        rotating_base.yaw = Functions.getDefaultHeadYaw(netHeadYaw);
        rotating_gun.pitch = Functions.getDefaultHeadPitch(headPitch);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        rotating_base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}