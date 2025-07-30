package divine.prerequisite.effect;

import divine.prerequisite.Divine;
import divine.prerequisite.registry.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = Divine.MOD_ID)
public class NoHealEffect extends MobEffect {
    private static final boolean DEFAULT_CAN_BE_CURED_BY_MILK = false;

    public NoHealEffect() {
        super(MobEffectCategory.HARMFUL, 0x333333);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        if (canBeCuredByMilk()) {
            return super.getCurativeItems();
        } else {
            return Collections.emptyList();
        }
    }

    private boolean canBeCuredByMilk() {
        try {
            return DEFAULT_CAN_BE_CURED_BY_MILK;
        } catch (Exception e) {
            return DEFAULT_CAN_BE_CURED_BY_MILK;
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.hasEffect(ModEffects.NO_HEAL.get())) {
            event.setCanceled(true);
        }
    }
}