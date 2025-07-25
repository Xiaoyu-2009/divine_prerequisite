package divine.prerequisite.registry;

import divine.prerequisite.Divine;
import divine.prerequisite.effect.FrostyEffect;
import divine.prerequisite.effect.CharmEffect;
import divine.prerequisite.effect.CorruptionEffect;
import divine.prerequisite.effect.NoHealEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Divine.MOD_ID);

    public static final Holder<MobEffect> FROSTY = MOB_EFFECTS.register("frosty", FrostyEffect::new);
    public static final Holder<MobEffect> CHARM = MOB_EFFECTS.register("charm", CharmEffect::new);
    public static final Holder<MobEffect> CORRUPTION = MOB_EFFECTS.register("corruption", CorruptionEffect::new);
    public static final Holder<MobEffect> NO_HEAL = MOB_EFFECTS.register("no_heal", NoHealEffect::new);

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}