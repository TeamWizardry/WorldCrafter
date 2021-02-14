package com.teamwizardry.worldcrafter.manager;

import static com.teamwizardry.worldcrafter.WorldCrafter.fluidRecipes;
import static net.minecraftforge.event.TickEvent.Phase.END;

import java.util.HashMap;
import java.util.Map;

import com.teamwizardry.worldcrafter.core.EntityTracker;
import com.teamwizardry.worldcrafter.recipe.FluidRecipe;

import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FluidRecipeManager extends RecipeManager
{
    private static Map<World, FluidRecipeManager> managers = new HashMap<>();
    
    public static FluidRecipeManager get(World world)
    {
        return managers.computeIfAbsent(world, k -> new FluidRecipeManager());
    }
    
    @SubscribeEvent
    public static void onWorldTick(WorldTickEvent event)
    {
        if (event.phase != END)
            return;
        
        RecipeManager manager = FluidRecipeManager.get(event.world);
        EntityTracker tracker = EntityTracker.get(event.world, FluidRecipe.class);
        
        manager.tickRecipes(event.world, tracker, fluidRecipes);
    }
}
