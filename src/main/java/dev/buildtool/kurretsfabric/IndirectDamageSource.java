package dev.buildtool.kurretsfabric;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class IndirectDamageSource extends EntityDamageSource {
    private final Entity owner;
    public IndirectDamageSource(String name, Entity source, Entity owner) {
        super(name, source);
        this.owner = owner;
    }

    @Override
    public Entity getAttacker() {
        return this.owner;
    }

    @Nullable
    @Override
    public Entity getSource() {
        return source;
    }

    @Override
    public Text getDeathMessage(LivingEntity entity) {
        Text component = this.owner == null ? this.source.getDisplayName() : this.owner.getDisplayName();
        ItemStack itemstack = this.owner instanceof LivingEntity ? ((LivingEntity) this.owner).getMainHandStack() : ItemStack.EMPTY;
        String s = "death.attack." + this.name;
        String s1 = s + ".item";
        return !itemstack.isEmpty() && itemstack.hasCustomName() ? Text.translatable(s1, entity.getDisplayName(), component, itemstack.getName()) : Text.translatable(s, entity.getDisplayName(), component);
    }
}
