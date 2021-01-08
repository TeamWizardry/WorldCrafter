package com.teamwizardry.worldcrafter;

import net.minecraft.block.Block;

public class BlockOutput extends Ingredient<BlockOutput>
{
    private final Block block;
    
    public BlockOutput(Block block)
    {
        this.block = block;
    }
    
    public Block getBlock() { return this.block; }
}
