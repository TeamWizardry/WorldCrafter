package com.teamwizardry.worldcrafter;

import java.util.List;
import java.util.function.BiConsumer;

import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RecipeConsumer extends ForgeRegistryEntry<RecipeConsumer>
{
    private final BiConsumer<RecipeInfo, List<ItemEntity>> consumer;
    
    public RecipeConsumer(BiConsumer<RecipeInfo, List<ItemEntity>> consumer)
    {
        this.consumer = consumer;
    }
    
    public void apply(RecipeInfo recipe, List<ItemEntity> items)
    {
        this.consumer.accept(recipe, items);
    }
}
