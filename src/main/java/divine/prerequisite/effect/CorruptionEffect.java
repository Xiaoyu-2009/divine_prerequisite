package divine.prerequisite.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class CorruptionEffect extends MobEffect {
    private static final double DEFAULT_BASE_DAMAGE_PERCENT = 0.03;
    private static final boolean DEFAULT_CAN_BE_CURED_BY_MILK = false;
    
    public CorruptionEffect() {
        super(MobEffectCategory.HARMFUL, 0x800000);
    }
    
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) {
            return;
        }
        
        double damagePercent = getBaseDamagePercent();
        double damage = entity.getMaxHealth() * damagePercent * (amplifier + 1);
        
        if (entity.getHealth() > 1.0F && damage > 0) {
            entity.hurt(entity.damageSources().magic(), (float) damage);
        }
    }
    
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
    
    private double getBaseDamagePercent() {
        try {
            return DEFAULT_BASE_DAMAGE_PERCENT;
        } catch (Exception e) {
            return DEFAULT_BASE_DAMAGE_PERCENT;
        }
    }
    
    @Override
    public List<ItemStack> getCurativeItems() {
        if (canBeCuredByMilk()) {
            return super.getCurativeItems();
        } else {
            return ImmutableList.of();
        }
    }
    
    private boolean canBeCuredByMilk() {
        try {
            return DEFAULT_CAN_BE_CURED_BY_MILK;
        } catch (Exception e) {
            return DEFAULT_CAN_BE_CURED_BY_MILK;
        }
    }
}