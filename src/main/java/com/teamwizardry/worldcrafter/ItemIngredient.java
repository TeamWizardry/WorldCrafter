package com.teamwizardry.worldcrafter;

import java.util.List;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemIngredient extends Ingredient<ItemIngredient>
{
    private final Item item;
    private final int count;
    private final double baseConsumeChance;
    private final int consumeCount;
    private final double consumeChance;
    
    public ItemIngredient(Item item, int count, double consumeChance)
    {
        this.item = item;
        this.count = count;
        this.baseConsumeChance = consumeChance;
        
        double totalConsumeCount = count * consumeChance;
        this.consumeCount = (int) (totalConsumeCount);
        this.consumeChance = totalConsumeCount - consumeCount;
    }
    
    public Item getItem() { return this.item; }
    
    public int getCount() { return this.count; }
    
    public double getConsumeChance() { return this.baseConsumeChance; }
    
    public void consume(List<ItemEntity> items)
    {
        int remaining = consumeCount;
        if (Math.random() < consumeChance)
            remaining++;
        
        for (ItemEntity entity : items)
        {
            ItemStack stack = entity.getItem();
            if (stack.getItem() != item)
                continue;
            if (remaining >= stack.getCount())
            {
                remaining -= stack.getCount();
                stack.shrink(stack.getCount());
            }
            else
            {
                stack.shrink(remaining);
                remaining = 0;
            }
            if (remaining == 0)
                return;
        }
    }
}
