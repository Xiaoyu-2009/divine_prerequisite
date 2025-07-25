package divine.prerequisite.effect;

import divine.prerequisite.config.ModConfigs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;

public class CorruptionEffect extends MobEffect {
    
    public CorruptionEffect() {
        super(MobEffectCategory.HARMFUL, 0x800000);
    }
    
    @Override
    public void fillEffectCures(java.util.Set<net.neoforged.neoforge.common.EffectCure> cures, net.minecraft.world.effect.MobEffectInstance effectInstance) {
        try {
            if (ModConfigs.CORRUPTION_CAN_BE_CURED_BY_MILK.get()) {
                cures.add(net.neoforged.neoforge.common.EffectCures.MILK);
            }
        } catch (Exception e) {}
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) {
            return true;
        }

        if (isEntityImmune(entity)) {
            return true;
        }

        double damagePercent = getBaseDamagePercent();
        double damage = entity.getMaxHealth() * damagePercent * (amplifier + 1);

        if (entity.getHealth() > 1.0F && damage > 0) {
            entity.hurt(entity.damageSources().magic(), (float) damage);
        }
        return true;
    }
    
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    private double getBaseDamagePercent() {
        try {
            return ModConfigs.CORRUPTION_BASE_DAMAGE_PERCENT.get();
        } catch (Exception e) {
            return 0.03;
        }
    }

    private boolean isEntityImmune(LivingEntity entity) {
        try {
            if (entity != null) {
                ResourceLocation entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
                if (entityId != null) {
                    List<? extends String> immuneEntities = ModConfigs.CORRUPTION_IMMUNE_ENTITIES.get();
                    return immuneEntities.contains(entityId.toString());
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}