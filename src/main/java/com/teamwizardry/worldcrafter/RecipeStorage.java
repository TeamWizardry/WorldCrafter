package com.teamwizardry.worldcrafter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;

public class RecipeStorage<T extends Recipe>
{
    private Set<T> recipes = new HashSet<>();
    
    public T getRecipe(List<ItemStack> items)
    {
        return recipes.stream().filter(recipe -> recipe.matches(items)).findFirst().orElse(null);
    }
    
    public void addRecipe(T recipe)
    {
        recipes.add(recipe);
    }
}
