package divine.prerequisite;

import divine.prerequisite.config.ModConfigs;
import divine.prerequisite.registry.ModEffects;
import divine.prerequisite.registry.ModParticles;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Divine.MOD_ID)
public class Divine {
    public static final String MOD_ID = "divine_prerequisite";

    public Divine(IEventBus modEventBus) {
        ModConfigs.register();
        ModEffects.register(modEventBus);
        ModParticles.register(modEventBus);
    }
}