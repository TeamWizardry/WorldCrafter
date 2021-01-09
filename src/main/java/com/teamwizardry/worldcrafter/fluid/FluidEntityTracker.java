package com.teamwizardry.worldcrafter.fluid;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidEntityTracker
{
    private static Map<World, FluidEntityTracker> trackers = new WeakHashMap<>();
    
    private Map<Long, List<ItemEntity>> items;
    
    private FluidEntityTracker()
    {
        this.items = new HashMap<>();
    }
    
    public static FluidEntityTracker get(World world)
    {
        return trackers.computeIfAbsent(world, k -> new FluidEntityTracker());
    }
    
    public static void addItem(World world, BlockPos pos, ItemEntity item)
    {
        FluidEntityTracker.get(world).addItem(pos, item);
    }
    
    public void addItem(BlockPos pos, ItemEntity item)
    {
        this.items.computeIfAbsent(pos.toLong(), k -> new LinkedList<>()).add(item);
    }
    
    public static List<ItemEntity> getItems(World world, BlockPos pos)
    {
        return FluidEntityTracker.get(world).getItems(pos);
    }
    
    public List<ItemEntity> getItems(BlockPos pos)
    {
        return this.items.getOrDefault(pos.toLong(), Collections.emptyList());
    }
    
    public static List<ItemEntity> getItems(World world, long pos)
    {
        return FluidEntityTracker.get(world).getItems(pos);
    }
    
    public List<ItemEntity> getItems(long pos)
    {
        return this.items.getOrDefault(pos, Collections.emptyList());
    }
    
    public static void clear(World world, BlockPos pos)
    {
        FluidEntityTracker.get(world).clear(pos);
    }
    
    public void clear(BlockPos pos)
    {
        this.items.remove(pos.toLong());
    }
    
    public static void clear(World world)
    {
        FluidEntityTracker.get(world).clear();
    }
    
    public void clear()
    {
        this.items.clear();
    }
    
    public static void forEach(World world, BiConsumer<Long, List<ItemEntity>> action)
    {
        FluidEntityTracker.get(world).forEach(action);
    }
    
    public void forEach(BiConsumer<Long, List<ItemEntity>> action)
    {
        items.forEach(action::accept);
    }
}
