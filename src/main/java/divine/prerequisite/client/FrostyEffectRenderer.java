package divine.prerequisite.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import divine.prerequisite.Divine;
import divine.prerequisite.registry.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.model.data.ModelData;

@OnlyIn(Dist.CLIENT)
public class FrostyEffectRenderer {
    private static final ResourceLocation FROST_SHADER = ResourceLocation.fromNamespaceAndPath(Divine.MOD_ID, "shaders/core/frost_overlay");
    private static final RandomSource random = RandomSource.create();
    private static final double FROST_MULTIPLIER = 0.15D;
    
    private static ShaderInstance frostShader;

    @EventBusSubscriber(modid = Divine.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void registerShaders(RegisterShadersEvent event) {
            try {
                event.registerShader(
                    new ShaderInstance(event.getResourceProvider(), FROST_SHADER, DefaultVertexFormat.POSITION_TEX),
                    shader -> frostShader = shader
                );
            } catch (Exception e) {}
        }
    }

    @EventBusSubscriber(modid = Divine.MOD_ID, value = Dist.CLIENT)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onRenderGui(RenderGuiEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            MobEffectInstance effect = mc.player.getEffect(ModEffects.FROSTY);

            if (effect == null) return;

            if (frostShader != null) {
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShader(() -> frostShader);
                frostShader.safeGetUniform("GameTime").set((float)(mc.level.getGameTime() % 24000L) / 20.0F);
                frostShader.safeGetUniform("OutSize").set(mc.getWindow().getWidth(), mc.getWindow().getHeight());

                int width = event.getGuiGraphics().guiWidth();
                int height = event.getGuiGraphics().guiHeight();

                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

                buffer.addVertex(0.0F, height, -90.0F).setUv(0.0F, 1.0F);
                buffer.addVertex(width, height, -90.0F).setUv(1.0F, 1.0F);
                buffer.addVertex(width, 0.0F, -90.0F).setUv(1.0F, 0.0F);
                buffer.addVertex(0.0F, 0.0F, -90.0F).setUv(0.0F, 0.0F);

                BufferUploader.drawWithShader(buffer.buildOrThrow());
                RenderSystem.disableBlend();
            }
        }

        @SubscribeEvent
        public static void onRenderLiving(RenderLivingEvent.Post<?, ?> event) {
            LivingEntity entity = event.getEntity();
            MobEffectInstance effect = entity.getEffect(ModEffects.FROSTY);
            if (effect == null) return;

            random.setSeed(entity.getId() * entity.getId() * 3121L + entity.getId() * 45238971L);

            int baseBlocks = (int)(entity.getBbHeight() / 0.4F);
            int effectBlocks = (int)((effect.getAmplifier() + 1) / FROST_MULTIPLIER);
            int numCubes = baseBlocks + effectBlocks + 1;

            numCubes = Math.max(1, numCubes);
            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource buffer = event.getMultiBufferSource();
            float specialOffset = 0.0F;

            for (int i = 0; i < numCubes; i++) {
                poseStack.pushPose();
                float dx = ((random.nextFloat() * (entity.getBbWidth() * 2.0F)) - entity.getBbWidth()) * 0.1F;
                float dy = Math.max(1.5F - (random.nextFloat()) * (entity.getBbHeight() - specialOffset), -0.1F) - specialOffset;
                float dz = ((random.nextFloat() * (entity.getBbWidth() * 2.0F)) - entity.getBbWidth()) * 0.1F;

                poseStack.translate(dx, dy, dz);
                poseStack.scale(0.5F, 0.5F, 0.5F);
                poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360F));
                poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360F));
                poseStack.translate(-0.5F, -0.5F, -0.5F);

                Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                    Blocks.ICE.defaultBlockState(),
                    poseStack,
                    buffer,
                    event.getPackedLight(),
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY,
                    RenderType.translucentMovingBlock()
                );

                poseStack.popPose();
            }
        }
    }
}