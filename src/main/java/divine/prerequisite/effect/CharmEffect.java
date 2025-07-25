package divine.prerequisite.effect;

import divine.prerequisite.Divine;
import divine.prerequisite.registry.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = Divine.MOD_ID)
public class CharmEffect extends MobEffect {
    private static final Map<UUID, UUID> EFFECT_SOURCES = new HashMap<>();

    public CharmEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF1493);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                ParticleTypes.HEART, 
                entity.getX(), 
                entity.getY() + entity.getBbHeight() / 2, 
                entity.getZ(),
                2, 0.3, 0.3, 0.3, 0.02
            );
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    public static void registerEffectSource(LivingEntity target, LivingEntity source) {
        if (target != null && source != null) {
            EFFECT_SOURCES.put(target.getUUID(), source.getUUID());
        }
    }

    public static void removeEffectSource(LivingEntity target) {
        if (target != null) {
            EFFECT_SOURCES.remove(target.getUUID());
        }
    }

    public static UUID getEffectSource(LivingEntity target) {
        return target != null ? EFFECT_SOURCES.get(target.getUUID()) : null;
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingDamageEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        float damage = event.getNewDamage();
        if (entity.hasEffect(ModEffects.CHARM)) {
            int amplifier = entity.getEffect(ModEffects.CHARM).getAmplifier() + 1;
            UUID sourceUUID = getEffectSource(entity);
            if (sourceUUID != null) {
                for (Entity e : entity.level().getEntities(entity, entity.getBoundingBox().inflate(100))) {
                    if (e.getUUID().equals(sourceUUID) && e instanceof LivingEntity livingSource) {
                        float healAmount = damage * (0.5f + (amplifier * 0.25f));
                        livingSource.heal(healAmount);
                        if (entity.level() instanceof ServerLevel serverLevel) {
                            for (int i = 0; i < 10; i++) {
                                double progress = i / 10.0;
                                double x = entity.getX() + (livingSource.getX() - entity.getX()) * progress;
                                double y = entity.getY() + entity.getBbHeight() / 2 + (livingSource.getY() + livingSource.getBbHeight() / 2 - entity.getY() - entity.getBbHeight() / 2) * progress;
                                double z = entity.getZ() + (livingSource.getZ() - entity.getZ()) * progress;
                                serverLevel.sendParticles(
                                    ParticleTypes.HEART, 
                                    x, y, z,
                                    1, 0.1, 0.1, 0.1, 0.0
                                );
                            }
                            serverLevel.playSound(
                                null, 
                                livingSource.getX(), 
                                livingSource.getY(), 
                                livingSource.getZ(),
                                SoundEvents.GENERIC_DRINK, 
                                SoundSource.PLAYERS, 
                                0.5f, 
                                1.0f + entity.level().random.nextFloat() * 0.4f
                            );
                        }
                        break;
                    }
                }
            }
        }
    }
}