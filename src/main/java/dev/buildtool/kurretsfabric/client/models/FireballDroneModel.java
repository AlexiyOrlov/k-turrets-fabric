package dev.buildtool.kurretsfabric.client.models;

import dev.buildtool.kurretsfabric.drones.FireballDrone;
import dev.buildtool.satako.Functions;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

@Deprecated
public class FireballDroneModel extends EntityModel<FireballDrone> {
    private final ModelPart beam;
    private final ModelPart beam2;
    private final ModelPart beam3;
    private final ModelPart beam4;
    private final ModelPart rotor1;
    private final ModelPart rotor2;
    private final ModelPart rotor3;
    private final ModelPart rotor4;
    private final ModelPart rotating;
    private final ModelPart barrel;
    private final ModelPart bb_main;

    public FireballDroneModel(ModelPart root) {
        this.beam = root.getChild("beam");
        this.beam2 = root.getChild("beam2");
        this.beam3 = root.getChild("beam3");
        this.beam4 = root.getChild("beam4");
        this.rotor1 = root.getChild("rotor1");
        this.rotor2 = root.getChild("rotor2");
        this.rotor3 = root.getChild("rotor3");
        this.rotor4 = root.getChild("rotor4");
        this.rotating = root.getChild("rotating");
        this.bb_main = root.getChild("bb_main");
        barrel = rotating.getChild("barrel");

    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData beam = modelPartData.addChild("beam", ModelPartBuilder.create().uv(30, 16).cuboid(3.0F, -15.0F, 3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        beam.addChild("cube_r1", ModelPartBuilder.create().uv(13, 29).cuboid(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, -11.0F, 1.5F, -0.7854F, 0.7854F, 0.0F));

        ModelPartData beam2 = modelPartData.addChild("beam2", ModelPartBuilder.create().uv(30, 13).cuboid(-4.0F, -15.0F, 3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        beam2.addChild("cube_r2", ModelPartBuilder.create().uv(28, 0).cuboid(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, -11.0F, 1.5F, -0.7854F, -0.7854F, 0.0F));

        ModelPartData beam3 = modelPartData.addChild("beam3", ModelPartBuilder.create().uv(30, 6).cuboid(-4.0F, -15.0F, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        beam3.addChild("cube_r3", ModelPartBuilder.create().uv(0, 15).cuboid(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, -11.0F, -1.5F, 0.7854F, 0.7854F, 0.0F));

        ModelPartData beam4 = modelPartData.addChild("beam4", ModelPartBuilder.create().uv(28, 29).cuboid(3.0F, -15.0F, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        beam4.addChild("cube_r4", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, -11.0F, -1.5F, 0.7854F, -0.7854F, 0.0F));

        modelPartData.addChild("rotor1", ModelPartBuilder.create().uv(0, 25).cuboid(-0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, 8.5F, 3.5F));

        modelPartData.addChild("rotor2", ModelPartBuilder.create().uv(23, 22).cuboid(-0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, 8.5F, 3.5F));

        modelPartData.addChild("rotor3", ModelPartBuilder.create().uv(10, 22).cuboid(-0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, 8.5F, -3.5F));

        modelPartData.addChild("rotor4", ModelPartBuilder.create().uv(22, 6).cuboid(-0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, 8.5F, -3.5F));

        ModelPartData rotating = modelPartData.addChild("rotating", ModelPartBuilder.create().uv(18, 22).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 15.0F, 0.0F));

        rotating.addChild("barrel", ModelPartBuilder.create().uv(0, 17).cuboid(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(11, 0).cuboid(-2.0F, -11.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(23, 29).cuboid(5.0F, -5.0F, 3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 29).cuboid(5.0F, -5.0F, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 25).cuboid(-6.0F, -5.0F, -4.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 6).cuboid(-6.0F, -5.0F, 3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        bb_main.addChild("cube_r5", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, 0.0F, -4.0F, 1.0F, 6.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -10.0F, 0.0F, 0.0F, 0.0F, -0.5672F));

        bb_main.addChild("cube_r6", ModelPartBuilder.create().uv(11, 7).cuboid(-1.0F, 0.0F, -4.0F, 1.0F, 6.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.5672F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(FireballDrone entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        rotating.yaw = Functions.getDefaultHeadYaw(netHeadYaw);
        barrel.pitch = Functions.getDefaultHeadPitch(headPitch);
        rotor1.yaw = ageInTicks * 1.5f;
        rotor2.yaw = -ageInTicks * 1.5f;
        rotor3.yaw = ageInTicks * 1.5f;
        rotor4.yaw = -ageInTicks * 1.5f;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        beam.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        beam2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        beam3.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        beam4.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotor1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotor2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotor3.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotor4.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotating.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}