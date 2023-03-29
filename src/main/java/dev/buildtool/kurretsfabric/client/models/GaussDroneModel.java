package dev.buildtool.kurretsfabric.client.models;

import dev.buildtool.kurretsfabric.drones.GaussDrone;
import dev.buildtool.satako.Functions;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17+ for Yarn
public class GaussDroneModel extends EntityModel<GaussDrone> {
    private final ModelPart rotor1;
    private final ModelPart rotor2;
    private final ModelPart rotor3;
    private final ModelPart rotor4;
    private final ModelPart sides;
    private final ModelPart muzzle;
    private final ModelPart bb_main;

    public GaussDroneModel(ModelPart root) {
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
        modelPartData.addChild("rotor1", ModelPartBuilder.create().uv(34, 23).cuboid(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.5F, 12.5F, 5.5F));

        modelPartData.addChild("rotor2", ModelPartBuilder.create().uv(10, 30).cuboid(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.5F, 12.5F, 5.5F));

        modelPartData.addChild("rotor3", ModelPartBuilder.create().uv(12, 22).cuboid(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(5.5F, 12.5F, -5.5F));

        modelPartData.addChild("rotor4", ModelPartBuilder.create().uv(12, 19).cuboid(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.5F, 12.5F, -5.5F));

        ModelPartData sides = modelPartData.addChild("sides", ModelPartBuilder.create().uv(17, 30).cuboid(1.0F, -9.0F, -1.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(29, 28).cuboid(-2.0F, -9.0F, -1.0F, 1.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        sides.addChild("muzzle", ModelPartBuilder.create().uv(24, 6).cuboid(-1.0F, -1.0F, -5.25F, 2.0F, 2.0F, 6.0F, new Dilation(0.0F))
                .uv(34, 15).cuboid(-1.0F, -2.0F, 0.75F, 2.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 1.25F));

        modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(12, 19).cuboid(-6.0F, -11.0F, -6.0F, 1.0F, 1.0F, 9.0F, new Dilation(0.0F))
                .uv(0, 17).cuboid(5.0F, -11.0F, -6.0F, 1.0F, 1.0F, 9.0F, new Dilation(0.0F))
                .uv(0, 28).cuboid(-3.0F, -11.0F, -1.0F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(24, 19).cuboid(2.0F, -11.0F, -1.0F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-6.0F, -10.0F, -1.0F, 12.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(35, 0).cuboid(4.0F, -9.0F, 2.0F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 17).cuboid(4.0F, -9.0F, -1.0F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(12, 8).cuboid(4.0F, -3.0F, -5.0F, 1.0F, 1.0F, 9.0F, new Dilation(0.0F))
                .uv(0, 6).cuboid(-5.0F, -3.0F, -5.0F, 1.0F, 1.0F, 9.0F, new Dilation(0.0F))
                .uv(12, 6).cuboid(-5.0F, -9.0F, -1.0F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 6).cuboid(-5.0F, -9.0F, 2.0F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(GaussDrone entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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