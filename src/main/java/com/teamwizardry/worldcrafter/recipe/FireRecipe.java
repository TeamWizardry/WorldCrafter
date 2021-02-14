package com.teamwizardry.worldcrafter.recipe;

import static com.teamwizardry.worldcrafter.WorldCrafter.entityStripper;
import static com.teamwizardry.worldcrafter.WorldCrafter.fireSerializer;
import static com.teamwizardry.worldcrafter.WorldCrafter.location;
import static net.minecraft.util.registry.Registry.RECIPE_TYPE;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.teamwizardry.worldcrafter.core.RecipeInfo;
import com.teamwizardry.worldcrafter.ingredient.ItemIngredient;
import com.teamwizardry.worldcrafter.ingredient.output.Output;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class FireRecipe extends Recipe
{
    public static final ResourceLocation UID = location("fire_recipe");
    
    protected ItemIngredient ingredient;
    
    public FireRecipe(ItemIngredient ingredient, Output output, int duration, boolean isParallel, CompoundNBT extraData)
    {
        super(Arrays.asList(), output, isParallel, extraData);
        this.ingredient = ingredient;
        this.duration = duration;
    }
    
    @Override
    public boolean matches(Collection<ItemStack> items)
    {
        List<ItemStack> internal = items.stream().map(ItemStack::copy).collect(Collectors.toList());
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
                    item.shrink(count);;
                    count = 0;
                }
                
                if (count <= 0)
                    break;
            }
        }
        return count <= 0;
    }
    
    @Override
    public void finish(RecipeInfo info, Collection<ItemEntity> items)
    {
        finishConsumers.forEach(consumer -> consumer.apply(info, items));
        do
        {
            ingredient.consume(items);
            output.createOutput(info.getWorld(), info.getPos(), true);
        }
        while (isParallel && this.matches(entityStripper.apply(items)));
    }
    
    @Override
    public List<String> getTooltipLines(int index, ItemStack stack)
    {
        List<String> lines = new LinkedList<>();
        tickConsumers.keySet().stream().flatMap(consumer -> consumer.apply(this, ingredient, stack).stream()).forEach(lines::add);
        finishConsumers.stream().flatMap(consumer -> consumer.apply(this, ingredient, stack).stream()).forEach(lines::add);
        return lines;
    }
    
    @Override
    public List<List<ItemStack>> getItems()
    {
        return Arrays.asList(ingredient.getMatchingItems());
    }
    
    @Override public List<ItemIngredient> getItemIngredients() { return Arrays.asList(ingredient); }
    @Override public ItemIngredient getItemIngredient(int index) { return this.ingredient; }
    
    @Override public ResourceLocation getId() { return UID; }

    @Override public IRecipeSerializer<?> getSerializer() { return fireSerializer; }

    @Override public IRecipeType<?> getType() { return RECIPE_TYPE.getValue(UID).get(); }
}
