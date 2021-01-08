package com.teamwizardry.worldcrafter;

import java.util.function.Consumer;

import net.minecraftforge.registries.ForgeRegistryEntry;

public class RecipeConsumer extends ForgeRegistryEntry<RecipeConsumer>
{
    private final Consumer<RecipeInfo> consumer;
    
    public RecipeConsumer(Consumer<RecipeInfo> consumer)
    {
        this.consumer = consumer;
    }
    
    public void apply(RecipeInfo recipe)
    {
        this.consumer.accept(recipe);
    }
}
