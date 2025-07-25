package divine.prerequisite.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLPaths;

import java.util.Arrays;
import java.util.List;

public class ModConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> CORRUPTION_IMMUNE_ENTITIES;
    public static final ModConfigSpec.BooleanValue CORRUPTION_CAN_BE_CURED_BY_MILK;
    public static final ModConfigSpec.DoubleValue CORRUPTION_BASE_DAMAGE_PERCENT;
    public static final ModConfigSpec.BooleanValue NO_HEAL_CAN_BE_CURED_BY_MILK;

    static {
        BUILDER.push("corruption_effect");
        
        CORRUPTION_IMMUNE_ENTITIES = BUILDER
                .comment("List of entities that are immune to corruption effect")
                .define("immune_entities", 
                    Arrays.asList("minecraft:wither", "minecraft:ender_dragon", "minecraft:warden"));
        
        CORRUPTION_CAN_BE_CURED_BY_MILK = BUILDER
                .comment("Whether corruption effect can be cured by milk")
                .define("can_be_cured_by_milk", false);
        
        CORRUPTION_BASE_DAMAGE_PERCENT = BUILDER
                .comment("Base damage percentage per second for corruption effect")
                .defineInRange("base_damage_percent", 0.03, 0.001, 1.0);
        
        BUILDER.pop();
        
        BUILDER.push("no_heal_effect");
        
        NO_HEAL_CAN_BE_CURED_BY_MILK = BUILDER
                .comment("Whether no heal effect can be cured by milk")
                .define("can_be_cured_by_milk", false);
        
        BUILDER.pop();
        
        SPEC = BUILDER.build();
    }

    public static void register() {
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, SPEC, "divine/prerequisite/prerequisite-common.toml");
    }
}