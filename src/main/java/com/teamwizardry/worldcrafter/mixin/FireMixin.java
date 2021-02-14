package com.teamwizardry.worldcrafter.mixin;

import static com.teamwizardry.worldcrafter.WorldCrafter.fireRecipes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.teamwizardry.worldcrafter.core.EntityTracker;
import com.teamwizardry.worldcrafter.recipe.FireRecipe;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(ItemEntity.class)
public abstract class FireMixin extends Entity
{
    public FireMixin(EntityType<?> entityTypeIn, World worldIn) { super(entityTypeIn, worldIn); }
    
    @Shadow public abstract ItemStack getItem();
    
    @Inject(method = "dealFireDamage", at = @At("HEAD"), cancellable = true)
    protected void registerItemEntities(int amount, CallbackInfo ci)
    {
        if (!fireRecipes.isRecipeIngredient(getItem()))
            return;
        this.setInvulnerable(true);
        EntityTracker.get(getEntityWorld(), FireRecipe.class).addItem(getPosition(), (ItemEntity) (Object) this);
        ci.cancel();
    }
}
