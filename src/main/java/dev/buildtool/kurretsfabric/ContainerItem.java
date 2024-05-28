package dev.buildtool.kurretsfabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContainerItem extends SpawnEggItem {
    private final Unit unit;

    public enum Unit {
        TURRET,
        DRONE
    }

    public static List<ContainerItem> placers = new ArrayList<>(12);

    public ContainerItem(EntityType<? extends MobEntity> type, int primaryColor, int secondaryColor, Settings settings, Unit unitType) {
        super(type, primaryColor, secondaryColor, settings);
        unit = unitType;
        placers.add(this);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!(world instanceof ServerWorld)) {
            return ActionResult.SUCCESS;
        }

        PlayerEntity player = context.getPlayer();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            UnitLimits unitLimits = KTurrets.UNIT_LIMITS.get(player);
            if (unit == Unit.TURRET) {
                if (unitLimits.getTurretCount() >= KTurrets.CONFIGURATION.turretLimitPerPlayer()) {
                    player.sendMessage(Text.translatable("k_turrets.reached.turret.limit.of").append(" " + KTurrets.CONFIGURATION.turretLimitPerPlayer()).append(". ").append(Text.translatable("k_turrets.cannot.place.more")));
                    return ActionResult.CONSUME;
                } else unitLimits.increaseTurretCount();
            } else if (unit == Unit.DRONE) {
                if (unitLimits.getDroneCount() >= KTurrets.CONFIGURATION.droneLimitPerPlayer()) {
                    player.sendMessage(Text.translatable("k_turrets.reached.drone.limit.of").append(" " + KTurrets.CONFIGURATION.droneLimitPerPlayer()).append(". ").append(Text.translatable("k_turrets.cannot.place.more")));
                    return ActionResult.CONSUME;
                } else unitLimits.increaseDroneCount();
            }
        }

        ItemStack itemStack = context.getStack();
        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        BlockState blockState = world.getBlockState(blockPos);

        BlockPos blockPos2 = blockState.getCollisionShape(world, blockPos).isEmpty() ? blockPos : blockPos.offset(direction);
        EntityType<?> entityType2 = this.getEntityType(itemStack.getNbt());
        Entity entity = entityType2.spawnFromItemStack((ServerWorld) world, itemStack, context.getPlayer(), blockPos2, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockPos, blockPos2) && direction == Direction.UP);
        if (entity != null) {
            if (itemStack.hasNbt()) {
                entity.readNbt(itemStack.getSubNbt("Contained"));
                entity.setPos(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
                entity.setPosition(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
            } else if (KTurrets.CONFIGURATION.setOwnerAuto()) {
                Turret turret = (Turret) entity;
                turret.setOwner(player.getUuid());
            }
            itemStack.decrement(1);
        }
        return ActionResult.CONSUME;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (stack.hasNbt()) {
            tooltip.add(Text.literal(stack.getNbt().getUuid("UUID").toString()));
        }
    }
}
