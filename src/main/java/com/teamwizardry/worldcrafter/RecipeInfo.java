package com.teamwizardry.worldcrafter;

import static com.teamwizardry.worldcrafter.WorldCrafter.entityStripper;

import java.util.List;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RecipeInfo
{
    private final World world;
    private final BlockPos pos;
    private final Recipe recipe;
    private int duration;
    
    public RecipeInfo(World world, BlockPos pos, Recipe recipe)
    {
        this.world = world;
        this.pos = pos;
        this.recipe = recipe;
    }
    
    public boolean isValid(List<ItemEntity> items)
    {
        return recipe.isValid(this, entityStripper.apply(items));
    }
    
    public void tick(List<ItemEntity> items)
    {
        duration++;
        recipe.tick(this, items);
    }
    
    public boolean isFinished(List<ItemEntity> items)
    {
        return recipe.isFinished(this, items);
    }
    
    public void finish(List<ItemEntity> items)
    {
        recipe.finish(this, items);
    }
    
    public World getWorld() { return world; }
    
    public BlockPos getPos() { return pos; }
    
    public Recipe getRecipe() { return recipe; }
    
    public int getCurrentDuration() { return duration; }
}
