package divine.prerequisite.effect;

import divine.prerequisite.registry.ModParticles;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.UUID;

public class FrostyEffect extends MobEffect {
    public static final UUID MOVEMENT_SPEED_MODIFIER_UUID = UUID.fromString("CE9DBC2A-EE3F-43F5-9DF7-F7F1EE4915A9");
    public static final double FROST_MULTIPLIER = -0.5D;
    private static final TagKey<MobEffect> IMMOBILIZING_EFFECTS = TagKey.create(Registries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath("divine_prerequisite", "immobilizing_effect"));
    
    public FrostyEffect() {
        super(MobEffectCategory.HARMFUL, 0x56CBFD);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath("divine_prerequisite", "frosty_speed"), FROST_MULTIPLIER, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        living.setIsInPowderSnow(true);

        if (amplifier > 0 && living.canFreeze()) {
            living.setTicksFrozen(Math.min(living.getTicksRequiredToFreeze(), living.getTicksFrozen() + amplifier));
        }

        if (isImmune(living)) {
            return true;
        }

        if (living instanceof Skeleton && !(living instanceof Stray)) {
            if (living.level().random.nextFloat() < 0.005f) {
                Stray stray = EntityType.STRAY.create(living.level());
                if (stray != null) {
                    stray.moveTo(living.getX(), living.getY(), living.getZ(), living.getYRot(), living.getXRot());
                    stray.setHealth(living.getHealth());
                    stray.setNoAi(((Skeleton)living).isNoAi());

                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        stray.setItemSlot(slot, living.getItemBySlot(slot).copy());
                    }

                    living.level().addParticle(ModParticles.FROST.value(), 
                        living.getX(), living.getY() + 1.0D, living.getZ(),
                        0.0D, 0.0D, 0.0D);
                    living.level().playSound(null, living.getX(), living.getY(), living.getZ(),
                        SoundEvents.POWDER_SNOW_FALL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    
                    living.level().addFreshEntity(stray);
                    living.discard();
                }
            }
        }

        if (isFireMob(living)) {
            living.hurt(living.damageSources().freeze(), 2.0F * (amplifier + 1));
            living.level().playSound(null, living.getX(), living.getY(), living.getZ(),
                SoundEvents.POWDER_SNOW_STEP, SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
        
        return true;
    }

    private boolean isImmune(LivingEntity entity) {
        return entity instanceof PolarBear || 
               entity instanceof SnowGolem || 
               entity instanceof EnderDragon || 
               entity instanceof WitherBoss;
    }

    private boolean isFireMob(LivingEntity entity) {
        return entity instanceof Blaze || 
               entity instanceof MagmaCube || 
               entity instanceof Strider;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}