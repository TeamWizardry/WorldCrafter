package com.teamwizardry.worldcrafter.loading.ingredient;

import java.util.Map;

import com.teamwizardry.worldcrafter.ingredient.ItemIngredient;
import com.teamwizardry.worldcrafter.loading.Loader;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemIngredientLoader extends Loader<ItemIngredient>
{
    public static final ItemIngredientLoader INSTANCE = new ItemIngredientLoader();
    
    protected static final String ITEM = "item";
    protected static final String TAG = "tag";
    protected static final String COUNT = "count";
    protected static final String CHANCE = "consumeChance";
    protected static final String NBT = "nbt";
    
    private ItemIngredientLoader() {}
    
    @Override
    public ItemIngredient load(Map<String, Object> yaml)
    {
        int count = Loader.loadInt(yaml, COUNT, 1);
        double chance = MathHelper.clamp(Loader.loadDouble(yaml, CHANCE, 1), 0, 1);
        
        if (yaml.containsKey(TAG))
        {
            Tag<Item> tag = ItemTags.getCollection().get(new ResourceLocation((String) yaml.get(TAG)));
            if (tag == null)
                throw new IllegalArgumentException("No Item Tag " + yaml.get(TAG) + " found.");
            return new ItemIngredient(tag, count, chance);
        }
        else if (yaml.containsKey(ITEM))
        {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation((String) yaml.get(ITEM)));
            return new ItemIngredient(item, count, chance);
        }
        else throw new IllegalArgumentException("Item Ingredient missing required identifier: `tag` or `item` ResourceLocation");
    }
}
