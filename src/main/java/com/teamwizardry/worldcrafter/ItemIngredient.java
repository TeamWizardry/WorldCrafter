package com.teamwizardry.worldcrafter;

import net.minecraft.item.Item;

public class ItemIngredient extends Ingredient<ItemIngredient>
{
    private final Item item;
    private final int count;
    private final double consumeChance;
    
    public ItemIngredient(Item item, int count, double consumeChance)
    {
        this.item = item;
        this.count = count;
        this.consumeChance = consumeChance;
    }
    
    public Item getItem() { return this.item; }
    
    public int getCount() { return this.count; }
    
    public double getConsumeChance() { return this.consumeChance; }
}
