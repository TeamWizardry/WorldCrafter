package com.teamwizardry.worldcrafter.loading;

import java.util.List;
import java.util.Map;

import com.teamwizardry.worldcrafter.ItemIngredient;
import com.teamwizardry.worldcrafter.Output;
import com.teamwizardry.worldcrafter.fluid.FluidIngredient;
import com.teamwizardry.worldcrafter.fluid.FluidRecipe;

import net.minecraft.nbt.CompoundNBT;

public class FluidRecipeLoader extends Loader<FluidRecipe>
{
    public static final FluidRecipeLoader INSTANCE = new FluidRecipeLoader();
    
    protected static final String FLUID = "fluid";
    
    private FluidRecipeLoader() {}
    
    @Override
    @SuppressWarnings("unchecked")
    public FluidRecipe load(Map<String, Object> yaml)
    {
        FluidIngredient fluid = FluidIngredientLoader.INSTANCE.load((Map<String, Object>) yaml.get(FLUID));
        List<ItemIngredient> input = ItemIngredientLoader.INSTANCE.loadAll((List<Map<String, Object>>) yaml.get(INPUT));
        Output output = OutputLoader.INSTANCE.load(yaml.get(OUTPUT));
        CompoundNBT nbt = NBTLoader.INSTANCE.load(yaml, FLUID, INPUT, OUTPUT);
        int duration = Loader.loadInt(yaml, DURATION, 200);
        return new FluidRecipe(input, fluid, output, duration, nbt);
    }
}
