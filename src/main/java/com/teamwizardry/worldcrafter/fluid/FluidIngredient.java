package com.teamwizardry.worldcrafter.fluid;

import java.util.Arrays;
import java.util.List;

import com.teamwizardry.worldcrafter.Ingredient;

import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class FluidIngredient extends Ingredient<FluidIngredient>
{
    private final Fluid fluid;
    private final int count;
    private final double baseConsumeChance;
    private final int consumeCount;
    private final double consumeChance;
    
    public FluidIngredient(Fluid fluid, int count, double consumeChance)
    {
        this.fluid = fluid;
        this.count = count;
        this.baseConsumeChance = consumeChance;
        
        double totalConsumeCount = count * consumeChance;
        this.consumeCount = (int) totalConsumeCount;
        this.consumeChance = totalConsumeCount - consumeCount;
    }
    
    public Fluid getFluid() { return fluid; }
    
    public int getCount() { return count; }
    
    public double getConsumeChance() { return baseConsumeChance; }

    public void consume(World world, List<BlockPos> sources)
    {
        int remaining = consumeCount;
        if (Math.random() < consumeChance)
            remaining++;
        
        for (BlockPos source : sources)
        {
            world.setBlockState(source, Blocks.AIR.getDefaultState());
            remaining--;
            if (remaining == 0)
                return;
        }
    }
    
    @Override
    public List<FluidStack> getMatchingFluids()
    {
        return Arrays.asList(new FluidStack(fluid, count * 1000));
    }
}
