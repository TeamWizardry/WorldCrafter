package com.teamwizardry.worldcrafter.manager;

import static com.teamwizardry.worldcrafter.WorldCrafter.fireRecipes;
import static net.minecraftforge.event.TickEvent.Phase.END;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.teamwizardry.worldcrafter.core.EntityTracker;
import com.teamwizardry.worldcrafter.core.RecipeInfo;
import com.teamwizardry.worldcrafter.core.RecipeStorage;
import com.teamwizardry.worldcrafter.recipe.FireRecipe;
import com.teamwizardry.worldcrafter.recipe.Recipe;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FireRecipeManager extends RecipeManager
{
    private static Map<World, FireRecipeManager> managers = new HashMap<>();
    
    protected Map<Long, Map<Item, RecipeInfo>> existingRecipeMaps;
    
    private FireRecipeManager()
    {
        this.existingRecipeMaps = new HashMap<>();
    }
    
    public static FireRecipeManager get(World world)
    {
        return managers.computeIfAbsent(world, k -> new FireRecipeManager());
    }
    
    @SubscribeEvent
    public static void onWorldTick(WorldTickEvent event)
    {
        if (event.phase != END)
            return;
        FireRecipeManager manager = FireRecipeManager.get(event.world);
        EntityTracker tracker = EntityTracker.get(event.world, FireRecipe.class);
        
        manager.tickRecipes(event.world, tracker, fireRecipes);
    }
    
    @Override
    public <BaseRecipe extends Recipe> void tickRecipes(World world, EntityTracker tracker, RecipeStorage<BaseRecipe> storage)
    {
        List<Long> posToRemove = new LinkedList<>();
        this.existingRecipeMaps.forEach((pos, recipeMap) -> {
            List<Item> itemsToRemove = new LinkedList<>();
            recipeMap.forEach((item, recipe) -> {
                if (recipe.isValid(tracker.getItems(pos)))
                {
                    Collection<ItemEntity> items = tracker.getItems(pos);
                    recipe.tick(items);
                    if (recipe.isFinished(items))
                    {
                        recipe.finish(items);
                        itemsToRemove.add(item);
                    }
                }
                else itemsToRemove.add(item);
            });
            
            for (Item item : itemsToRemove)
                recipeMap.remove(item);
            itemsToRemove.clear();
            
            if (recipeMap.isEmpty())
                posToRemove.add(pos);
        });
        
        for (long pos : posToRemove)
            this.existingRecipeMaps.remove(pos);
        posToRemove.clear();
        
        tracker.forEach((pos, items) -> {
            Map<Item, RecipeInfo> recipeMap = this.existingRecipeMaps.computeIfAbsent(pos, k -> new HashMap<>());
            for (ItemEntity entity : items)
            {
                ItemStack stack = entity.getItem();
                Item item = stack.getItem();
                if (recipeMap.containsKey(item))
                    continue;
                BaseRecipe recipe = storage.getRecipe(Arrays.asList(stack));
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
                        recipeMap.put(item, info);
                }
            }
        });
        tracker.clear();
    }
}
