package dev.buildtool.kurretsfabric.client.models;

import dev.buildtool.kurretsfabric.drones.CobbleDrone;
import dev.buildtool.satako.Functions;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17+ for Yarn
public class CobbleDroneModel extends EntityModel<CobbleDrone> {
    private final ModelPart rotor1;
    private final ModelPart rotor2;
    private final ModelPart rotor3;
    private final ModelPart rotor4;
    private final ModelPart rotating;
    private final ModelPart muzzle;
    private final ModelPart bb_main;

    public CobbleDroneModel(ModelPart root) {
        this.rotor1 = root.getChild("rotor1");
        this.rotor2 = root.getChild("rotor2");
        this.rotor3 = root.getChild("rotor3");
        this.rotor4 = root.getChild("rotor4");
        this.rotating = root.getChild("rotating");
        this.bb_main = root.getChild("bb_main");
        muzzle = rotating.getChild("muzzle");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData rotor1 = modelPartData.addChild("rotor1", ModelPartBuilder.create().uv(27, 29).cuboid(-0.5F, 2.5F, -1.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(4.5F, 10.5F, 5.5F));

        ModelPartData rotor2 = modelPartData.addChild("rotor2", ModelPartBuilder.create().uv(18, 29).cuboid(-0.5F, 2.5F, -1.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(4.5F, 10.5F, -5.5F));

        ModelPartData rotor3 = modelPartData.addChild("rotor3", ModelPartBuilder.create().uv(9, 29).cuboid(-0.5F, 2.5F, -1.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.5F, 10.5F, -5.5F));

        ModelPartData rotor4 = modelPartData.addChild("rotor4", ModelPartBuilder.create().uv(0, 29).cuboid(-0.5F, 2.5F, -1.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.5F, 10.5F, 5.5F));

        ModelPartData rotating = modelPartData.addChild("rotating", ModelPartBuilder.create().uv(28, 10).cuboid(-1.0F, -7.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData muzzle = rotating.addChild("muzzle", ModelPartBuilder.create().uv(37, 13).cuboid(-1.0F, -1.0F, -4.25F, 2.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.25F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 12).cuboid(5.0F, -10.0F, -5.0F, 1.0F, 1.0F, 10.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-6.0F, -10.0F, -5.0F, 1.0F, 1.0F, 10.0F, new Dilation(0.0F))
                .uv(23, 26).cuboid(-5.0F, -10.0F, -6.0F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 26).cuboid(-5.0F, -10.0F, 5.0F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(21, 22).cuboid(-5.0F, -8.0F, -1.0F, 10.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(39, 0).cuboid(5.0F, -2.0F, -5.0F, 1.0F, 1.0F, 10.0F, new Dilation(0.0F))
                .uv(39, 0).cuboid(-6.0F, -2.0F, -5.0F, 1.0F, 1.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData cube_r1 = bb_main.addChild("cube_r1", ModelPartBuilder.create().uv(0, 35).cuboid(-0.25F, -4.5F, -1.0F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 35).mirrored().cuboid(-11.75F, -4.5F, -1.0F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(5.5F, -5.5F, 0.0F, -0.6981F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(CobbleDrone entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        rotating.yaw = Functions.getDefaultHeadYaw(netHeadYaw);
        muzzle.pitch = Functions.getDefaultHeadPitch(headPitch);
        rotor1.yaw = ageInTicks * 1.5f;
        rotor2.yaw = -ageInTicks * 1.5f;
        rotor3.yaw = ageInTicks * 1.5f;
        rotor4.yaw = -ageInTicks * 1.5f;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        rotor1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotor2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotor3.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotor4.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotating.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}