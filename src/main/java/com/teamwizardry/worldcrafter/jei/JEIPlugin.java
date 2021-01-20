package com.teamwizardry.worldcrafter.jei;

import java.util.Arrays;
import java.util.List;

import com.teamwizardry.worldcrafter.ItemIngredient;
import com.teamwizardry.worldcrafter.ItemOutput;
import com.teamwizardry.worldcrafter.Output;
import com.teamwizardry.worldcrafter.WorldCrafter;
import com.teamwizardry.worldcrafter.fluid.FluidIngredient;
import com.teamwizardry.worldcrafter.fluid.FluidRecipe;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.resources.I18n;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEIPlugin implements IModPlugin
{
    @Override
    public ResourceLocation getPluginUid() { return WorldCrafter.location("plugin"); }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        
        registry.addRecipeCategories(new FluidRecipeCategory(guiHelper));
    }
    
    @Override
    public void registerRecipes(IRecipeRegistration registry)
    {
        registry.addRecipes(WorldCrafter.fluidRecipes.getRecipes(), FluidRecipe.UID);
    }
    
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry)
    {
        WorldCrafter.fluidRecipes.getRecipes().stream().map(FluidRecipe::getFluid)
                                                       .map(FluidIngredient::getFluid)
                                                       .distinct()
                                                       .map(Fluid::getFilledBucket)
                                                       .map(ItemStack::new)
                                                       .forEach(stack -> registry.addRecipeCatalyst(stack, FluidRecipe.UID));
    }
    
    public static List<String> inputItemTooltips(ItemIngredient ingredient)
    {
        double consumeChance = ingredient.getConsumeChance();
        if (consumeChance > 0 && consumeChance < 1)
            return Arrays.asList(I18n.format("worldcrafter.item.consumeChance", (int) (consumeChance*100)));
        else if ((int) Math.ceil(consumeChance) == 0)
            return Arrays.asList(I18n.format("worldcrafter.item.noConsume"));
        return Arrays.asList();
    }
    
    public static List<String> inputFluidTooltips(FluidIngredient ingredient)
    {
        double consumeChance = ingredient.getConsumeChance();
        if (consumeChance > 0 && consumeChance < 1)
            return Arrays.asList(I18n.format("worldcrafter.fluid.consumeChance", (int) (consumeChance*100)));
        else if ((int) consumeChance == 1)
            return Arrays.asList(I18n.format("worldcrafter.fluid.consumed"));
        return Arrays.asList();
    }
    
    public static List<String> outputTooltips(int outputIndex, Output output)
    {
        if (outputIndex > 0 || output.getItemOutputs() != null)
        {
            ItemOutput item = output.getItemOutput(outputIndex);
            int min = item.getMin();
            int max = item.getMax();
            if (min != max)
                return Arrays.asList(I18n.format("worldcrafter.output.range", min, max));
        }
        return Arrays.asList();
    }
}
