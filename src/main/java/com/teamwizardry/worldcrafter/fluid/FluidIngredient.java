package com.teamwizardry.worldcrafter.fluid;

import com.teamwizardry.worldcrafter.Ingredient;

import net.minecraft.fluid.Fluid;

public class FluidIngredient extends Ingredient<FluidIngredient>
{
    private final Fluid fluid;
    private final int count;
    private final double consumeChance;
    
    public FluidIngredient(Fluid fluid, int count, double consumeChance)
    {
        this.fluid = fluid;
        this.count = count;
        this.consumeChance = consumeChance;
    }
    
    public Fluid getFluid() { return fluid; }
    
    public int getCount() { return count; }
    
    public double getConsumeChance() { return consumeChance; }
}
