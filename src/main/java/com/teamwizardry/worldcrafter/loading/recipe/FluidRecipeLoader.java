package com.teamwizardry.worldcrafter.loading.recipe;

import java.util.List;
import java.util.Map;

import com.teamwizardry.worldcrafter.ingredient.FluidIngredient;
import com.teamwizardry.worldcrafter.ingredient.ItemIngredient;
import com.teamwizardry.worldcrafter.ingredient.output.Output;
import com.teamwizardry.worldcrafter.loading.Loader;
import com.teamwizardry.worldcrafter.loading.NBTLoader;
import com.teamwizardry.worldcrafter.loading.ingredient.FluidIngredientLoader;
import com.teamwizardry.worldcrafter.loading.ingredient.ItemIngredientLoader;
import com.teamwizardry.worldcrafter.loading.output.OutputLoader;
import com.teamwizardry.worldcrafter.recipe.FluidRecipe;

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
        CompoundNBT nbt = NBTLoader.INSTANCE.load(yaml, FLUID, INPUT, OUTPUT, TYPE);
        int duration = Loader.loadInt(yaml, DURATION, 200);
        boolean isParallel = Loader.loadBoolean(yaml, PARALLEL, false);
        return new FluidRecipe(input, fluid, output, duration, isParallel, nbt);
    }
}
