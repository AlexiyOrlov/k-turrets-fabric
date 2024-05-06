package dev.buildtool.kurretsfabric.mixin;

import dev.buildtool.kurretsfabric.Turret;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(EntityRenderDispatcher.class)
public class EntityRendererMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", shift = At.Shift.AFTER))
    private void renderHealthIndicator(Entity entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo callbackInfo) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (entity instanceof Turret turret) {
            if (turret.getOwner().isEmpty() || (turret.getOwner().isPresent() && (player.getUuid().equals(turret.getOwner().get()) || player.isTeammate(turret)))) {
                matrices.push();
                String health = String.format("%.1f", turret.getHealth()) + "/" + (int) turret.getMaxHealth();
                matrices.scale(0.03f, 0.03f, 0.03f);
                matrices.multiply(MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getRotation());
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180));
                TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
                matrices.translate(-textRenderer.getWidth(health) / 2f, -30 - turret.getHeight() * 30, 0);
                textRenderer.draw(matrices, health, 0, 0, turret.getHealth() < turret.getMaxHealth() / 2 ? Formatting.RED.getColorValue() : Formatting.GREEN.getColorValue());
                matrices.pop();
            }
        }
    }
}
