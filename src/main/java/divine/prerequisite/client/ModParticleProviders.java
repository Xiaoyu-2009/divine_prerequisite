package divine.prerequisite.client;

import divine.prerequisite.Divine;
import divine.prerequisite.client.particle.FrostParticle;
import divine.prerequisite.registry.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Divine.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticleProviders {
    @SubscribeEvent
    @SuppressWarnings("deprecation")
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.FROST.get(), FrostParticle.Provider::new);
    }
}