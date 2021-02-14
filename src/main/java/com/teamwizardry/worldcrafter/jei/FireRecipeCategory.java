package com.teamwizardry.worldcrafter.jei;

import java.util.List;

import com.teamwizardry.worldcrafter.recipe.FireRecipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class FireRecipeCategory extends BaseRecipeCategory<FireRecipe>
{
    public FireRecipeCategory(IGuiHelper guiHelper)
    {
        super(guiHelper,
              FireRecipe.UID,
              guiHelper.createBlankDrawable(width, height),
              I18n.format("worldcrafter.jei.fireRecipe"),
              guiHelper.createDrawableIngredient(new ItemStack(Items.FLINT_AND_STEEL)));
    }
    
    @Override public Class<? extends FireRecipe> getRecipeClass() { return FireRecipe.class; }
    
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FireRecipe recipe, IIngredients ingredients)
    {
        super.setRecipe(recipeLayout, recipe, ingredients);
        
        IGuiItemStackGroup items = recipeLayout.getItemStacks();
        
        int numOutputs = items.getGuiIngredients().size() - 1;
        int outputRows = (numOutputs+1)/2;
        
        int totalRows = Math.max(1, outputRows);
        
        int x = centerline - itemSize*3;
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
