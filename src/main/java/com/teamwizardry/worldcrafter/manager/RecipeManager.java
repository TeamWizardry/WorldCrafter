package com.teamwizardry.worldcrafter.manager;

import static com.teamwizardry.worldcrafter.WorldCrafter.entityStripper;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.teamwizardry.worldcrafter.core.EntityTracker;
import com.teamwizardry.worldcrafter.core.RecipeInfo;
import com.teamwizardry.worldcrafter.core.RecipeStorage;
import com.teamwizardry.worldcrafter.recipe.Recipe;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class RecipeManager
{
    protected Map<Long, RecipeInfo> existingRecipes;
    
    protected RecipeManager()
    {
        this.existingRecipes = new HashMap<>();
    }
    
    public <BaseRecipe extends Recipe> void tickRecipes(World world, EntityTracker tracker, RecipeStorage<BaseRecipe> storage)
    {
        List<Long> toRemove = new LinkedList<>();
        this.existingRecipes.forEach((pos, recipe) -> {
            if (recipe.isValid(tracker.getItems(pos)))
            {
                Collection<ItemEntity> items = tracker.getItems(pos);
                recipe.tick(items);
                if (recipe.isFinished(items))
                {
                    recipe.finish(items);
                    toRemove.add(pos);
                }
                tracker.clear(BlockPos.fromLong(pos));
            }
            else toRemove.add(pos);
        });
        
        for (long pos : toRemove)
            this.existingRecipes.remove(pos);
        toRemove.clear();
        
        tracker.forEach((pos, items) -> {
            BaseRecipe recipe = storage.getRecipe(entityStripper.apply(items));
            if (recipe != null)
            {
                RecipeInfo info = new RecipeInfo(world, BlockPos.fromLong(pos), recipe);
                if (recipe.getDuration() == 0)
                {
                    if (info.isValid(items))
                    {
                        info.tick(items);
                        info.finish(items);
                    }
                }
                else
                    this.existingRecipes.put(pos, info);
            }
        });
        tracker.clear();
    }
}
