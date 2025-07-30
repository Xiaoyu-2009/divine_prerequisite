package divine.prerequisite.registry;

import divine.prerequisite.Divine;
import divine.prerequisite.effect.FrostyEffect;
import divine.prerequisite.effect.CharmEffect;
import divine.prerequisite.effect.CorruptionEffect;
import divine.prerequisite.effect.NoHealEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = 
    DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Divine.MOD_ID);

    public static final RegistryObject<MobEffect> FROSTY = MOB_EFFECTS.register("frosty", FrostyEffect::new);
    public static final RegistryObject<MobEffect> CHARM = MOB_EFFECTS.register("charm", CharmEffect::new);
    public static final RegistryObject<MobEffect> CORRUPTION = MOB_EFFECTS.register("corruption", CorruptionEffect::new);
    public static final RegistryObject<MobEffect> NO_HEAL = MOB_EFFECTS.register("no_heal", NoHealEffect::new);
    
    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}