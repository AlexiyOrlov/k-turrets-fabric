package dev.buildtool.kurretsfabric.client;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.satako.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class EntityRenderer2<E extends LivingEntity, M extends EntityModel<E>> extends EntityRenderer<E, M> {
    private final Identifier texture;

    @SuppressWarnings("unchecked")
    public EntityRenderer2(EntityRendererFactory.Context ctx, M model, String mod, String texture, float shadowRadius) {
        super(ctx, model, mod, texture, false, shadowRadius);
        this.texture = new Identifier(KTurrets.ID, "textures/entity/" + texture + ".png");
    }

    @Override
    public Identifier getTexture(E entity) {
        return texture;
    }
}
