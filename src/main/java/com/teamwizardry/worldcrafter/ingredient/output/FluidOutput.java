package com.teamwizardry.worldcrafter.ingredient.output;

import com.teamwizardry.worldcrafter.ingredient.Ingredient;

import net.minecraft.fluid.Fluid;

public class FluidOutput extends Ingredient<FluidOutput>
{
    private final Fluid fluid;
    
    public FluidOutput(Fluid fluid)
    {
        this.fluid = fluid;
    }
    
    public Fluid getFluid() { return this.fluid; }
}
