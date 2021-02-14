package com.teamwizardry.worldcrafter.loading.recipe;

import java.util.List;
import java.util.Map;

import com.teamwizardry.worldcrafter.ingredient.ItemIngredient;
import com.teamwizardry.worldcrafter.ingredient.output.Output;
import com.teamwizardry.worldcrafter.loading.Loader;
import com.teamwizardry.worldcrafter.loading.NBTLoader;
import com.teamwizardry.worldcrafter.loading.ingredient.ItemIngredientLoader;
import com.teamwizardry.worldcrafter.loading.output.OutputLoader;
import com.teamwizardry.worldcrafter.recipe.ExplosionRecipe;

import net.minecraft.nbt.CompoundNBT;

public class ExplosionRecipeLoader extends Loader<ExplosionRecipe>
{
    public static final ExplosionRecipeLoader INSTANCE = new ExplosionRecipeLoader();
    
    private ExplosionRecipeLoader() {}
    
    @Override
    @SuppressWarnings("unchecked")
    public ExplosionRecipe load(Map<String, Object> yaml)
    {
        List<ItemIngredient> input = ItemIngredientLoader.INSTANCE.loadAll((List<Map<String, Object>>) yaml.get(INPUT));
        Output output = OutputLoader.INSTANCE.load(yaml.get(OUTPUT));
        CompoundNBT nbt = NBTLoader.INSTANCE.load(yaml, INPUT, OUTPUT, TYPE);
        return new ExplosionRecipe(input, output, nbt);
    }
}
