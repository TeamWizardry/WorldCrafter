package com.teamwizardry.worldcrafter.loading.output;

import java.util.Map;

import com.teamwizardry.worldcrafter.ingredient.output.FluidOutput;
import com.teamwizardry.worldcrafter.loading.Loader;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidOutputLoader extends Loader<FluidOutput>
{
    public static final FluidOutputLoader INSTANCE = new FluidOutputLoader();
    
    protected static final String FLUID = "fluid";
    protected static final String NBT = "nbt";
    
    private FluidOutputLoader() {}
    
    @Override
    public FluidOutput load(Map<String, Object> yaml)
    {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation((String) yaml.get(FLUID)));
        
        return new FluidOutput(fluid);
    }
}
