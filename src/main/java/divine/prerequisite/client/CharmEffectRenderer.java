package divine.prerequisite.client;

import divine.prerequisite.Divine;
import divine.prerequisite.registry.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Divine.MOD_ID, value = Dist.CLIENT)
public class CharmEffectRenderer {
    private static final ResourceLocation HEART_TEXTURE = ResourceLocation.withDefaultNamespace("textures/particle/heart.png");
    private static final RandomSource RANDOM = RandomSource.create();
    
    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();

        MobEffectInstance effect = entity.getEffect(ModEffects.CHARM);
        if (effect != null) {
            renderCharmEffect(entity, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick(), event.getPackedLight());
        }
    }
    
    private static void renderCharmEffect(LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, float partialTick, int packedLight) {
        int amplifier = entity.getEffect(ModEffects.CHARM).getAmplifier() + 1;

        poseStack.pushPose();
        poseStack.translate(0, entity.getBbHeight() + 0.5 + Math.sin(entity.tickCount * 0.05) * 0.1, 0);

        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        }

        float scale = 0.3f + amplifier * 0.05f;
        poseStack.scale(-scale, -scale, scale);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(HEART_TEXTURE));
        
        int heartCount = Math.min(amplifier, 5);
        for (int i = 0; i < heartCount; i++) {
            float angle = (float) (i * Math.PI * 2 / heartCount);
            float radius = 0.7f;
            float x = (float) Math.sin(angle + entity.tickCount * 0.05) * radius;
            float y = (float) Math.cos(angle + entity.tickCount * 0.05) * radius;
            
            poseStack.pushPose();
            poseStack.translate(x, y, 0);
            poseStack.mulPose(Axis.ZP.rotationDegrees(entity.tickCount * 2 + i * 45));

            int light = packedLight;
            int overlayCoords = 655360;

            float red = 1.0f;
            float green = 0.3f + (float) Math.sin(entity.tickCount * 0.1 + i) * 0.2f;
            float blue = 0.5f + (float) Math.sin(entity.tickCount * 0.1 + i) * 0.2f;
            float alpha = 0.8f;

            var pose = poseStack.last().pose();
            
            vertexConsumer.addVertex(pose, -0.5f, -0.5f, 0).setColor(red, green, blue, alpha).setUv(0, 1).setOverlay(overlayCoords).setLight(light).setNormal(0, 0, 1);
            vertexConsumer.addVertex(pose, 0.5f, -0.5f, 0).setColor(red, green, blue, alpha).setUv(1, 1).setOverlay(overlayCoords).setLight(light).setNormal(0, 0, 1);
            vertexConsumer.addVertex(pose, 0.5f, 0.5f, 0).setColor(red, green, blue, alpha).setUv(1, 0).setOverlay(overlayCoords).setLight(light).setNormal(0, 0, 1);
            vertexConsumer.addVertex(pose, -0.5f, 0.5f, 0).setColor(red, green, blue, alpha).setUv(0, 0).setOverlay(overlayCoords).setLight(light).setNormal(0, 0, 1);
            
            poseStack.popPose();
        }
        poseStack.popPose();
    }
}