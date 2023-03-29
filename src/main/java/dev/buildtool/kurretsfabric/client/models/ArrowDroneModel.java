package dev.buildtool.kurretsfabric.client.models;

import dev.buildtool.kurretsfabric.drones.ArrowDrone;
import dev.buildtool.satako.Functions;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17+ for Yarn
public class ArrowDroneModel extends EntityModel<ArrowDrone> {
    private final ModelPart rotor1;
    private final ModelPart rotor2;
    private final ModelPart rotor3;
    private final ModelPart rotor4;
    private final ModelPart sides;
    private final ModelPart muzzle;
    private final ModelPart bb_main;

    public ArrowDroneModel(ModelPart root) {
        this.rotor1 = root.getChild("rotor1");
        this.rotor2 = root.getChild("rotor2");
        this.rotor3 = root.getChild("rotor3");
        this.rotor4 = root.getChild("rotor4");
        this.sides = root.getChild("sides");
        this.bb_main = root.getChild("bb_main");
        muzzle = sides.getChild("muzzle");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("rotor1", ModelPartBuilder.create().uv(38, 37).cuboid(-0.5F, 2.5F, -1.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.5F, 10.5F, -3.5F));

        modelPartData.addChild("rotor2", ModelPartBuilder.create().uv(37, 27).cuboid(-0.5F, 2.5F, -1.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.5F, 10.5F, -3.5F));

        modelPartData.addChild("rotor3", ModelPartBuilder.create().uv(33, 0).cuboid(-0.5F, 2.5F, -1.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.5F, 10.5F, 3.5F));

        modelPartData.addChild("rotor4", ModelPartBuilder.create().uv(11, 22).cuboid(-0.5F, 2.5F, -1.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.5F, 10.5F, 3.5F));

        ModelPartData sides = modelPartData.addChild("sides", ModelPartBuilder.create().uv(13, 35).cuboid(1.0F, -6.0F, -2.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(30, 31).cuboid(-2.0F, -6.0F, -2.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        sides.addChild("muzzle", ModelPartBuilder.create().uv(29, 19).cuboid(-1.0F, 2.0F, -4.5F, 2.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -7.0F, 0.5F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 10).cuboid(3.0F, -9.0F, -4.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-4.0F, -9.0F, -4.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(17, 31).cuboid(-3.0F, -8.0F, -1.0F, 6.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 30).cuboid(-2.0F, -7.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(23, 10).cuboid(4.25F, -2.0F, -2.0F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(19, 22).cuboid(-5.25F, -2.0F, -2.0F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        bb_main.addChild("cube_r1", ModelPartBuilder.create().uv(24, 35).cuboid(8.5F, 0.0F, 0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(40, 9).cuboid(-0.5F, 0.0F, 0.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-4.5F, -8.0F, -3.5F, 0.3491F, 0.0F, 0.0F));

        bb_main.addChild("cube_r2", ModelPartBuilder.create().uv(0, 36).cuboid(8.5F, 0.0F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(5, 36).cuboid(-0.5F, 0.0F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-4.5F, -8.0F, 3.5F, -0.3491F, 0.0F, 0.0F));

        bb_main.addChild("cube_r3", ModelPartBuilder.create().uv(11, 2).cuboid(-1.5F, 2.5F, -2.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -12.5F, -4.0F, 0.0F, -0.6981F, 0.0F));

        bb_main.addChild("cube_r4", ModelPartBuilder.create().uv(11, 12).cuboid(0.5F, 2.5F, -2.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -12.5F, -4.0F, 0.0F, 0.6981F, 0.0F));

        bb_main.addChild("cube_r5", ModelPartBuilder.create().uv(0, 20).cuboid(0.5F, 2.5F, -6.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -12.5F, 4.0F, 0.0F, -0.6981F, 0.0F));

        bb_main.addChild("cube_r6", ModelPartBuilder.create().uv(22, 0).cuboid(-1.5F, 2.5F, -6.0F, 1.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -12.5F, 4.0F, 0.0F, 0.6981F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(ArrowDrone entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        sides.yaw = Functions.getDefaultHeadYaw(netHeadYaw);
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
        sides.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}