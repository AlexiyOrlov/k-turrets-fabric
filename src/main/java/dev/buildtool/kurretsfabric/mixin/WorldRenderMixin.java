package dev.buildtool.kurretsfabric.mixin;

import dev.buildtool.kurretsfabric.Drone;
import dev.buildtool.kurretsfabric.client.KTurretsClient;
import dev.buildtool.satako.IntegerColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class WorldRenderMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;renderParticles(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/client/render/Camera;F)V", shift = At.Shift.AFTER))
    private void renderDroneLocations(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo callbackInfo) {
        if (KTurretsClient.showDrones.isPressed()) {
            MinecraftClient minecraft = MinecraftClient.getInstance();
            PlayerEntity player = minecraft.player;
            List<Drone> nearbyDrones = minecraft.world.getEntitiesByClass(Drone.class, new Box(player.getBlockPos()).expand(64), drone -> drone.getOwner().isPresent() && drone.getOwner().get().equals(player.getUuid()));
            if (nearbyDrones.isEmpty()) {
                KTurretsClient.noDronesNearby = true;
            } else {
                VertexConsumerProvider.Immediate vertexConsumerProvider = minecraft.getBufferBuilders().getEffectVertexConsumers();
                Vec3d projectedView = minecraft.gameRenderer.getCamera().getPos();
                matrices.push();
                matrices.translate(-projectedView.x, -projectedView.y, -projectedView.z);
                IntegerColor color = new IntegerColor(0x40FF5790);
                nearbyDrones.forEach(drone -> {
                    Vec3d dronePosition = drone.getPos();
                    matrices.translate(dronePosition.x - 0.5, dronePosition.y - 0.2, dronePosition.z - 0.5);
                    addRectangle(vertexConsumerProvider.getBuffer(RenderLayer.getLightning()), matrices.peek().getPositionMatrix(), 0, 0, 0, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), false, 0);
                    matrices.translate(-(dronePosition.x - 0.5), -(dronePosition.y - 0.2), -(dronePosition.z - 0.5));
                });
                matrices.pop();
            }
        }
    }

    private static void addRectangle(VertexConsumer vertexConsumerProvider, Matrix4f matrix4f, int width, int height, int depth, float red, float green, float blue, float alpha, boolean addBackFaces, float extruder) {
        //Up
        vertexConsumerProvider.vertex(matrix4f, 0, height + 1 + extruder, 0).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, 0, height + 1 + extruder, depth + 1).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, width + 1, height + 1 + extruder, depth + 1).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, width + 1, height + 1 + extruder, 0).color(red, green, blue, alpha).next();
        if (addBackFaces) {
            vertexConsumerProvider.vertex(matrix4f, 1 + width, height + 1 + extruder, 0).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, 1 + width, height + 1 + extruder, 1 + depth).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, 0, height + 1 + extruder, 1 + depth).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, 0, height + 1 + extruder, 0).color(red, green, blue, alpha).next();
        }

        //Down
        vertexConsumerProvider.vertex(matrix4f, 1 + width, -extruder, 0).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, 1 + width, -extruder, 1 + depth).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, 0, -extruder, 1 + depth).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, 0, -extruder, 0).color(red, green, blue, alpha).next();
        if (addBackFaces) {
            vertexConsumerProvider.vertex(matrix4f, 0, -extruder, 0).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, 0, -extruder, 1 + depth).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, 1 + width, -extruder, 1 + depth).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, 1 + width, -extruder, 0).color(red, green, blue, alpha).next();
        }

        //North
        vertexConsumerProvider.vertex(matrix4f, 0, 0, -extruder).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, 0, height + 1, -extruder).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, width + 1, height + 1, -extruder).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, width + 1, 0, -extruder).color(red, green, blue, alpha).next();
        if (addBackFaces) {
            vertexConsumerProvider.vertex(matrix4f, width + 1, 0, -extruder).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, width + 1, height + 1, -extruder).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, 0, height + 1, -extruder).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, 0, 0, -extruder).color(red, green, blue, alpha).next();
        }

        //South
        vertexConsumerProvider.vertex(matrix4f, width + 1, 0, depth + 1 + extruder).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, width + 1, height + 1, depth + 1 + extruder).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, 0, height + 1, depth + 1 + extruder).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, 0, 0, depth + 1 + extruder).color(red, green, blue, alpha).next();
        if (addBackFaces) {
            vertexConsumerProvider.vertex(matrix4f, 0, 0, depth + 1 + extruder).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, 0, height + 1, depth + 1 + extruder).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, width + 1, height + 1, depth + 1 + extruder).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, width + 1, 0, depth + 1 + extruder).color(red, green, blue, alpha).next();
        }

        //West
        vertexConsumerProvider.vertex(matrix4f, -extruder, 0, 0).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, -extruder, 0, depth + 1).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, -extruder, height + 1, depth + 1).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, -extruder, height + 1, 0).color(red, green, blue, alpha).next();
        if (addBackFaces) {
            vertexConsumerProvider.vertex(matrix4f, -extruder, height + 1, 0).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, -extruder, height + 1, depth + 1).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, -extruder, 0, depth + 1).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, -extruder, 0, 0).color(red, green, blue, alpha).next();
        }

        //East
        vertexConsumerProvider.vertex(matrix4f, width + 1 + extruder, height + 1, 0).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, width + 1 + extruder, height + 1, depth + 1).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, width + 1 + extruder, 0, depth + 1).color(red, green, blue, alpha).next();
        vertexConsumerProvider.vertex(matrix4f, width + 1 + extruder, 0, 0).color(red, green, blue, alpha).next();
        if (addBackFaces) {
            vertexConsumerProvider.vertex(matrix4f, width + 1 + extruder, 0, 0).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, width + 1 + extruder, 0, depth + 1).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, width + 1 + extruder, height + 1, depth + 1).color(red, green, blue, alpha).next();
            vertexConsumerProvider.vertex(matrix4f, width + 1 + extruder, height + 1, 0).color(red, green, blue, alpha).next();
        }
    }
}
