package com.teamwizardry.worldcrafter.loading.output;

import java.util.Map;

import com.teamwizardry.worldcrafter.ingredient.output.ItemOutput;
import com.teamwizardry.worldcrafter.loading.Loader;
import com.teamwizardry.worldcrafter.loading.NBTLoader;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemOutputLoader extends Loader<ItemOutput>
{
    public static final ItemOutputLoader INSTANCE = new ItemOutputLoader();
    
    protected static final String ITEM = "item";
    protected static final String COUNT = "count";
    protected static final String MIN = "min";
    protected static final String MAX = "max";
    protected static final String NBT = "nbt";
    
    private ItemOutputLoader() {}
    
    @Override
    @SuppressWarnings("unchecked")
    public ItemOutput load(Map<String, Object> yaml)
    {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation((String) yaml.get(ITEM)));
        int count = Loader.loadInt(yaml, COUNT, 1);
        int min = Loader.loadInt(yaml, MIN, 1);
        int max = Loader.loadInt(yaml, MAX, 1);
        CompoundNBT nbt = NBTLoader.INSTANCE.load((Map<String, Object>) yaml.get(NBT));
        
        if (min == max && min == 1)
            return new ItemOutput(item, count, count, nbt);
        return new ItemOutput(item, min, max, nbt);
    }
}
