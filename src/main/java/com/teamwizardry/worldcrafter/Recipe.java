package com.teamwizardry.worldcrafter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class Recipe implements IRecipe<IInventory>
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

    public List<List<ItemStack>> getItems()
    {
        return ingredients.stream().map(ItemIngredient::getMatchingItems).filter(items -> !items.isEmpty()).collect(Collectors.toList());
    }
    
    public List<List<FluidStack>> getFluidIngredients() { return Arrays.asList(); }
    public List<ItemIngredient> getItemIngredients() { return this.ingredients; }
    public ItemIngredient getItemIngredient(int index) { return this.ingredients.get(index); }
    public Output getOutput() { return this.output; }
    public int getDuration() { return this.duration; }
    
    public List<String> getTooltipLines(int index, ItemStack stack)
    {
        if (index >= ingredients.size()) index -= ingredients.size();
        ItemIngredient ingredient = ingredients.get(index);
        
        List<String> lines = new LinkedList<>();
        tickConsumers.keySet().stream().flatMap(consumer -> consumer.apply(this, ingredient, stack).stream()).forEach(lines::add);
        finishConsumers.stream().flatMap(consumer -> consumer.apply(this, ingredient, stack).stream()).forEach(lines::add);
        return lines;
    }
    
    /// Vanilla IRecipe boilerplate ///
    
    @Override public boolean matches(IInventory inv, World worldIn) { return false; }
    @Override public ItemStack getCraftingResult(IInventory inv) { return ItemStack.EMPTY; }
    @Override public boolean canFit(int width, int height) { return false; }
    @Override public ItemStack getRecipeOutput() { return ItemStack.EMPTY; }
    @Override public boolean isDynamic() { return true; }
    
    public static class Serializer<BaseRecipe extends Recipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BaseRecipe>
    {
        private final RecipeStorage<BaseRecipe> recipeStorage;
        
        public Serializer(RecipeStorage<BaseRecipe> recipeStorage)
        {
            this.recipeStorage = recipeStorage;
        }
        
        @Override
        public BaseRecipe read(ResourceLocation recipeId, JsonObject json)
        {
            JsonElement items = json.get("items");
            List<ItemStack> stacks = new ArrayList<>();
            for (JsonElement element : items.getAsJsonArray())
            {
                JsonObject obj = JSONUtils.getJsonObject(element, "item");
                stacks.add(ShapedRecipe.deserializeItem(obj));
            }
            return recipeStorage.getRecipe(stacks);
        }

        @Override
        public BaseRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
        {
            int numItems = buffer.readVarInt();
            List<ItemStack> stacks = new ArrayList<>(numItems);
            for (int i = 0; i < numItems; i++)
                stacks.add(buffer.readItemStack());
            return recipeStorage.getRecipe(stacks);
        }

        @Override
        public void write(PacketBuffer buffer, BaseRecipe recipe)
        {
            buffer.writeVarInt(recipe.getItemIngredients().size());
            for (ItemIngredient ingredient : recipe.getItemIngredients())
                buffer.writeItemStack(ingredient.getMatchingItems().get(0));
        }
    }
}
