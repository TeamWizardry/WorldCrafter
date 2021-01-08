package com.teamwizardry.worldcrafter.loading;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.teamwizardry.worldcrafter.ItemOutput;
import com.teamwizardry.worldcrafter.Output;

import net.minecraft.item.Items;

public class OutputLoader extends Loader<Output>
{
    public static final OutputLoader INSTANCE = new OutputLoader();
    
    private static final String ITEM = "item";
    private static final String BLOCK = "block";
    private static final String FLUID = "fluid";
    
    private OutputLoader() {}
    
    @SuppressWarnings("unchecked")
    public Output load(Object yaml)
    {
        if (yaml instanceof Map)
            return load((Map<String, Object>) yaml);
        else if (yaml instanceof List)
            return load((List<Map<String, Object>>) yaml);
        return null;
    }
    
    @Override
    public Output load(Map<String, Object> yaml)
    {
        if (yaml.containsKey(ITEM))
            return new Output(ItemOutputLoader.INSTANCE.load(yaml));
        if (yaml.containsKey(BLOCK))
            return new Output(BlockOutputLoader.INSTANCE.load(yaml));
        if (yaml.containsKey(FLUID))
            return new Output(FluidOutputLoader.INSTANCE.load(yaml));
        return new Output(new ItemOutput(Items.AIR, 0, 0, null));
    }
    
    public Output load(List<Map<String, Object>> yaml)
    {
        return new Output(ItemOutputLoader.INSTANCE.loadAll(yaml.stream().filter(map -> map.containsKey(ITEM)).collect(Collectors.toList())));
    }
}
