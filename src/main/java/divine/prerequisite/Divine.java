package divine.prerequisite;

import divine.prerequisite.registry.ModEffects;
import divine.prerequisite.registry.ModParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Divine.MOD_ID)
@Mod.EventBusSubscriber(modid = Divine.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Divine {
    public static final String MOD_ID = "divine_prerequisite";

    public Divine() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEffects.register(modEventBus);
        ModParticles.register(modEventBus);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(this::clientSetup);
        });
        MinecraftForge.EVENT_BUS.register(this);
    }

    @OnlyIn(Dist.CLIENT)
    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MinecraftForge.EVENT_BUS.register(divine.prerequisite.client.FrostyEffectRenderer.class);
            MinecraftForge.EVENT_BUS.register(divine.prerequisite.client.CharmEffectRenderer.class);
        });
    }
}