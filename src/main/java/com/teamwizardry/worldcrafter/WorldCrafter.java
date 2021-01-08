package com.teamwizardry.worldcrafter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.teamwizardry.worldcrafter.fluid.FluidRecipe;
import com.teamwizardry.worldcrafter.fluid.FluidRecipeManager;
import com.teamwizardry.worldcrafter.loading.RecipeLoader;

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
    
    public static WorldCrafter INSTANCE;
    
    public WorldCrafter()
    {
        INSTANCE = this;
        
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::registerRegistries);
        MinecraftForge.EVENT_BUS.addListener(this::serverStartingEvent);
        eventBus.addGenericListener(RecipeConsumer.class, this::registerRecipeConsumers);
        
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
    
    public void serverStartingEvent(FMLServerAboutToStartEvent event)
    {
        IReloadableResourceManager manager = event.getServer().getResourceManager();
        manager.addReloadListener((ISelectiveResourceReloadListener) (listener, predicate) -> {
            RecipeLoader.loadRecipes(manager, WorldCrafter.MODID + "/recipes");
        });
    }
}