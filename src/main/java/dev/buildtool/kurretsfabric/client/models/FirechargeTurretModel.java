package dev.buildtool.kurretsfabric.client.models;

import dev.buildtool.kurretsfabric.turrets.FireChargeTurret;
import dev.buildtool.satako.Functions;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17+ for Yarn
public class FirechargeTurretModel extends EntityModel<FireChargeTurret> {
    private final ModelPart rotating;
    private final ModelPart bone;

    public FirechargeTurretModel(ModelPart root) {
        this.rotating = root.getChild("rotating");
        this.bone = root.getChild("bone");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData rotating = modelPartData.addChild("rotating", ModelPartBuilder.create().uv(17, 18).cuboid(-2.0F, -6.75F, -1.25F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 16).cuboid(-0.5F, -4.25F, -8.25F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(12, 0).cuboid(-0.5F, -6.25F, -8.25F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(10, 9).cuboid(0.5F, -5.25F, -8.25F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(0, 7).cuboid(-1.5F, -5.25F, -8.25F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 17.0F, 0.25F));

        rotating.addChild("cube_r1", ModelPartBuilder.create().uv(22, 0).cuboid(-1.0F, -5.25F, -3.5F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.25F, 1.75F, -0.3491F, 0.0F, 0.0F));

        rotating.addChild("arrow", ModelPartBuilder.create().uv(39, 6).cuboid(-0.5F, 0.5F, -0.0833F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(39, 6).cuboid(-0.5F, -1.5F, -0.0833F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(42, 22).cuboid(-0.5F, -0.5F, -1.0833F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(2.5F, -4.75F, 1.0833F, 0.0F, 0.2618F, 0.0F));

        rotating.addChild("arrow2", ModelPartBuilder.create().uv(39, 6).mirrored().cuboid(-0.5F, 0.5F, -0.0833F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
                .uv(39, 6).mirrored().cuboid(-0.5F, -1.5F, -0.0833F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
                .uv(42, 22).mirrored().cuboid(-0.5F, -0.5F, -1.0833F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-2.5F, -4.75F, 1.0833F, 0.0F, -0.2618F, 0.0F));

        rotating.addChild("arrow3", ModelPartBuilder.create().uv(39, 13).mirrored().cuboid(1.5F, 0.5F, -1.8333F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
                .uv(39, 13).mirrored().cuboid(1.5F, -1.5F, -1.8333F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
                .uv(42, 30).mirrored().cuboid(1.5F, -0.5F, -2.8333F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, -9.75F, 2.0833F, 0.0F, -0.2618F, 1.5708F));

        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -7.0F, -2.0F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(20, 9).cuboid(-2.0F, -1.0F, -6.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        bone.addChild("cube_r2", ModelPartBuilder.create().uv(20, 9).cuboid(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, -0.5F, 4.0F, 0.0F, -0.6545F, 0.0F));

        bone.addChild("cube_r3", ModelPartBuilder.create().uv(20, 9).cuboid(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, -0.5F, 4.0F, 0.0F, 0.6545F, 0.0F));

        bone.addChild("cube_r4", ModelPartBuilder.create().uv(0, 25).cuboid(-1.5F, -3.5F, -1.0F, 1.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-2.2071F, -2.75F, 2.7071F, 0.3927F, 0.7854F, 0.6545F));

        bone.addChild("cube_r5", ModelPartBuilder.create().uv(7, 25).cuboid(0.5F, -3.5F, -1.0F, 1.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(2.2071F, -2.75F, 2.7071F, 0.3927F, -0.7854F, -0.6545F));

        bone.addChild("cube_r6", ModelPartBuilder.create().uv(14, 27).cuboid(-1.0F, -3.5F, -1.5F, 2.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.0F, -1.5F, -0.3054F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(FireChargeTurret entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        rotating.yaw = Functions.getDefaultHeadYaw(netHeadYaw);
        rotating.pitch = Functions.getDefaultHeadPitch(headPitch);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        rotating.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}