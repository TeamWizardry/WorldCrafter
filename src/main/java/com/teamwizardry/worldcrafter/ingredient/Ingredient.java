package com.teamwizardry.worldcrafter.ingredient;

import static com.teamwizardry.worldcrafter.core.NBTHelper.path;

import java.util.Arrays;
import java.util.List;

import com.teamwizardry.worldcrafter.core.NBTHelper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

public abstract class Ingredient<T extends Ingredient<T>>
{
    protected final CompoundNBT data = new CompoundNBT();
    
    @SuppressWarnings("unchecked")
    public <Primitive> T setData(String path, Primitive value)
    {
        CompoundNBT nbt = path(data, path.substring(0, path.lastIndexOf('.')));
        String key = path.substring(path.lastIndexOf('.')+1);
        
        if (value instanceof Integer)
            nbt.putInt(key, (Integer) value);
        else if (value instanceof Boolean)
            nbt.putBoolean(key, (Boolean) value);
        else if (value instanceof Double)
            nbt.putDouble(key, (Double) value);
        else if (value instanceof String)
            nbt.putString(key, (String) value);
        return (T)this;
    }
    
    public int getInt(String path)
    {
        return NBTHelper.getInt(data, path);
    }
    
    public boolean getBoolean(String path)
    {
        return NBTHelper.getBoolean(data, path);
    }
    
    public double getDouble(String path)
    {
        return NBTHelper.getDouble(data, path);
    }
    
    public String getString(String path)
    {
        return NBTHelper.getString(data, path);
    }
    
    public List<ItemStack> getMatchingItems()
    {
        return Arrays.asList();
    }
    
    public List<FluidStack> getMatchingFluids()
    {
        return Arrays.asList();
    }
}
