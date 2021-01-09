package com.teamwizardry.worldcrafter.fluid;

import static com.teamwizardry.worldcrafter.WorldCrafter.entityStripper;
import static com.teamwizardry.worldcrafter.WorldCrafter.fluidRecipes;
import static net.minecraftforge.event.TickEvent.Phase.END;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.teamwizardry.worldcrafter.RecipeInfo;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FluidRecipeManager
{
    private static Map<World, FluidRecipeManager> managers = new HashMap<>();
    
    private Map<Long, RecipeInfo> existingRecipes;
    
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
            if (recipe.isValid(tracker.getItems(pos)))
            {
                List<ItemEntity> items = tracker.getItems(pos);
                recipe.tick(items);
                if (recipe.isFinished(items))
                {
                    recipe.finish(items);
                    toRemove.add(pos);
                }                
                tracker.clear(BlockPos.fromLong(pos));
            }
            else
                toRemove.add(pos);
        });
        
        for (Long pos : toRemove)
            manager.existingRecipes.remove(pos);
        toRemove.clear();
        
        tracker.forEach((pos, items) -> {
            FluidRecipe recipe = fluidRecipes.getRecipe(entityStripper.apply(items));
            if (recipe != null)
                manager.existingRecipes.put(pos, new RecipeInfo(event.world, BlockPos.fromLong(pos), recipe));
        });
        tracker.clear();
    }
}
