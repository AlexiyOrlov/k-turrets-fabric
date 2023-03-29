package dev.buildtool.kurretsfabric;

import dev.buildtool.kurretsfabric.client.screens.TurretOptionsScreen;
import dev.buildtool.satako.DefaultInventory;
import dev.buildtool.satako.UniqueList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public abstract class Turret extends MobEntity implements RangedAttackMob, ExtendedScreenHandlerFactory, Inventory {
    private static final TrackedData<NbtCompound> TARGETS = DataTracker.registerData(Turret.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    private static final TrackedData<Optional<UUID>> OWNER = DataTracker.registerData(Turret.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    protected static final TrackedData<Boolean> PUSHABLE = DataTracker.registerData(Turret.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> PROTECTING_FROM_PLAYERS = DataTracker.registerData(Turret.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<String> TEAM = DataTracker.registerData(Turret.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<NbtCompound> IGNORED_PLAYERS = DataTracker.registerData(Turret.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    private static final TrackedData<String> OWNER_NAME = DataTracker.registerData(Turret.class, TrackedDataHandlerRegistry.STRING);

    public Turret(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    public Optional<UUID> getOwner() {
        return this.dataTracker.get(OWNER);
    }

    public void setOwner(UUID uuid) {
        dataTracker.set(OWNER, Optional.of(uuid));
    }

    public NbtCompound getTargets() {
        return dataTracker.get(TARGETS);
    }

    public void setTargets(NbtCompound targets) {
        dataTracker.set(TARGETS, targets);
    }

    public boolean isMobile() {
        return dataTracker.get(PUSHABLE);
    }

    public void setPushable(boolean b) {
        dataTracker.set(PUSHABLE, b);
    }

    public boolean isProtectingFromPlayers() {
        return dataTracker.get(PROTECTING_FROM_PLAYERS);
    }

    public void setProtectingFromPlayers(boolean b) {
        dataTracker.set(PROTECTING_FROM_PLAYERS, b);
    }

    public String getAutomaticTeam() {
        return dataTracker.get(TEAM);
    }

    public void setAutomaticTeam(String team) {
        dataTracker.set(TEAM, team);
    }

    public NbtCompound getIgnoredPlayers() {
        return dataTracker.get(IGNORED_PLAYERS);
    }

    public void setIgnoredPlayers(NbtCompound ignoredPlayers) {
        dataTracker.set(IGNORED_PLAYERS, ignoredPlayers);
    }

    public String getOwnerName() {
        return dataTracker.get(OWNER_NAME);
    }

    public void setOwnerName(String name) {
        dataTracker.set(OWNER_NAME, name);
    }


    public Predicate<LivingEntity> alienPlayers = livingEntity -> {
        if (getOwner().isPresent()) {
            if (livingEntity instanceof PlayerEntity player) {
                NbtCompound targets = getIgnoredPlayers();
                for (String key : targets.getKeys()) {
                    if (targets.get(key).equals(player.getName().getString()))
                        return false;
                }
                return !player.getUuid().equals(getOwner().get()) && !isTeammate(player);
            }
        }
        return false;
    };

    public static DefaultAttributeContainer.Builder createDefaultAttributes() {
        return createLivingAttributes().add(EntityAttributes.GENERIC_FLYING_SPEED, 0.2).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(OWNER, Optional.empty());
        NbtCompound compound = new NbtCompound();
        List<EntityType<?>> entityTypes = Registry.ENTITY_TYPE.stream().filter(entityType -> !entityType.getSpawnGroup().isPeaceful()).toList();
        for (int i = 0; i < entityTypes.size(); i++) {
            compound.putString("Target#" + i, Registry.ENTITY_TYPE.getId(entityTypes.get(i)).toString());
        }
        compound.putInt("Count", entityTypes.size());
        dataTracker.startTracking(TARGETS, compound);
        dataTracker.startTracking(PUSHABLE, false);
        dataTracker.startTracking(PROTECTING_FROM_PLAYERS, false);
        dataTracker.startTracking(TEAM, "");
        dataTracker.startTracking(IGNORED_PLAYERS, new NbtCompound());
        dataTracker.startTracking(OWNER_NAME, "");
    }

    @Override
    protected void initGoals() {
        targetSelector.add(1, new RevengeTask(this));
    }

    public abstract boolean isArmed();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.put("Targets", getTargets());
        getOwner().ifPresent(uuid1 -> nbt.putUuid("Owner", uuid1));
        nbt.putBoolean("Pushable", isMobile());
        nbt.putBoolean("Protecting from alien players", isProtectingFromPlayers());
        nbt.putString("Team", getAutomaticTeam());
        nbt.put("Ignored players", getIgnoredPlayers());
        nbt.putString("Owner name", getOwnerName());
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        setTargets(nbt.getCompound("Targets"));
        if (nbt.contains("Owner"))
            setOwner(nbt.getUuid("Owner"));
        setPushable(nbt.getBoolean("Pushable"));
        setProtectingFromPlayers(nbt.getBoolean("Protecting from alien players"));
        setAutomaticTeam(nbt.getString("Team"));
        setIgnoredPlayers(nbt.getCompound("Ignored players"));
        if (nbt.contains("Owner name"))
            setOwnerName(nbt.getString("Owner name"));
    }

    public static NbtCompound encodeTargets(List<EntityType<?>> entityTypes) {
        NbtCompound compound = new NbtCompound();
        for (int i = 0; i < entityTypes.size(); i++) {
            EntityType<?> entityType = entityTypes.get(i);
            compound.putString("Target#" + i, Registry.ENTITY_TYPE.getId(entityType).toString());
        }
        compound.putInt("Count", entityTypes.size());
        return compound;
    }

    public static List<EntityType<?>> decodeTargets(NbtCompound nbtCompound) {
        int size = nbtCompound.getInt("Count");
        List<EntityType<?>> entityTypes = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String target = nbtCompound.getString("Target#" + i);
            entityTypes.add(Registry.ENTITY_TYPE.get(new Identifier(target)));
        }
        return entityTypes;
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    protected abstract List<DefaultInventory> getContainedItems();

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        getContainedItems().forEach(defaultInventory -> ItemScatterer.spawn(world, getBlockPos(), defaultInventory));
        getOwner().ifPresent(uuid1 -> {
            if (!world.isClient) {
                PlayerEntity player = world.getPlayerByUuid(uuid1);
                if (player != null) {
                    String position = " " + (int) getX() + " " + (int) getY() + " " + ((int) getZ());
                    if (damageSource.getAttacker() != null) {
                        player.sendMessage(getCustomName().copy().append(" ").append(Text.translatable("k_turrets.was.destroyed.by")).append(" ").append(damageSource.getAttacker().getCustomName()).append(" ").append(position), false);
                    } else {
                        if (damageSource.getSource() != null)
                            player.sendMessage(getCustomName().copy().append(" ").append(Text.translatable("k_turrets.was.destroyed.by")).append(" ").append(damageSource.getSource().getCustomName()).append(" ").append(Text.translatable("k_turrets.at")).append(" ").append(position), false);
                        else
                            player.sendMessage(damageSource.getDeathMessage(this).copy().append(" ").append(Text.translatable("k_turrets.at")).append(position), false);
                    }
                }
            }
        });
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ITEM_SHIELD_BLOCK;
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        StatusEffect statusEffect = effect.getEffectType();
        if (statusEffect == StatusEffects.POISON || statusEffect == StatusEffects.INSTANT_HEALTH || statusEffect == StatusEffects.HEALTH_BOOST || statusEffect == StatusEffects.REGENERATION || statusEffect == StatusEffects.WITHER || statusEffect == StatusEffects.HUNGER)
            return false;
        return super.canHaveStatusEffect(effect);
    }

    @Override
    public boolean isCollidable() {
        return !isMobile();
    }

    @Override
    public boolean isPushable() {
        return isMobile();
    }

    @Override
    protected void knockback(LivingEntity target) {
        if (isMobile())
            super.knockback(target);
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    public Item getSpawnItem() {
        return SpawnEggItem.forEntity(getType());
    }

    @Nullable
    @Override
    public AbstractTeam getScoreboardTeam() {
        if (getOwner().isPresent()) {
            PlayerEntity player = world.getPlayerByUuid(getOwner().get());
            if (player != null && player.getScoreboardTeam() != null)
                return player.getScoreboardTeam();
            else return getAutomaticTeam().isEmpty() ? null : world.getScoreboard().getTeam(getAutomaticTeam());
        }
        return super.getScoreboardTeam();
    }

    @Override
    public boolean isTeammate(Entity other) {
        return super.isTeammate(other) || (getOwner().isPresent() && other.getUuid().equals(getOwner().get())) || other instanceof Turret turret && turret.getOwner().equals(getOwner());
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        Entity source = damageSource.getAttacker();
        if (source instanceof PlayerEntity player && getOwner().isPresent() && player.getUuid().equals(getOwner().get()))
            return true;
        return super.isInvulnerableTo(damageSource);
    }

    @Override
    public int size() {
        return getContainedItems().get(0).size();
    }

    @Override
    public boolean isEmpty() {
        return getContainedItems().get(0).isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return getContainedItems().get(0).getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        getContainedItems().get(0).setStack(slot, stack);
    }

    @Override
    public void clear() {
        getContainedItems().get(0).clear();
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return getContainedItems().get(0).isValid(slot, stack);
    }

    public void addPlayerToExceptions(String name) {
        NbtCompound nbtCompound = getIgnoredPlayers();
        for (String key : nbtCompound.getKeys()) {
            String nickname = nbtCompound.getString(key);
            if (nickname.equals(name))
                return;
        }
        nbtCompound.putString("Ignored player#" + nbtCompound.getSize(), name);
    }

    public void removePlayerFromExceptions(String name) {
        NbtCompound compound = getIgnoredPlayers();
        for (String key : compound.getKeys()) {
            String nickname = compound.getString(key);
            if (nickname.equals(name)) {
                compound.remove(key);
                break;
            }
        }
    }

    public List<String> getExceptions() {
        NbtCompound nbtCompound = getIgnoredPlayers();
        List<String> names = new UniqueList<>(nbtCompound.getSize());
        nbtCompound.getKeys().forEach(s -> names.add(nbtCompound.getString(s)));
        return names;
    }

    @Override
    public boolean isInsideWall() {
        return false;
    }

    protected boolean canUse(PlayerEntity playerEntity) {
        return getOwner().isEmpty() || getOwner().get().equals(playerEntity.getUuid());
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return canUse(player);
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setStackInHand(Hand hand, ItemStack stack) {

    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    protected double getRange() {
        return getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
    }

    @Environment(EnvType.CLIENT)
    private void openConfigurationScreen() {
        MinecraftClient.getInstance().setScreen(new TurretOptionsScreen(this));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (canUse(player) && !player.isSneaking()) {
            if (player instanceof ServerPlayerEntity)
                player.openHandledScreen(this);
            return ActionResult.SUCCESS;
        }

        ItemStack itemStack = player.getStackInHand(hand);
        if (getHealth() < getMaxHealth() && itemStack.streamTags().anyMatch(itemTagKey -> itemTagKey.id().equals(KTurrets.titaniumIngots))) {
            heal(restoreHealth());
            itemStack.decrement(1);
            return ActionResult.SUCCESS;
        }
        if (canUse(player)) {
            if (world.isClient)
                openConfigurationScreen();
            if (player.getScoreboardTeam() != null)
                setAutomaticTeam(player.getScoreboardTeam().getName());
            else setAutomaticTeam("");
            if (getOwner().isEmpty()) {
                setOwnerName(player.getName().getString());
            }
            return ActionResult.SUCCESS;
        } else if (world.isClient) {
            if (getOwner().isEmpty()) {
                if (this instanceof Drone)
                    player.sendMessage(Text.translatable("k_turrets.drone.not.yours"), true);
                else
                    player.sendMessage(Text.translatable("k_turrets.turret.not.yours"), true);
            } else if (this instanceof Drone)
                player.sendMessage(Text.translatable("k_turrets.drone.belongs.to").append(" ").append(player.getName()), true);
            else
                player.sendMessage(Text.translatable("k_turrets.turret.belongs.to").append(" ").append(player.getName()), true);

        }
        return ActionResult.PASS;
    }

    @Override
    public void markDirty() {

    }

    protected float restoreHealth() {
        return getMaxHealth() / 6;
    }
}
