package com.teamwizardry.worldcrafter;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class Recipe
{
    protected List<ItemIngredient> ingredients;
    protected Output output;
    protected List<RecipeConsumer> tickConsumers;
    protected List<RecipeConsumer> finishConsumers;
    protected CompoundNBT extraData;
    
    public Recipe(List<ItemIngredient> ingredients, Output output, CompoundNBT extraData)
    {
        this.ingredients = ingredients;
        this.output = output;
        this.tickConsumers = new LinkedList<>();
        this.finishConsumers = new LinkedList<>();
        this.extraData = extraData;
    }
    
    public boolean matches(List<ItemStack> items)
    {
        List<ItemStack> internal = items.stream().map(ItemStack::copy).collect(Collectors.toList());
        for (ItemIngredient ingredient : ingredients)
        {
            int count = ingredient.getInt("count");
            for (ItemStack item : internal)
            {
                if (item.getCount() <= 0)
                    continue;
                if (item.getItem() == ingredient.getItem())
                {
                    int itemCount = item.getCount();
                    if (count > itemCount)
                    {
                        item.shrink(count);
                        count -= itemCount;
                    }
                    else
                    {
                        
                    }
                    
                    if (count <= 0)
                        break;
                }
            }
        }
        return true;
    }
    
    public boolean isValid(World world, BlockPos pos)
    {
        return true;
    }
    
    public void tick()
    {
        tickConsumers.forEach(consumer -> consumer.apply(null));
    }
    
    public boolean isFinished()
    {
        return true;
    }
    
    public void finish()
    {
        finishConsumers.forEach(consumer -> consumer.apply(null));
    }
}
