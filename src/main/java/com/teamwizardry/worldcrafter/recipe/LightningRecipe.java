package com.teamwizardry.worldcrafter.recipe;

import static com.teamwizardry.worldcrafter.WorldCrafter.lightningSerializer;
import static com.teamwizardry.worldcrafter.WorldCrafter.location;
import static net.minecraft.util.registry.Registry.RECIPE_TYPE;

import java.util.List;

import com.teamwizardry.worldcrafter.ingredient.ItemIngredient;
import com.teamwizardry.worldcrafter.ingredient.output.Output;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class LightningRecipe extends Recipe
{
    public static final ResourceLocation UID = location("lightning_recipe");
    
    public LightningRecipe(List<ItemIngredient> ingredients, Output output, CompoundNBT extraData)
    {
        super(ingredients, output, true, extraData);
    }
    
    @Override public ResourceLocation getId() { return UID; }

    @Override public IRecipeSerializer<?> getSerializer() { return lightningSerializer; }

    @Override public IRecipeType<?> getType() { return RECIPE_TYPE.getValue(UID).get(); }
}
