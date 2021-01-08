package com.teamwizardry.worldcrafter;

import java.util.List;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RecipeInfo<SubRecipe extends Recipe>
{
    private final World world;
    private final BlockPos pos;
    private final SubRecipe recipe;
    private List<ItemEntity> entityItems;
    private int duration;
    
    public RecipeInfo(World world, BlockPos pos, SubRecipe recipe)
    {
        this.world = null;
        this.pos = pos;
        this.recipe = recipe;
    }
    
    public boolean isValid()
    {
        return recipe.isValid(world, pos);
    }
    
    public void tick()
    {
        duration++;
        recipe.tick();
    }
    
    public boolean isFinished()
    {
        return recipe.isFinished();
    }
    
    public void finish()
    {
        recipe.finish();
    }
    
    public World getWorld() { return world; }
    
    public BlockPos getPos() { return pos; }
    
    public SubRecipe getRecipe() { return recipe; }
    
    public int getCurrentDuration() { return duration; }
}
