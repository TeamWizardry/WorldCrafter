package com.teamwizardry.worldcrafter;

import static com.teamwizardry.worldcrafter.Output.OutputType.BLOCK;
import static com.teamwizardry.worldcrafter.Output.OutputType.FLUID;
import static com.teamwizardry.worldcrafter.Output.OutputType.ITEM;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Output
{
    public enum OutputType
    {
        ITEM,
        BLOCK,
        FLUID
    }
    
    private List<ItemOutput> outputItems;
    private BlockOutput outputBlock;
    private FluidOutput outputFluid;
    
    private OutputType outputType;
    
    public Output(ItemOutput outputItem)
    {
        this.outputItems = new ArrayList<>(1);
        this.outputItems.add(outputItem);
        this.outputType = ITEM;
    }
    
    public Output(List<ItemOutput> outputItems)
    {
        this.outputItems = outputItems;
        this.outputType = ITEM;
    }
    
    public Output(BlockOutput outputBlock)
    {
        this.outputBlock = outputBlock;
        this.outputType = BLOCK;
    }
    
    public Output(FluidOutput outputFluid)
    {
        this.outputFluid = outputFluid;
        this.outputType = FLUID;
    }
    
    public void createOutput(World world, BlockPos pos)
    {
        switch (this.outputType)
        {
            case ITEM:
                Random random = new Random();
                outputItems.stream().map(output -> {
                    int min = output.getMin();
                    int max = output.getMax();
                    int count = random.nextInt(max - min + 1) + min;
                    ItemStack stack = new ItemStack(output.getItem(), count);
                    if (output.getNBT() != null)
                        stack.setTag(output.getNBT());
                    return stack;
                }).forEach(stack -> world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack)));
                break;
            case BLOCK:
                world.setBlockState(pos, outputBlock.getBlock().getDefaultState());
                break;
            case FLUID:
                world.setBlockState(pos, outputFluid.getFluid().getDefaultState().getBlockState());
                break;
        }
    }
}
