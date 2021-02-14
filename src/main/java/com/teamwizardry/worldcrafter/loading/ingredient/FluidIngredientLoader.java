package com.teamwizardry.worldcrafter.loading.ingredient;

import java.util.Map;

import com.teamwizardry.worldcrafter.ingredient.FluidIngredient;
import com.teamwizardry.worldcrafter.loading.Loader;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidIngredientLoader extends Loader<FluidIngredient>
{
    public static final FluidIngredientLoader INSTANCE = new FluidIngredientLoader();

    private static final String TYPE = "type";
    private static final String COUNT = "count";
    private static final String CHANCE = "consumeChance";
    
    private FluidIngredientLoader() {}
    
    @Override
    public FluidIngredient load(Map<String, Object> yaml)
    {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(Loader.loadString(yaml, TYPE)));
        int count = Loader.loadInt(yaml, COUNT, 1);
        count = Math.max(count, 1);
        double chance = MathHelper.clamp(Loader.loadDouble(yaml, CHANCE, 0), 0, 1);
        
        return new FluidIngredient(fluid, count, chance);
    }
}
