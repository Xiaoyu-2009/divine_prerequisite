package divine.prerequisite.effect;

import divine.prerequisite.Divine;
import divine.prerequisite.config.ModConfigs;
import divine.prerequisite.registry.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

@EventBusSubscriber(modid = Divine.MOD_ID)
public class NoHealEffect extends MobEffect {
    
    public NoHealEffect() {
        super(MobEffectCategory.HARMFUL, 0x333333);
    }
    
    @Override
    public void fillEffectCures(java.util.Set<net.neoforged.neoforge.common.EffectCure> cures, net.minecraft.world.effect.MobEffectInstance effectInstance) {
        try {
            if (ModConfigs.NO_HEAL_CAN_BE_CURED_BY_MILK.get()) {
                cures.add(net.neoforged.neoforge.common.EffectCures.MILK);
            }
        } catch (Exception e) {}
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return true;
    }
    
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return false;
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.hasEffect(ModEffects.NO_HEAL)) {
            event.setCanceled(true);
        }
    }
}