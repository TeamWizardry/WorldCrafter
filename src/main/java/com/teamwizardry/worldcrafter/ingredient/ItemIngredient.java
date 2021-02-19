package com.teamwizardry.worldcrafter.ingredient;

import static com.teamwizardry.worldcrafter.ingredient.ItemIngredient.ItemType.ITEM;
import static com.teamwizardry.worldcrafter.ingredient.ItemIngredient.ItemType.TAG;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;

public class ItemIngredient extends Ingredient<ItemIngredient>
{
    public enum ItemType
    {
        ITEM,
        TAG
    }
    
    private final ItemType type;
    private final Item item;
    private final ITag<Item> tag;
    private final int count;
    private final double baseConsumeChance;
    private final int consumeCount;
    private final double consumeChance;
    
    public ItemIngredient(Item item, int count, double consumeChance)
    {
        this.type = ITEM;
        this.item = item;
        this.tag = null;
        
        this.count = count;
        this.baseConsumeChance = consumeChance;
        
        double totalConsumeCount = count * consumeChance;
        this.consumeCount = (int) totalConsumeCount;
        this.consumeChance = totalConsumeCount - consumeCount;
    }
    
    public ItemIngredient(ITag<Item> tag, int count, double consumeChance)
    {
        this.type = TAG;
        this.item = null;
        this.tag = tag;
        
        this.count = count;
        this.baseConsumeChance = consumeChance;
        
        double totalConsumeCount = count * consumeChance;
        this.consumeCount = (int) totalConsumeCount;
        this.consumeChance = totalConsumeCount - consumeCount;
    }
    
    public Item getItem() { return this.item; }
    
    public ITag<Item> getTag() { return this.tag; }
    
    public int getCount() { return this.count; }
    
    public double getConsumeChance() { return this.baseConsumeChance; }
    
    public void consume(Collection<ItemEntity> items)
    {
        int remaining = consumeCount;
        if (Math.random() < consumeChance)
            remaining++;
        
        for (ItemEntity entity : items)
        {
            ItemStack stack = entity.getItem();
            switch (type)
            {
                case ITEM: if (stack.getItem() != item) continue; break;
                case TAG: if (!tag.contains(stack.getItem())) continue; break;
            }
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
    
    @Override
    public List<ItemStack> getMatchingItems()
    {
        switch (type)
        {
            case ITEM: return Arrays.asList(new ItemStack(item, count));
            case TAG: return tag.getAllElements().stream().map(item -> new ItemStack(item, count)).collect(Collectors.toList());
        }
        return super.getMatchingItems();
    }
}
