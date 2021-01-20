package com.teamwizardry.worldcrafter;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RecipeConsumer extends ForgeRegistryEntry<RecipeConsumer>
{
    private final BiConsumer<RecipeInfo, List<ItemEntity>> recipeCallback;
    private final TriFunction<Recipe, Ingredient<?>, ItemStack, List<String>> tooltipCallback;
    
    public RecipeConsumer(BiConsumer<RecipeInfo, List<ItemEntity>> recipeCallback)
    {
        this.recipeCallback = recipeCallback;
        this.tooltipCallback = (info, ingredient, item) -> Arrays.asList();
    }
    public RecipeConsumer(BiConsumer<RecipeInfo, List<ItemEntity>> recipeCallback, TriFunction<Recipe, Ingredient<?>, ItemStack, List<String>> tooltipCallback)
    {
        this.recipeCallback = recipeCallback;
        this.tooltipCallback = tooltipCallback;
    }
    
    public void apply(RecipeInfo recipe, List<ItemEntity> items)
    {
        this.recipeCallback.accept(recipe, items);
    }
    
    public List<String> apply(Recipe recipe, Ingredient<?> ingredient, ItemStack stack)
    {
        return this.tooltipCallback.apply(recipe, ingredient, stack);
    }
    
    @FunctionalInterface public interface TriFunction<T, U, V, W>
    {
        public W apply(T t, U u, V v);
    }
}
