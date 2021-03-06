package com.teamwizardry.worldcrafter.jei;

import static com.teamwizardry.worldcrafter.jei.JEIPlugin.inputItemTooltips;
import static com.teamwizardry.worldcrafter.jei.JEIPlugin.outputTooltips;

import com.teamwizardry.worldcrafter.recipe.Recipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.util.ResourceLocation;

public abstract class BaseRecipeCategory<BaseRecipe extends Recipe> implements IRecipeCategory<BaseRecipe>
{
    protected static final int itemSize = 18;
    protected static final int width = 10 + itemSize*8 + 10;
    protected static final int height = 10 + itemSize*5 + 10;
    protected static final int centerline = width/2;
    
    protected final ResourceLocation UID;
    protected final IDrawableStatic background;
    protected final String localizedName;
    protected final IDrawable icon;
    
    protected final IDrawable slotDrawable;
    
    public BaseRecipeCategory(IGuiHelper helper, ResourceLocation UID, IDrawableStatic background, String localizedName, IDrawable icon)
    {
        this.UID = UID;
        this.background = background;
        this.localizedName = localizedName;
        this.icon = icon;
        this.slotDrawable = helper.getSlotDrawable();
    }
    
    @Override public ResourceLocation getUid() { return UID; }
    @Override public String getTitle() { return localizedName; }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }
    
    @Override
    public void setIngredients(BaseRecipe recipe, IIngredients ingredients)
    {
        ingredients.setInputLists(VanillaTypes.ITEM, recipe.getItems());
        ingredients.setInputLists(VanillaTypes.FLUID, recipe.getFluidIngredients());
        
        ingredients.setOutputLists(VanillaTypes.ITEM, recipe.getOutput().getMatchingItems());
        ingredients.setOutputLists(VanillaTypes.FLUID, recipe.getOutput().getMatchingFluids());
    }
    
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, BaseRecipe recipe, IIngredients ingredients)
    {
        int numInputs = recipe.getItemIngredients().size();
        
        IGuiItemStackGroup items = recipeLayout.getItemStacks();
        items.addTooltipCallback((slotIndex, isInput, stack, tooltip) -> {
            if (isInput)
                tooltip.addAll(inputItemTooltips(recipe.getItemIngredient(slotIndex)));
            else
                tooltip.addAll(outputTooltips(slotIndex - numInputs, recipe.getOutput()));
            tooltip.addAll(recipe.getTooltipLines(slotIndex, stack));
        });
    }
}
