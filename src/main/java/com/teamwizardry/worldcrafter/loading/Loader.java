package com.teamwizardry.worldcrafter.loading;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public abstract class Loader<T>
{
    protected static final String INPUT = "input";
    protected static final String OUTPUT = "output";
    protected static final String TICK = "tickEvents";
    protected static final String FINISH = "finishEvents";
    protected static final String DURATION = "duration";
    
    public abstract T load(Map<String, Object> yaml);
    
    public List<T> loadAll(List<Map<String, Object>> yaml)
    {
        return yaml.stream().map(this::load).collect(Collectors.toList());
    }
    
    public static final String loadString(Map<String, Object> yaml, String key)
    {
        if (yaml.containsKey(key))
            return (String) yaml.get(key);
        throw new NoSuchElementException("Key " + key + " not found in map " + yaml);
    }
    
    public static final int loadInt(Map<String, Object> yaml, String key, int defaultValue)
    {
        if (yaml.containsKey(key))
        {
            Object intObj = yaml.get(key);
            if (intObj instanceof Number)
                return ((Number) intObj).intValue();
            throw new InvalidParameterException("Excpected [" + key + "] to be an Integer, found " + intObj.getClass().getSimpleName() + " instead");
        }
        return defaultValue;
    }
    
    public static final double loadDouble(Map<String, Object> yaml, String key, double defaultValue)
    {
        if (yaml.containsKey(key))
        {
            Object doubleObj = yaml.get(key);
            if (doubleObj instanceof Number)
                return ((Number) doubleObj).doubleValue();
            throw new InvalidParameterException("Excpected [" + key + "] to be a Double, found " + doubleObj.getClass().getSimpleName() + " instead");
        }
        return defaultValue;
    }
}
