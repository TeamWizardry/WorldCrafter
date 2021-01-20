package com.teamwizardry.worldcrafter.jei;

import static com.teamwizardry.worldcrafter.jei.JEIPlugin.inputFluidTooltips;

import java.util.List;

import com.teamwizardry.worldcrafter.fluid.FluidRecipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FluidRecipeCategory extends BaseRecipeCategory<FluidRecipe>
{
    private static final int itemSize = 18;
    private static final int width = 10 + itemSize*8 + 10;
    private static final int height = 10 + itemSize*5 + 10;
    private static final int centerline = width/2;
    
    public FluidRecipeCategory(IGuiHelper guiHelper)
    {
        super(FluidRecipe.UID,
              guiHelper.createBlankDrawable(width, height),
              I18n.format("worldcrafter.jei.fluidRecipe"),
              guiHelper.createDrawableIngredient(new ItemStack(Blocks.WATER)));
    }
    
    @Override public Class<? extends FluidRecipe> getRecipeClass() { return FluidRecipe.class; }
    
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FluidRecipe recipe, IIngredients ingredients)
    {
        IGuiItemStackGroup items = recipeLayout.getItemStacks();
        IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();
        
        fluids.addTooltipCallback((slotIndex, isInput, stack, tooltip) -> 
            tooltip.addAll(inputFluidTooltips(recipe.getFluid()))
        );
        
        int numInputs = recipe.getIngredients().size();
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
        
        FluidStack fluid = ingredients.getInputs(VanillaTypes.FLUID).get(0).get(0);
        fluids.init(0, true, centerline - itemSize, 10 + totalRows * itemSize + itemSize, itemSize*2, itemSize*2, fluid.getAmount(), true, null);
        fluids.set(0, ingredients.getInputs(VanillaTypes.FLUID).get(0));
    }
}
