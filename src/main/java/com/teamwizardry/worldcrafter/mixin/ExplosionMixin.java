package com.teamwizardry.worldcrafter.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.teamwizardry.worldcrafter.core.EntityTracker;
import com.teamwizardry.worldcrafter.recipe.ExplosionRecipe;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

@Mixin(ItemEntity.class)
public abstract class ExplosionMixin extends Entity
{
    public ExplosionMixin(EntityType<?> entityType, World world) { super(entityType, world); }
    
    @Inject(method = "attackEntityFrom", at = @At("HEAD"))
    protected void registerItemEntities(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci)
    {
        if (source.isExplosion())
            EntityTracker.get(getEntityWorld(), ExplosionRecipe.class).addItem(getPosition(), (ItemEntity) (Object) this);
    }
}
