package com.teamwizardry.worldcrafter.loading;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.CompoundNBT;

public class NBTLoader extends Loader<CompoundNBT>
{
    public static final NBTLoader INSTANCE = new NBTLoader();
    
    private NBTLoader() {}
    
    @Override
    public CompoundNBT load(Map<String, Object> yaml)
    {
        return load(yaml, Collections.emptyList());
    }
    
    public CompoundNBT load(Map<String, Object> yaml, String... keysToIgnore)
    {
        return load(yaml, Arrays.asList(keysToIgnore));
    }
    
    @SuppressWarnings("unchecked")
    public CompoundNBT load(Map<String, Object> yaml, List<String> keysToIgnore)
    {
        if (yaml == null) return null;
        
        CompoundNBT nbt = new CompoundNBT();
        yaml.forEach((key, value) -> {
            if (keysToIgnore.contains(key)) return;
            if (value instanceof Map)
                nbt.put(key, load((Map<String, Object>) value));
            else if (value instanceof List)
                nbt.put(key, load((List<Object>) value));
            else if (value instanceof Boolean)
                nbt.putBoolean(key, (Boolean) value);
            else if (value instanceof Double)
                nbt.putDouble(key, (Double) value);
            else if (value instanceof Integer)
                nbt.putInt(key, (Integer) value);
            else
                nbt.putString(key, (String) value);
        });
        return nbt;
    }
    
    @SuppressWarnings("unchecked")
    private CompoundNBT load(List<Object> yaml)
    {
        CompoundNBT nbt = new CompoundNBT();
        for (int i = 0; i < yaml.size(); i++)
        {
            String index = Integer.toString(i);
            Object value = yaml.get(i);
            if (value instanceof Map)
                nbt.put(index, load((Map<String, Object>) value));
            else if (value instanceof List)
                nbt.put(index, load((List<Object>) value));
            else if (value instanceof Boolean)
                nbt.putBoolean(index, (Boolean) value);
            else if (value instanceof Double)
                nbt.putDouble(index, (Double) value);
            else if (value instanceof Integer)
                nbt.putInt(index, (Integer) value);
            else
                nbt.putString(index, (String) value);
        }
        return nbt;
    }
}
