package divine.prerequisite.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import divine.prerequisite.Divine;

public class VoidDamageSource {

    public static final ResourceKey<DamageType> VOID_DAMAGE_TYPE = ResourceKey.create(
        Registries.DAMAGE_TYPE,
        ResourceLocation.fromNamespaceAndPath(Divine.MOD_ID, "divine_void_damage")
    );
    
    public static boolean dealVoidDamage(LivingEntity target, Entity attacker, float originalDamage) {
        if (target == null || target.isDeadOrDying()) {
            return false;
        }

        float currentHealth = target.getHealth();
        float damageAmount = originalDamage;

        DamageSource voidDamage = new DamageSource(
            target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(VOID_DAMAGE_TYPE),
            attacker
        );

        boolean success = target.hurt(voidDamage, 0.0001f);
        
        if (success) {
            target.setHealth(currentHealth);
            float newHealth = Math.max(0.0F, currentHealth - damageAmount);
            target.setHealth(newHealth);

            if (newHealth <= 0.0F) {
                handleEntityDeath(target, attacker);
            }
            return true;
        }
        return false;
    }

    private static void handleEntityDeath(LivingEntity target, Entity attacker) {
        DamageSource voidDamage = new DamageSource(
            target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(VOID_DAMAGE_TYPE),
            attacker
        );
        target.die(voidDamage);
    }

    public static boolean isVoidDamage(DamageSource source) {
        return source != null && source.typeHolder().is(VOID_DAMAGE_TYPE.location());
    }
}