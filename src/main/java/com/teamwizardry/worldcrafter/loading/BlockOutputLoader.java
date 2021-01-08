package com.teamwizardry.worldcrafter.loading;

import java.util.Map;

import com.teamwizardry.worldcrafter.BlockOutput;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockOutputLoader extends Loader<BlockOutput>
{
    public static final BlockOutputLoader INSTANCE = new BlockOutputLoader();
    
    protected static final String BLOCK = "block";
    protected static final String COUNT = "count";
    protected static final String NBT = "nbt";
    
    private BlockOutputLoader() {}
    
    @Override
    public BlockOutput load(Map<String, Object> yaml)
    {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation((String) yaml.get(BLOCK)));
        
        return new BlockOutput(block);
    }
}
