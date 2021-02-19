package com.teamwizardry.worldcrafter.jei;

import java.util.List;

import com.teamwizardry.worldcrafter.recipe.LightningRecipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class LightningRecipeCategory extends BaseRecipeCategory<LightningRecipe>
{
    public LightningRecipeCategory(IGuiHelper guiHelper)
    {
        super(guiHelper,
              LightningRecipe.UID,
              guiHelper.createBlankDrawable(width, height),
              I18n.format("worldcrafter.jei.lightningRecipe"),
              guiHelper.createDrawableIngredient(new ItemStack(Blocks.TNT)));
    }
    
    @Override public Class<? extends LightningRecipe> getRecipeClass() { return LightningRecipe.class; }
    
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, LightningRecipe recipe, IIngredients ingredients)
    {
        super.setRecipe(recipeLayout, recipe, ingredients);
        
        IGuiItemStackGroup items = recipeLayout.getItemStacks();
        
        int numInputs = recipe.getItemIngredients().size();
        int numOutputs = items.getGuiIngredients().size() - numInputs;
        int inputRows = (numInputs+1)/2;
        int outputRows = (numOutputs+1)/2;
        
        int totalRows = Math.max(inputRows, outputRows);
        
        int x = centerline - itemSize*3;
        if (numInputs > 1) x -= itemSize;
        int y = 10 + totalRows * itemSize;
        int index = 0;
        for (List<ItemStack> list : ingredients.getInputs(VanillaTypes.ITEM))
        {
            items.init(index, true, x, y);
            items.set(index, list);
            if (index % 2 == 0)
                x -= itemSize;
            else
            {
                x += itemSize;
                y -= itemSize;
            }
            index++;
        }
        
        x = centerline + itemSize*2;
        y = 10 + totalRows * itemSize;
        for (List<ItemStack> list : ingredients.getOutputs(VanillaTypes.ITEM))
        {
            items.init(index, false, x, y);
            items.set(index, list);
            if (index % 2 == 0)
                x += 18;
            else
            {
                x -= 18;
                y -= 18;
            }
            index++;
        }
    }
}
