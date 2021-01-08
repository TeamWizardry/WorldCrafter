package com.teamwizardry.worldcrafter;

import net.minecraft.nbt.CompoundNBT;

public class NBTHelper
{
    /**
     * Follows a '.'-delimited path down a CompoundNBT and returns the compound at the bottom.
     * <b>Will generate missing CompoundNBTs in the path, use {@link NBTHelper#get(CompoundNBT, String)} for simple value retrieval.</b>
     */
    public static CompoundNBT path(CompoundNBT root, String path)
    {
        CompoundNBT nbt = root;
        
        for (String p : path.split("\\."))
        {
            if (!nbt.contains(p))
                nbt.put(p, new CompoundNBT());
            nbt = nbt.getCompound(p);
        }
        
        return nbt;
    }
    
    /**
     * Follows a '.'-delimited path down a CompoundNBT and returns the compound at the bottom.
     * Will throw an error if values are missing 
     */
    public static CompoundNBT get(CompoundNBT root, String path) throws IllegalArgumentException
    {
        CompoundNBT nbt = root;
        
        for (String p : path.split("\\."))
        {
            if (!nbt.contains(p))
                throw new IllegalArgumentException("CompoundNBT does not contain full path: " + path);
            nbt = nbt.getCompound(p);
        }
        
        return nbt;
    }
    
    public static int getInt(CompoundNBT root, String path)
    {
        CompoundNBT nbt = path(root, path.substring(0, path.lastIndexOf('.')));
        String key = path.substring(path.lastIndexOf('.')+1);
        
        return nbt.getInt(key);
    }
    
    public static boolean getBoolean(CompoundNBT root, String path)
    {
        CompoundNBT nbt = path(root, path.substring(0, path.lastIndexOf('.')));
        String key = path.substring(path.lastIndexOf('.')+1);
        
        return nbt.getBoolean(key);
    }
    
    public static double getDouble(CompoundNBT root, String path)
    {
        CompoundNBT nbt = path(root, path.substring(0, path.lastIndexOf('.')));
        String key = path.substring(path.lastIndexOf('.')+1);
        
        return nbt.getDouble(key);
    }
    
    public static String getString(CompoundNBT root, String path)
    {
        CompoundNBT nbt = path(root, path.substring(0, path.lastIndexOf('.')));
        String key = path.substring(path.lastIndexOf('.')+1);
        
        return nbt.getString(key);
    }
}
