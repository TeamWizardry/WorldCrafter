package com.teamwizardry.worldcrafter.manager;

import static com.teamwizardry.worldcrafter.WorldCrafter.explosionRecipes;
import static net.minecraftforge.event.TickEvent.Phase.END;

import java.util.HashMap;
import java.util.Map;

import com.teamwizardry.worldcrafter.core.EntityTracker;
import com.teamwizardry.worldcrafter.recipe.ExplosionRecipe;

import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExplosionRecipeManager extends RecipeManager
{
    private static Map<World, ExplosionRecipeManager> managers = new HashMap<>();
    
    public static ExplosionRecipeManager get(World world)
    {
        return managers.computeIfAbsent(world, k -> new ExplosionRecipeManager());
    }
    
    @SubscribeEvent
    public static void onWorldTick(WorldTickEvent event)
    {
        if (event.phase != END)
            return;
        
        RecipeManager manager = ExplosionRecipeManager.get(event.world);
        EntityTracker tracker = EntityTracker.get(event.world, ExplosionRecipe.class);
        
        manager.tickRecipes(event.world, tracker, explosionRecipes);
    }
}
