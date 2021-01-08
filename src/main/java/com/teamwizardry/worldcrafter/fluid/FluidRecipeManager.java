package com.teamwizardry.worldcrafter.fluid;

import static net.minecraftforge.event.TickEvent.Phase.END;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.teamwizardry.worldcrafter.RecipeInfo;
import com.teamwizardry.worldcrafter.WorldCrafter;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FluidRecipeManager
{
    private static Map<World, FluidRecipeManager> managers = new HashMap<>();
    
    private Map<Long, RecipeInfo<FluidRecipe>> existingRecipes;
    
    private FluidRecipeManager()
    {
        this.existingRecipes = new HashMap<>();
    }
    
    public static FluidRecipeManager get(World world)
    {
        return managers.computeIfAbsent(world, k -> new FluidRecipeManager());
    }
    
    @SubscribeEvent
    public static void onWorldTick(WorldTickEvent event)
    {
        if (event.phase != END)
            return;
        FluidRecipeManager manager = FluidRecipeManager.get(event.world);
        FluidEntityTracker tracker = FluidEntityTracker.get(event.world);
        
        List<Long> toRemove = new LinkedList<>();
        manager.existingRecipes.forEach((pos, recipe) -> {
            if (recipe.isValid())
                tracker.clear(BlockPos.fromLong(pos));
            else
                toRemove.add(pos);
        });
        
        for (Long pos : toRemove)
            manager.existingRecipes.remove(pos);
        toRemove.clear();
        
        manager.existingRecipes.forEach((pos, recipe) -> {
            recipe.tick();
            if (recipe.isFinished())
            {
                recipe.finish();
                toRemove.add(pos);
            }
        });
        
        for (Long pos : toRemove)
            manager.existingRecipes.remove(pos);
        toRemove.clear();
        
        tracker.forEach((pos, items) -> manager.existingRecipes.put(pos, new RecipeInfo<FluidRecipe>(event.world, BlockPos.fromLong(pos), WorldCrafter.fluidRecipes.getRecipe(items))));
        tracker.clear();
    }
}
