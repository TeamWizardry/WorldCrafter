package com.teamwizardry.worldcrafter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.teamwizardry.worldcrafter.fluid.FluidRecipe;
import com.teamwizardry.worldcrafter.fluid.FluidRecipeManager;
import com.teamwizardry.worldcrafter.loading.RecipeLoader;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
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
    
    public static final IRecipeType<FluidRecipe> fluidRecipeType = new RecipeType<>();
    public static final RecipeStorage<FluidRecipe> fluidRecipes = new RecipeStorage<>();
    public static final IRecipeSerializer<FluidRecipe> fluidSerializer = new Recipe.Serializer<FluidRecipe>(fluidRecipes);
    
    public static final Function<List<ItemEntity>, List<ItemStack>> entityStripper = entities -> entities.stream().map(ItemEntity::getItem).collect(Collectors.toList());
    
    public static WorldCrafter INSTANCE;
    
    public WorldCrafter()
    {
        INSTANCE = this;
        
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::registerRegistries);
        MinecraftForge.EVENT_BUS.addListener(this::serverStartingEvent);
        eventBus.addGenericListener(RecipeConsumer.class, this::registerRecipeConsumers);
        eventBus.addGenericListener(IRecipeSerializer.class, this::registerRecipeData);
        
        MinecraftForge.EVENT_BUS.register(FluidRecipeManager.class);
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
        Registry.register(Registry.RECIPE_TYPE, FluidRecipe.UID, fluidRecipeType);
        event.getRegistry().register(fluidSerializer.setRegistryName(FluidRecipe.UID));
    }
    
    public void serverStartingEvent(FMLServerAboutToStartEvent event)
    {
        IReloadableResourceManager manager = event.getServer().getResourceManager();
        manager.addReloadListener((ISelectiveResourceReloadListener) (listener, predicate) -> {
            RecipeLoader.loadRecipes(manager, WorldCrafter.MODID + "/recipes");
        });
    }
    
    private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T>
    {
        @Override public String toString() { return Registry.RECIPE_TYPE.getKey(this).toString(); }
    }
}