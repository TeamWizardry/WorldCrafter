package com.teamwizardry.worldcrafter.jei;

import static com.teamwizardry.worldcrafter.WorldCrafter.explosionRecipes;
import static com.teamwizardry.worldcrafter.WorldCrafter.fireRecipes;
import static com.teamwizardry.worldcrafter.WorldCrafter.fluidRecipes;
import static com.teamwizardry.worldcrafter.WorldCrafter.lightningRecipes;

import java.util.Arrays;
import java.util.Collection;

import com.teamwizardry.worldcrafter.WorldCrafter;
import com.teamwizardry.worldcrafter.ingredient.FluidIngredient;
import com.teamwizardry.worldcrafter.ingredient.ItemIngredient;
import com.teamwizardry.worldcrafter.ingredient.output.ItemOutput;
import com.teamwizardry.worldcrafter.ingredient.output.Output;
import com.teamwizardry.worldcrafter.recipe.ExplosionRecipe;
import com.teamwizardry.worldcrafter.recipe.FireRecipe;
import com.teamwizardry.worldcrafter.recipe.FluidRecipe;
import com.teamwizardry.worldcrafter.recipe.LightningRecipe;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

@JeiPlugin
public class JEIPlugin implements IModPlugin
{
    @Override
    public ResourceLocation getPluginUid() { return WorldCrafter.location("plugin"); }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        
        registry.addRecipeCategories(new FluidRecipeCategory(guiHelper),
                                     new FireRecipeCategory(guiHelper),
                                     new ExplosionRecipeCategory(guiHelper),
                                     new LightningRecipeCategory(guiHelper));
    }
    
    @Override
    public void registerRecipes(IRecipeRegistration registry)
    {
        registry.addRecipes(fluidRecipes.getRecipes(), FluidRecipe.UID);
        registry.addRecipes(fireRecipes.getRecipes(), FireRecipe.UID);
        registry.addRecipes(explosionRecipes.getRecipes(), ExplosionRecipe.UID);
        registry.addRecipes(lightningRecipes.getRecipes(), LightningRecipe.UID);
    }
    
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry)
    {
        fluidRecipes.getRecipes().stream().map(FluidRecipe::getFluid)
                                                       .map(FluidIngredient::getFluid)
                                                       .distinct()
                                                       .map(Fluid::getFilledBucket)
                                                       .map(ItemStack::new)
                                                       .forEach(stack -> registry.addRecipeCatalyst(stack, FluidRecipe.UID));
        
        registry.addRecipeCatalyst(new ItemStack(Items.FLINT_AND_STEEL), FireRecipe.UID);
        registry.addRecipeCatalyst(new ItemStack(Items.FIRE_CHARGE), FireRecipe.UID);
        
        registry.addRecipeCatalyst(new ItemStack(Blocks.TNT), ExplosionRecipe.UID);
    }
    
    public static Collection<ITextComponent> inputItemTooltips(ItemIngredient ingredient)
    {
        double consumeChance = ingredient.getConsumeChance();
        if (consumeChance > 0 && consumeChance < 1)
            return Arrays.asList(new StringTextComponent(I18n.format("worldcrafter.item.consumeChance", (int) (consumeChance*100))));
        else if ((int) Math.ceil(consumeChance) == 0)
            return Arrays.asList(new StringTextComponent(I18n.format("worldcrafter.item.noConsume")));
        return Arrays.asList();
    }
    
    public static Collection<ITextComponent> inputFluidTooltips(FluidIngredient ingredient)
    {
        double consumeChance = ingredient.getConsumeChance();
        if (consumeChance > 0 && consumeChance < 1)
            return Arrays.asList(new StringTextComponent(I18n.format("worldcrafter.fluid.consumeChance", (int) (consumeChance*100))));
        else if ((int) consumeChance == 1)
            return Arrays.asList(new StringTextComponent(I18n.format("worldcrafter.fluid.consumed")));
        return Arrays.asList();
    }
    
    public static Collection<ITextComponent> outputTooltips(int outputIndex, Output output)
    {
        if (outputIndex > 0 || output.getItemOutputs() != null)
        {
            ItemOutput item = output.getItemOutput(outputIndex);
            int min = item.getMin();
            int max = item.getMax();
            if (min != max)
                return Arrays.asList(new StringTextComponent(I18n.format("worldcrafter.output.range", min, max)));
        }
        return Arrays.asList();
    }
}
