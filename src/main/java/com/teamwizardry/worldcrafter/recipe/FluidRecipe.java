package com.teamwizardry.worldcrafter.recipe;

import static com.teamwizardry.worldcrafter.WorldCrafter.fluidSerializer;
import static com.teamwizardry.worldcrafter.WorldCrafter.location;
import static net.minecraft.util.registry.Registry.RECIPE_TYPE;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.teamwizardry.worldcrafter.core.RecipeInfo;
import com.teamwizardry.worldcrafter.ingredient.FluidIngredient;
import com.teamwizardry.worldcrafter.ingredient.ItemIngredient;
import com.teamwizardry.worldcrafter.ingredient.output.Output;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class FluidRecipe extends Recipe
{
    public static final ResourceLocation UID = location("fluid_recipe");
    
    private final FluidIngredient fluid;
    
    public FluidRecipe(List<ItemIngredient> ingredients, FluidIngredient fluid, Output output, int duration, boolean isParallel, CompoundNBT extraData)
    {
        super(ingredients, output, isParallel, extraData);
        this.fluid = fluid;
        this.duration = duration;
    }
    
    @Override
    public boolean isValid(RecipeInfo info, Collection<ItemStack> items)
    {
        boolean fluidMatches = this.fluidMatches(info.getWorld(), info.getPos());
        boolean superValid = super.isValid(info, items);
        return superValid && fluidMatches;
    }
    
    @Override
    public void finish(RecipeInfo info, Collection<ItemEntity> items)
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
    
    @Override
    public List<List<FluidStack>> getFluidIngredients()
    {
        return Arrays.asList(fluid.getMatchingFluids());
    }
    
    public FluidIngredient getFluid() { return fluid; }

    @Override public ResourceLocation getId() { return UID; }

    @Override public IRecipeSerializer<?> getSerializer() { return fluidSerializer; }

    @Override public IRecipeType<?> getType() { return RECIPE_TYPE.getValue(UID).get(); }
}
