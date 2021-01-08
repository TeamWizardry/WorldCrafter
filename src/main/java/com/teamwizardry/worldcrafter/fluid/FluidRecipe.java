package com.teamwizardry.worldcrafter.fluid;

import java.util.List;

import com.teamwizardry.worldcrafter.ItemIngredient;
import com.teamwizardry.worldcrafter.Output;
import com.teamwizardry.worldcrafter.Recipe;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidRecipe extends Recipe
{
    private final FluidIngredient fluid;
    private final int duration;
    
    public FluidRecipe(List<ItemIngredient> ingredients, FluidIngredient fluid, Output output, int duration, CompoundNBT extraData)
    {
        super(ingredients, output, extraData);
        this.fluid = fluid;
        this.duration = duration;
    }
    
    public boolean isValid(World world, BlockPos pos)
    {
        boolean superValid = super.isValid(world, pos);
        boolean itemsMatch = this.matches(FluidEntityTracker.getItems(world, pos));
        boolean fluidMatches = this.fluidMatches(world, pos);
        return superValid && itemsMatch && fluidMatches;
    }
    
    private boolean fluidMatches(World world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof FlowingFluidBlock)
            return ((FlowingFluidBlock) block).getFluid().equals(fluid.getFluid());
        return false;
    }
    
    public int getDuration() { return this.duration; }
}
