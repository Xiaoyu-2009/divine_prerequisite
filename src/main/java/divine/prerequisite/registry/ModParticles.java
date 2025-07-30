package divine.prerequisite.registry;

import divine.prerequisite.Divine;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = 
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Divine.MOD_ID);

    public static final RegistryObject<SimpleParticleType> FROST = PARTICLE_TYPES.register("frost",
            () -> new SimpleParticleType(false));
            
    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}