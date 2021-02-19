package com.teamwizardry.worldcrafter.loading.recipe;

import java.util.List;
import java.util.Map;

import com.teamwizardry.worldcrafter.ingredient.ItemIngredient;
import com.teamwizardry.worldcrafter.ingredient.output.Output;
import com.teamwizardry.worldcrafter.loading.Loader;
import com.teamwizardry.worldcrafter.loading.NBTLoader;
import com.teamwizardry.worldcrafter.loading.ingredient.ItemIngredientLoader;
import com.teamwizardry.worldcrafter.loading.output.OutputLoader;
import com.teamwizardry.worldcrafter.recipe.LightningRecipe;

import net.minecraft.nbt.CompoundNBT;

public class LightningRecipeLoader extends Loader<LightningRecipe>
{
    public static final LightningRecipeLoader INSTANCE = new LightningRecipeLoader();
    
    private LightningRecipeLoader() {}
    
    @Override
    @SuppressWarnings("unchecked")
    public LightningRecipe load(Map<String, Object> yaml)
    {
        List<ItemIngredient> input = ItemIngredientLoader.INSTANCE.loadAll((List<Map<String, Object>>) yaml.get(INPUT));
        Output output = OutputLoader.INSTANCE.load(yaml.get(OUTPUT)).setInvulnerable();
        CompoundNBT nbt = NBTLoader.INSTANCE.load(yaml, INPUT, OUTPUT, TYPE);
        return new LightningRecipe(input, output, nbt);
    }
}
