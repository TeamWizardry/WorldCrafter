package com.teamwizardry.worldcrafter;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.teamwizardry.worldcrafter.core.EventHandler;
import com.teamwizardry.worldcrafter.core.RecipeConsumer;
import com.teamwizardry.worldcrafter.core.RecipeStorage;
import com.teamwizardry.worldcrafter.loading.recipe.RecipeLoader;
import com.teamwizardry.worldcrafter.manager.ExplosionRecipeManager;
import com.teamwizardry.worldcrafter.manager.FireRecipeManager;
import com.teamwizardry.worldcrafter.manager.FluidRecipeManager;
import com.teamwizardry.worldcrafter.manager.LightningRecipeManager;
import com.teamwizardry.worldcrafter.recipe.ExplosionRecipe;
import com.teamwizardry.worldcrafter.recipe.FireRecipe;
import com.teamwizardry.worldcrafter.recipe.FluidRecipe;
import com.teamwizardry.worldcrafter.recipe.LightningRecipe;
import com.teamwizardry.worldcrafter.recipe.Recipe;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

@Mod(WorldCrafter.MODID)
public class WorldCrafter
{
    public static final String MODID = "worldcrafter";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    
    public static final RecipeStorage<FluidRecipe> fluidRecipes = new RecipeStorage<>();
    public static final IRecipeSerializer<FluidRecipe> fluidSerializer = new Recipe.Serializer<>(fluidRecipes);
    
    public static final RecipeStorage<FireRecipe> fireRecipes = new RecipeStorage<>();
    public static final IRecipeSerializer<FireRecipe> fireSerializer = new Recipe.Serializer<>(fireRecipes);
    
    public static final RecipeStorage<ExplosionRecipe> explosionRecipes = new RecipeStorage<>();
    public static final IRecipeSerializer<ExplosionRecipe> explosionSerializer = new Recipe.Serializer<>(explosionRecipes);
    
    public static final RecipeStorage<LightningRecipe> lightningRecipes = new RecipeStorage<>();
    public static final IRecipeSerializer<LightningRecipe> lightningSerializer = new Recipe.Serializer<>(lightningRecipes);
    
    public static final Function<Collection<ItemEntity>, List<ItemStack>> entityStripper = entities -> entities.stream().map(ItemEntity::getItem).collect(Collectors.toList());
    
    public static WorldCrafter INSTANCE;
    
    public WorldCrafter()
    {
        INSTANCE = this;
        
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::registerRegistries);
        MinecraftForge.EVENT_BUS.addListener(this::serverStartingEvent);
        eventBus.addGenericListener(RecipeConsumer.class, this::registerRecipeConsumers);
        eventBus.addGenericListener(IRecipeSerializer.class, this::registerRecipeData);
        
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
        
        MinecraftForge.EVENT_BUS.register(FluidRecipeManager.class);
        MinecraftForge.EVENT_BUS.register(FireRecipeManager.class);
        MinecraftForge.EVENT_BUS.register(ExplosionRecipeManager.class);
        MinecraftForge.EVENT_BUS.register(LightningRecipeManager.class);
    }
    
    public static ResourceLocation location(String path) { return new ResourceLocation(MODID, path); }
    
    private void registerRegistries(RegistryEvent.NewRegistry event)
    {
        new RegistryBuilder<RecipeConsumer>().setType(RecipeConsumer.class)
                                             .setName(location("recipe_consumer"))
                                             .disableSaving()
                                             .create();
    }
    
    private void registerRecipeConsumers(RegistryEvent.Register<RecipeConsumer> event)
    {
//        event.getRegistry().registerAll(
//                new RecipeConsumer((recipe -> {
//                    
//                })).setRegistryName(MODID, "consume_ingredients"),
//                new RecipeConsumer((recipe ->  {
//                    
//                })).setRegistryName(MODID, "consume_fluid"));
    }
    
    public void registerRecipeData(RegistryEvent.Register<IRecipeSerializer<?>> event)
    {
        event.getRegistry().register(fluidSerializer.setRegistryName(FluidRecipe.UID));
    }
    
    public void serverStartingEvent(FMLServerAboutToStartEvent event)
    {
        IReloadableResourceManager manager = event.getServer().getResourceManager();
        manager.addReloadListener((ISelectiveResourceReloadListener) (listener, predicate) -> {
            RecipeLoader.loadRecipes(manager, WorldCrafter.MODID + "/recipes");
        });
    }
}