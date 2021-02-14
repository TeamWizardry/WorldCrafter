package com.teamwizardry.worldcrafter.loading.recipe;

import java.util.Map;

import com.teamwizardry.worldcrafter.ingredient.ItemIngredient;
import com.teamwizardry.worldcrafter.ingredient.output.Output;
import com.teamwizardry.worldcrafter.loading.Loader;
import com.teamwizardry.worldcrafter.loading.NBTLoader;
import com.teamwizardry.worldcrafter.loading.ingredient.ItemIngredientLoader;
import com.teamwizardry.worldcrafter.loading.output.OutputLoader;
import com.teamwizardry.worldcrafter.recipe.FireRecipe;

import net.minecraft.nbt.CompoundNBT;

public class FireRecipeLoader extends Loader<FireRecipe>
{
    public static final FireRecipeLoader INSTANCE = new FireRecipeLoader();
    
    private FireRecipeLoader() {}
    
    @Override
    @SuppressWarnings("unchecked")
    public FireRecipe load(Map<String, Object> yaml)
    {
        ItemIngredient input = ItemIngredientLoader.INSTANCE.load((Map<String, Object>) yaml.get(INPUT));
        Output output = OutputLoader.INSTANCE.load(yaml.get(OUTPUT));
        CompoundNBT nbt = NBTLoader.INSTANCE.load(yaml, INPUT, OUTPUT, TYPE);
        int duration = Loader.loadInt(yaml, DURATION, 200);
        boolean isParallel = Loader.loadBoolean(yaml, PARALLEL, true);
        return new FireRecipe(input, output, duration, isParallel, nbt);
    }
}
