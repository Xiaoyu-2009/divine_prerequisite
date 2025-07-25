package divine.prerequisite.registry;

import divine.prerequisite.Divine;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = 
            DeferredRegister.create(Registries.PARTICLE_TYPE, Divine.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FROST = PARTICLE_TYPES.register("frost", 
            () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}