package me.jamestmartin.wasteland.world;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public enum MoonPhase {
    FULL(6),
    WANING_GIBBOUS(5),
    THIRD_QUARTER(3),
    WANING_CRESCENT(1),
    NEW(0),
    WAXING_CRESCENT(2),
    FIRST_QUARTER(3),
    WAXING_GIBBOUS(4);
    
    private static final long MOON_CYCLE_LENGTH_IN_DAYS = 8;

    private static final long DAY_LENGTH = 24000;
    private static final long DUSK = 12000;
    private static final long NIGHT = 13000;
    private static final long DAWN = 23000;
    
    private static final int MAXIMUM_SUNLIGHT = 16;
    
    private final int maximumMoonlight;
    
    private static Constructor<?> blockPositionConstructor;
    private static Field serverWorldField;
    private static Method getLightEngineMethod;
    private static Field lightEngineLayerSkyField;
    private static Method getLightForMethod;
    
    static {
        try {
            Class<?> blockPositionClass = Class.forName("net.minecraft.server.v1_16_R3.BlockPosition");
            blockPositionConstructor = blockPositionClass.getConstructor(int.class, int.class, int.class);
            
            Class<?> craftWorldClass = Class.forName("org.bukkit.craftbukkit.v1_16_R3.CraftWorld");
            serverWorldField = craftWorldClass.getDeclaredField("world");
            serverWorldField.setAccessible(true);
            
            Class<?> minecraftWorldClass = Class.forName("net.minecraft.server.v1_16_R3.World");
            getLightEngineMethod = minecraftWorldClass.getMethod("e");
            
            Class<?> lightEngineClass = Class.forName("net.minecraft.server.v1_16_R3.LightEngine");
            lightEngineLayerSkyField = lightEngineClass.getDeclaredField("b");
            lightEngineLayerSkyField.setAccessible(true);
            
            Class<?> lightEngineLayerClass = Class.forName("net.minecraft.server.v1_16_R3.LightEngineLayer");
            getLightForMethod = lightEngineLayerClass.getMethod("b", blockPositionClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private MoonPhase(int maximumMoonlight) {
        this.maximumMoonlight = maximumMoonlight;
    }
    
    public boolean isWaning() {
        return !isWaxing();
    }
    
    public boolean isWaxing() {
        return this == NEW
                || this == WAXING_CRESCENT
                || this == FIRST_QUARTER
                || this == WAXING_GIBBOUS;
    }
    
    public boolean isFull() {
        return this == FULL;
    }
    
    public boolean isNew() {
        return this == NEW;
    }
    
    /** Is the moon phase full or new. */
    public boolean isExtreme() {
        return isFull() || isNew();
    }
    
    public boolean isQuarter() {
        return this == THIRD_QUARTER || this == FIRST_QUARTER;
    }
    
    public boolean isGibbous() {
        return this == WANING_GIBBOUS || this == WAXING_GIBBOUS;
    }
    
    public boolean isCrescent() {
        return this == WANING_CRESCENT || this == WAXING_CRESCENT;
    }
    
    /** Is the moon phase new a new moon. */
    public boolean isDark() {
        return isNew() || isCrescent();
    }
    
    /** Is the moon phase near a full moon. */
    public boolean isLight() {
        return isFull() || isGibbous();
    }
    
    /** Turn a linear proportion into a cosine-based proportion. */
    // There's probably a term for this but I don't know it.
    private static double combine(double proportion) {
        return (1 - Math.cos(Math.PI * proportion)) / 2;
    }
    
    /** The proportion of light that comes from the sun as opposed to the moon. */
    private static float calculateSunlightProportion(final long time) {
        // Daytime
        if (time < DUSK) {
            return 1f;
        }
        // Dusk
        if (time < NIGHT) {
            return (float) (1 - combine(((float) (time - DUSK)) / (NIGHT - DUSK)));
        }
        // Night
        if (time < DAWN) {
            return 0f;
        }
        // Dawn
        return (float) combine((float) (time - DAWN) / (DAY_LENGTH - DAWN));
    }
    
    private static float calculateSunlightProportion(final World world) {
        return calculateSunlightProportion(world.getTime());
    }
    
    /** Get the maximum sunlight any block can receive at the current time of day. */
    public static int getWorldMaximumSunlight(long time) {
        return (int) (MAXIMUM_SUNLIGHT * calculateSunlightProportion(time));
    }
    
    /** Get the maximum sunlight any block can receive at the current time of day. */
    public static int getWorldMaximumSunight(World world) {
        return getWorldMaximumSunlight(world.getTime());
    }
    
    /** Get the level of sunlight a block with the given exposure to the sky is receiving at the given time. */
    public static int getSunlight(long time, int skylight) {
        return (int) (skylight * calculateSunlightProportion(time) / 15);
    }
    
    /** Get the level of sunlight a block with the given exposure to the sky is receiving right now. */
    public static int getSunlight(World world, int skylight) {
        return getSunlight(world.getTime(), skylight);
    }
    
    /** Get the level of sunlight this block is receiving right now. */
    public static int getSunlight(Block block) {
        return (int) (getExposureToSky(block) * calculateSunlightProportion(block.getWorld()));
    }
    
    /** Get the level of sunlight this location is receiving right now. */
    public static int getSunlight(Location location) {
        return (int) getSunlight(location.getBlock());
    }
    
    public static int getExposureToSky(Block block) {
        try {
            Object blockPosition = blockPositionConstructor.newInstance(block.getX(), block.getY(), block.getZ());
            Object serverWorld = serverWorldField.get(block.getWorld());
            Object lightEngine = getLightEngineMethod.invoke(serverWorld);
            Object lightEngineLayerSky = lightEngineLayerSkyField.get(lightEngine);
            return (int) getLightForMethod.invoke(lightEngineLayerSky, blockPosition);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    /** The maximum amount of light emitted by the moon for the purposes of game mechanics. */
    public int getWorldMaximumMoonlight() {
        return maximumMoonlight;
    }
    
    /** Get the most moonlight a block can receive with this moon phase and the given exposure to the sky. */
    public int getMaximumMoonlight(int skylight) {
        return (skylight * maximumMoonlight) / 16;
    }
    
    /** Get the most moonlight a block can receive with this moon phase and the block's exposure to the sky. */
    public int getMaximumMoonlight(Block block) {
        return getMaximumMoonlight(getExposureToSky(block));
    }
    
    /** Get the most moonlight a location can receive with this moon phase and the locations's exposure to the sky. */
    public int getMaximumMoonlight(Location location) {
        return getMaximumMoonlight(location.getBlock());
    }
    
    /** Get the amount of moonlight a block will receive with the given exposure to the sky and time of day. */
    public static int getMoonlight(long absoluteTime, int skylight) {
        float moonlightProportion = 1 - calculateSunlightProportion(absoluteTime % DAY_LENGTH);
        int tonightsMoonlight = MoonPhase.fromTime(absoluteTime).maximumMoonlight;
        return (int) (moonlightProportion * tonightsMoonlight * skylight / 15);
    }
    
    /** Get the amount of moonlight a block is receiving */
    public static int getMoonlight(Block block) {
        return getMoonlight(block.getWorld().getFullTime(), getExposureToSky(block));
    }
    
    /** Get the amount of moonlight a location is receiving */
    public static int getMoonlight(Location location) {
        return getMoonlight(location.getBlock());
    }
    
    /** Get the amount of skylight a block will receive with the given exposure to the sky and time of day. */
    public static int getSkylight(long absoluteTime, int skylight) {
        float sunlightProportion = calculateSunlightProportion(absoluteTime % DAY_LENGTH);
        int tonightsMoonlight = MoonPhase.fromTime(absoluteTime).maximumMoonlight;
        return (int) ((sunlightProportion * 15 + (1f - sunlightProportion) * tonightsMoonlight) * skylight / 15);
    }
    
    /** Get the amount of skylight a block is receiving, either moonlight or skylight. */
    public static int getSkylight(Block block) {
        return getSkylight(block.getWorld().getFullTime(), getExposureToSky(block));
    }
    
    /** Get the amount of skylight a location is receiving, either moonlight or skylight. */
    public static int getSkylight(Location location) {
        return getSkylight(location.getBlock());
    }
    
    /** Get the moon phase from an absolute time. */
    public static MoonPhase fromTime(final long time) {
        final long day = time / DAY_LENGTH;
        final int phase = (int) (day % MOON_CYCLE_LENGTH_IN_DAYS);
        return MoonPhase.values()[phase];
    }
    
    /** Get the world's moon phase. */
    public static MoonPhase fromWorld(final World world) {
        return MoonPhase.fromTime(world.getFullTime());
    }
}
