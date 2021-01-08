package com.teamwizardry.worldcrafter;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;

public class ItemOutput extends Ingredient<ItemOutput>
{
    private final Item item;
    private final int min;
    private final int max;
    private final CompoundNBT nbt;
    
    public ItemOutput(Item item, int min, int max, CompoundNBT nbt)
    {
        this.item = item;
        this.min = min;
        this.max = max;
        this.nbt = nbt;
    }
    
    public Item getItem() { return this.item; }
    
    public int getMin() { return this.min; }
    
    public int getMax() { return this.max; }
    
    public CompoundNBT getNBT() { return this.nbt; }
}
