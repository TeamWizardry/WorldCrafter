package com.teamwizardry.worldcrafter.manager;

import static com.teamwizardry.worldcrafter.WorldCrafter.lightningRecipes;
import static net.minecraftforge.event.TickEvent.Phase.END;

import java.util.HashMap;
import java.util.Map;

import com.teamwizardry.worldcrafter.core.EntityTracker;
import com.teamwizardry.worldcrafter.recipe.LightningRecipe;

import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LightningRecipeManager extends RecipeManager
{
    private static Map<World, LightningRecipeManager> managers = new HashMap<>();
    
    public static LightningRecipeManager get(World world)
    {
        return managers.computeIfAbsent(world, k -> new LightningRecipeManager());
    }
    
    @SubscribeEvent
    public static void onWorldTick(WorldTickEvent event)
    {
        if (event.phase != END)
            return;
        
        RecipeManager manager = LightningRecipeManager.get(event.world);
        EntityTracker tracker = EntityTracker.get(event.world, LightningRecipe.class);
        
        manager.tickRecipes(event.world, tracker, lightningRecipes);
    }
}
