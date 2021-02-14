package com.teamwizardry.worldcrafter.loading.recipe;

import static com.teamwizardry.worldcrafter.WorldCrafter.explosionRecipes;
import static com.teamwizardry.worldcrafter.WorldCrafter.fireRecipes;
import static com.teamwizardry.worldcrafter.WorldCrafter.fluidRecipes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.google.common.base.Joiner;
import com.teamwizardry.worldcrafter.loading.Loader;

import net.minecraft.resources.FallbackResourceManager;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.packs.ModFileResourcePack;

public class RecipeLoader
{
    private static final Yaml yaml = new Yaml();
    private static final Logger LOGGER = LogManager.getLogger();
    
    protected RecipeLoader() {}
    
    public static void loadRecipes(IReloadableResourceManager resourceManager, String folder)
    {
        if(!(resourceManager instanceof SimpleReloadableResourceManager)) return;
        for (String resourceNamespace : resourceManager.getResourceNamespaces()) {
            if(resourceNamespace.equals("minecraft") || resourceNamespace.equals("forge"))
                continue;
            FallbackResourceManager fallbackResourceManager = ((SimpleReloadableResourceManager) resourceManager).namespaceResourceManagers.get(resourceNamespace);
            for (IResourcePack pack : fallbackResourceManager.resourcePacks){
                if(pack.getResourceNamespaces(ResourcePackType.SERVER_DATA).isEmpty() || !(pack instanceof ResourcePack))
                    continue;
                if(pack instanceof ModFileResourcePack){
                    for (ResourceLocation resourceLocation : loadRecipesFromModFile(ResourcePackType.SERVER_DATA, folder, Integer.MAX_VALUE, file -> file.endsWith(".yaml"), ((ModFileResourcePack) pack).getModFile())) {
                        LOGGER.info("Found Recipe file: " + resourceLocation);
                        try {
                            loadYamls(pack.getResourceStream(ResourcePackType.SERVER_DATA, resourceLocation));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    for (ResourceLocation resourceLocation :  pack.getAllResourceLocations(ResourcePackType.SERVER_DATA, (String) pack.getResourceNamespaces(ResourcePackType.SERVER_DATA).toArray()[0], folder, Integer.MAX_VALUE, file -> file.endsWith(".yaml"))) {
                        LOGGER.info("Found Recipe file: " + resourceLocation);
                        try {
                            loadYamls(pack.getResourceStream(ResourcePackType.SERVER_DATA, resourceLocation));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    protected static void loadYamls(InputStream file)
    {
        StreamSupport.stream(yaml.loadAll(file).spliterator(), false)
                     .forEach(map -> readRecipe((Map<String, Object>) map));
    }
    
    protected static Collection<ResourceLocation> loadRecipesFromModFile(ResourcePackType type, String pathIn, int maxDepth, Predicate<String> filter, ModFile modFile)
    {
        try
        {
            Path root = modFile.getLocator().findPath(modFile, type.getDirectoryName()).toAbsolutePath();
            Path inputPath = root.getFileSystem().getPath(pathIn);
            return Files.walk(root).
                    map(path -> root.relativize(path.toAbsolutePath())).
                    filter(path -> path.getNameCount() > 1 && path.getNameCount() - 1 <= maxDepth). // Make sure the depth is within bounds, ignoring domain
                    filter(path -> !path.toString().endsWith(".mcmeta")). // Ignore .mcmeta files
                    filter(path -> path.toString().startsWith(inputPath.toString())). // Make sure the target path is inside this one (again ignoring domain)
                    filter(path -> filter.test(path.getFileName().toString())). // Test the file name against the predicate
                    filter(path -> path.toString().startsWith(inputPath.toString())).
                    // Finally we need to form the RL, so use the first name as the domain, and the rest as the path
                    // It is VERY IMPORTANT that we do not rely on Path.toString as this is inconsistent between operating systems
                    // Join the path names ourselves to force forward slashes
                    //
                    map(path -> new ResourceLocation(path.getName(0).toString(), Joiner.on('/').join(path.subpath(1,Math.min(maxDepth, path.getNameCount()))))).
                    collect(Collectors.toList());
        }
        catch (IOException e)
        {
            return Collections.emptyList();
        }
    }
    
    protected static void readRecipe(Map<String, Object> yaml)
    {
        String recipeType = (String) yaml.get(Loader.TYPE);
        switch (recipeType)
        {
            case "fluid": fluidRecipes.addRecipe(FluidRecipeLoader.INSTANCE.load(yaml)); break;
            case "fire": fireRecipes.addRecipe(FireRecipeLoader.INSTANCE.load(yaml)); break;
            case "explosion": explosionRecipes.addRecipe(ExplosionRecipeLoader.INSTANCE.load(yaml)); break;
            case "lightning": break;
            case "anvil": break;
            case "piston": break;
            case "splash": break;
            case "lingering": break;
        }
    }
}
