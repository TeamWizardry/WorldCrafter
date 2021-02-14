package com.teamwizardry.worldcrafter.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.teamwizardry.worldcrafter.core.EntityTracker;
import com.teamwizardry.worldcrafter.recipe.FluidRecipe;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(FlowingFluidBlock.class)
public abstract class FluidMixin
{
    @Inject(method = "onEntityCollision", at = @At("HEAD"))
    protected void registerItemEntities(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci)
    {
        if (!(entity instanceof ItemEntity))
            return;
        if (!world.getFluidState(pos).isSource())
            return;
        EntityTracker.get(world, FluidRecipe.class).addItem(pos, (ItemEntity) entity);
    }
}
