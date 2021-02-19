package com.teamwizardry.worldcrafter.core;

import com.teamwizardry.worldcrafter.recipe.ExplosionRecipe;
import com.teamwizardry.worldcrafter.recipe.LightningRecipe;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onExplosion(ExplosionEvent.Detonate event)
    {
        World world = event.getWorld();
        event.getAffectedEntities().stream().filter(entity -> entity instanceof ItemEntity).forEach(entity ->
            EntityTracker.get(world, ExplosionRecipe.class).addItem(entity.getPosition(), (ItemEntity) entity)
        );
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLightningStrike(EntityStruckByLightningEvent event)
    {
        Entity entity = event.getEntity();
        if (entity instanceof ItemEntity)
        {
            EntityTracker.get(entity.getEntityWorld(), LightningRecipe.class).addItem(entity.getPosition(), (ItemEntity) entity);
            if (entity.isInvulnerable())
                event.setCanceled(true);
        }
    }
}
