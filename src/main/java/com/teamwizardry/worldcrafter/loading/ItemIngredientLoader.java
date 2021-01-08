package com.teamwizardry.worldcrafter.loading;

import java.util.Map;

import com.teamwizardry.worldcrafter.ItemIngredient;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemIngredientLoader extends Loader<ItemIngredient>
{
    public static final ItemIngredientLoader INSTANCE = new ItemIngredientLoader();
    
    protected static final String ITEM = "item";
    protected static final String COUNT = "count";
    protected static final String CHANCE = "chance";
    protected static final String NBT = "nbt";
    
    private ItemIngredientLoader() {}
    
    @Override
    public ItemIngredient load(Map<String, Object> yaml)
    {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation((String) yaml.get(ITEM)));
        int count = Loader.loadInt(yaml, COUNT, 1);
        double chance = Loader.loadDouble(yaml, CHANCE, 1);
        
        return new ItemIngredient(item, count, chance);
    }
}
