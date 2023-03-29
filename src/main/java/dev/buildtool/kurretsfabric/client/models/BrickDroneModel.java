package dev.buildtool.kurretsfabric.client.models;

import dev.buildtool.kurretsfabric.drones.BrickDrone;
import dev.buildtool.satako.Functions;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17+ for Yarn
public class BrickDroneModel extends EntityModel<BrickDrone> {
    private final ModelPart rotor1;
    private final ModelPart base;
    private final ModelPart rotor2;
    private final ModelPart rotor3;
    private final ModelPart rotor4;
    private final ModelPart sides;
    private final ModelPart muzzle;

    public BrickDroneModel(ModelPart root) {
        this.rotor1 = root.getChild("rotor1");
        this.base = root.getChild("base");
        this.rotor2 = root.getChild("rotor2");
        this.rotor3 = root.getChild("rotor3");
        this.rotor4 = root.getChild("rotor4");
        this.sides = root.getChild("sides");
        muzzle = sides.getChild("muzzle");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("rotor1", ModelPartBuilder.create().uv(0, 20).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.5F, 14.5F, 6.0F));

        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(17, 14).cuboid(-4.0F, -9.0F, -2.0F, 8.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 14).cuboid(-6.0F, -9.0F, -6.0F, 2.0F, 1.0F, 12.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(4.0F, -9.0F, -6.0F, 2.0F, 1.0F, 12.0F, new Dilation(0.0F))
                .uv(17, 0).cuboid(-2.0F, -8.0F, -3.0F, 4.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        base.addChild("cube_r1", ModelPartBuilder.create().uv(0, 28).cuboid(-1.0F, -6.0F, -0.5F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F))
                .uv(5, 28).cuboid(-11.0F, -6.0F, -0.5F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(5.5F, -4.0F, 5.5F, 0.6981F, 0.0F, 0.0F));

        base.addChild("cube_r2", ModelPartBuilder.create().uv(10, 28).cuboid(-11.0F, -6.0F, -0.5F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F))
                .uv(15, 28).cuboid(-1.0F, -6.0F, -0.5F, 1.0F, 10.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(5.5F, -4.0F, -5.5F, -0.6981F, 0.0F, 0.0F));

        modelPartData.addChild("rotor2", ModelPartBuilder.create().uv(0, 14).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.5F, 14.5F, 6.0F));

        modelPartData.addChild("rotor3", ModelPartBuilder.create().uv(0, 6).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.5F, 14.5F, -6.0F));

        modelPartData.addChild("rotor4", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.5F, 14.5F, -6.0F));

        ModelPartData sides = modelPartData.addChild("sides", ModelPartBuilder.create().uv(27, 31).cuboid(1.0F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(20, 31).cuboid(-2.0F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 20.0F, 0.0F));

        sides.addChild("muzzle", ModelPartBuilder.create().uv(23, 22).cuboid(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(BrickDrone entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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
        base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotor2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotor3.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotor4.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        sides.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}