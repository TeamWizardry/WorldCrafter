package com.teamwizardry.worldcrafter.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.teamwizardry.worldcrafter.recipe.Recipe;

import net.minecraft.item.ItemStack;

public class RecipeStorage<T extends Recipe>
{
    private Set<T> recipes = new HashSet<>();
    
    public Collection<T> getRecipes() { return recipes; }
    
    public T getRecipe(List<ItemStack> items)
    {
        return recipes.stream().filter(recipe -> recipe.matches(items)).findFirst().orElse(null);
    }
    
    public boolean isRecipeIngredient(ItemStack item)
    {
        return recipes.stream().anyMatch(recipe -> 
                recipe.getItemIngredients().stream().anyMatch(ingredient ->
                    ingredient.getMatchingItems().stream().anyMatch(item::isItemEqual)));
    }
    
    public void addRecipe(T recipe)
    {
        recipes.add(recipe);
    }
}
