package com.teamwizardry.worldcrafter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public abstract class Recipe
{
    protected List<ItemIngredient> ingredients;
    protected Output output;
    protected Map<RecipeConsumer, Integer> tickConsumers;
    protected List<RecipeConsumer> finishConsumers;
    protected int duration;
    protected CompoundNBT extraData;
    
    public Recipe(List<ItemIngredient> ingredients, Output output, CompoundNBT extraData)
    {
        this.ingredients = ingredients;
        this.output = output;
        this.tickConsumers = new HashMap<>();
        this.finishConsumers = new LinkedList<>();
        this.duration = 0;
        this.extraData = extraData;
    }
    
    public boolean matches(List<ItemStack> items)
    {
        List<ItemStack> internal = items.stream().map(ItemStack::copy).collect(Collectors.toList());
        for (ItemIngredient ingredient : ingredients)
        {
            int count = ingredient.getCount();
            for (ItemStack item : internal)
            {
                if (item.isEmpty())
                    continue;
                if (item.getItem() == ingredient.getItem())
                {
                    int itemCount = item.getCount();
                    if (count >= itemCount)
                    {
                        item.shrink(itemCount);
                        count -= itemCount;
                    }
                    else
                    {
                       item.shrink(count);
                       count = 0;
                    }
                    
                    if (count <= 0)
                        break;
                }
            }
            if (count > 0)
                return false;
        }
        return true;
    }
    
    public boolean isValid(RecipeInfo info, List<ItemStack> items)
    {
        return this.matches(items);
    }
    
    public void tick(RecipeInfo info, List<ItemEntity> items)
    {
        int currentDuration = info.getCurrentDuration();
        tickConsumers.forEach((consumer, interval) -> {
            if (currentDuration % interval == 0)
                consumer.apply(info, items);
        });
    }
    
    public boolean isFinished(RecipeInfo info, List<ItemEntity> items)
    {
        return info.getCurrentDuration() > this.duration;
    }
    
    public void finish(RecipeInfo info, List<ItemEntity> items)
    {
        finishConsumers.forEach(consumer -> consumer.apply(info, items));
        ingredients.forEach(ingredient -> ingredient.consume(items));
        output.createOutput(info.getWorld(), info.getPos());
    }
}
