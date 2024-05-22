// Made with Blockbench 4.10.1
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package dev.buildtool.kurretsfabric.client.models;

import dev.buildtool.kurretsfabric.turrets.FireChargeTurret;
import dev.buildtool.satako.Functions;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class FireballTurretModelv2 extends EntityModel<FireChargeTurret> {
    private final ModelPart gun_holder;
    private final ModelPart gun;
    private final ModelPart bb_main;

    public FireballTurretModelv2(ModelPart root) {
        this.gun_holder = root.getChild("gun_holder");
        this.gun = gun_holder.getChild("gun");
        this.bb_main = root.getChild("bb_main");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData gun_holder = modelPartData.addChild("gun_holder", ModelPartBuilder.create().uv(0, 0).cuboid(2.0F, -14.0F, -1.0F, 1.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-3.0F, -14.0F, -1.0F, 1.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData gun = gun_holder.addChild("gun", ModelPartBuilder.create().uv(25, 12).cuboid(-2.0F, -1.75F, -2.0F, 4.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 23).cuboid(-1.0F, -0.75F, -8.0F, 2.0F, 2.0F, 6.0F, new Dilation(0.0F))
                .uv(23, 32).cuboid(2.0F, -7.75F, 0.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 32).cuboid(-3.0F, -7.75F, 0.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -13.25F, 0.0F));

        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(9, 32).cuboid(4.0F, -5.0F, -6.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 32).cuboid(-6.0F, -5.0F, -6.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F))
                .uv(35, 23).cuboid(-6.0F, -5.0F, 4.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F))
                .uv(26, 23).cuboid(4.0F, -5.0F, 4.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-5.0F, -6.0F, -5.0F, 10.0F, 1.0F, 10.0F, new Dilation(0.0F))
                .uv(41, 0).cuboid(-4.0F, -7.0F, -4.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 12).cuboid(-3.0F, -8.0F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setAngles(FireChargeTurret entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        gun_holder.yaw = Functions.getDefaultHeadYaw(netHeadYaw);
        gun.pitch = Functions.getDefaultHeadPitch(headPitch);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        gun_holder.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}