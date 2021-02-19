package com.teamwizardry.worldcrafter.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;

import com.teamwizardry.worldcrafter.recipe.ExplosionRecipe;
import com.teamwizardry.worldcrafter.recipe.FireRecipe;
import com.teamwizardry.worldcrafter.recipe.FluidRecipe;
import com.teamwizardry.worldcrafter.recipe.LightningRecipe;
import com.teamwizardry.worldcrafter.recipe.Recipe;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityTracker
{
    protected static Map<Class<? extends Recipe>, Map<World, EntityTracker>> trackers = new HashMap<>();
    
    static
    {
        trackers.put(FluidRecipe.class, new WeakHashMap<>());
        trackers.put(FireRecipe.class, new WeakHashMap<>());
        trackers.put(ExplosionRecipe.class, new WeakHashMap<>());
        trackers.put(LightningRecipe.class, new WeakHashMap<>());
    }
    
    public static EntityTracker get(World world, Class<? extends Recipe> cls)
    {
        return trackers.get(cls).computeIfAbsent(world, k -> new EntityTracker());
    }
    
    protected Map<Long, Set<ItemEntity>> items;
    
    protected EntityTracker()
    {
        this.items = new HashMap<>();
        
    }
    
    public void addItem(BlockPos pos, ItemEntity item)
    {
        this.items.computeIfAbsent(pos.toLong(), k -> new HashSet<>()).add(item);
    }
    
    public Set<ItemEntity> getItems(BlockPos pos)
    {
        return this.items.getOrDefault(pos.toLong(), Collections.emptySet());
    }
    
    public Set<ItemEntity> getItems(long pos)
    {
        return this.items.getOrDefault(pos, Collections.emptySet());
    }
    
    public void clear(BlockPos pos)
    {
        this.items.remove(pos.toLong());
    }
    
    public void clear()
    {
        this.items.clear();
    }
    
    public void forEach(BiConsumer<Long, Set<ItemEntity>> action)
    {
        items.forEach(action::accept);
    }
}
