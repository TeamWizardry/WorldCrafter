package com.teamwizardry.worldcrafter.fluid;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.teamwizardry.worldcrafter.ItemIngredient;
import com.teamwizardry.worldcrafter.Output;
import com.teamwizardry.worldcrafter.Recipe;
import com.teamwizardry.worldcrafter.RecipeInfo;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidRecipe extends Recipe
{
    private final FluidIngredient fluid;
    
    public FluidRecipe(List<ItemIngredient> ingredients, FluidIngredient fluid, Output output, int duration, CompoundNBT extraData)
    {
        super(ingredients, output, extraData);
        this.fluid = fluid;
        this.duration = duration;
    }
    
    @Override
    public boolean isValid(RecipeInfo info, List<ItemStack> items)
    {
        boolean superValid = super.isValid(info, items);
        boolean itemsMatch = this.matches(items);
        boolean fluidMatches = this.fluidMatches(info.getWorld(), info.getPos());
        return superValid && itemsMatch && fluidMatches;
    }
    
    @Override
    public void finish(RecipeInfo info, List<ItemEntity> items)
    {
        super.finish(info, items);
        fluid.consume(info.getWorld(), findConnectedSources(info.getWorld(), info.getPos(), fluid.getCount()));
    }
    
    private boolean fluidMatches(World world, BlockPos pos)
    {
        return findConnectedSources(world, pos, fluid.getCount()).size() >= fluid.getCount();
    }
    
    private List<BlockPos> findConnectedSources(World world, BlockPos center, int numSources)
    {
        Queue<BlockPos> toSearch = new LinkedList<>();
        Set<BlockPos> searched = new HashSet<>();
        List<BlockPos> sources = new LinkedList<>();
        toSearch.add(center);
        searched.add(center);
        sources.add(center);
        
        while (!toSearch.isEmpty())
        {
            BlockPos pos = toSearch.remove();
            for (Direction dir : Direction.values())
            {
                BlockPos newPos = pos.offset(dir);
                if (searched.contains(newPos))
                    continue;
                
                IFluidState state = world.getFluidState(newPos);
                if (!state.getFluid().equals(fluid.getFluid()))
                    continue;
                toSearch.add(newPos);
                searched.add(newPos);
                if (!state.isSource())
                    continue;
                sources.add(newPos);
                if (sources.size() >= numSources)
                    return sources;
            }
        }
        return sources;
    }
}
