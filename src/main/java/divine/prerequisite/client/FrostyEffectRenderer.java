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
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Divine.MOD_ID, value = Dist.CLIENT)
public class FrostyEffectRenderer {
    private static final RandomSource random = RandomSource.create();
    private static final double FROST_MULTIPLIER = 0.15D;

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        MobEffectInstance effect = mc.player.getEffect(ModEffects.FROSTY.get());
        if (effect == null) return;

        ShaderInstance frostShader = ModShaders.getFrostShader();
        if (frostShader != null) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(() -> frostShader);
            frostShader.safeGetUniform("GameTime").set((float) (mc.level.getGameTime() % 24000L) / 20.0F);
            frostShader.safeGetUniform("OutSize").set(mc.getWindow().getWidth(), mc.getWindow().getHeight());

            int width = event.getWindow().getGuiScaledWidth();
            int height = event.getWindow().getGuiScaledHeight();

            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            buffer.vertex(0.0D, height, -90.0D).uv(0.0F, 1.0F).endVertex();
            buffer.vertex(width, height, -90.0D).uv(1.0F, 1.0F).endVertex();
            buffer.vertex(width, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
            buffer.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
            tesselator.end();

            RenderSystem.disableBlend();
        }
    }

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effect = entity.getEffect(ModEffects.FROSTY.get());
        if (effect == null) return;

        random.setSeed(entity.getId() * entity.getId() * 3121L + entity.getId() * 45238971L);

        int baseBlocks = (int) (entity.getBbHeight() / 0.4F);
        int effectBlocks = (int) ((effect.getAmplifier() + 1) / FROST_MULTIPLIER);
        int numCubes = baseBlocks + effectBlocks + 1;

        numCubes = Math.max(1, numCubes);

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource buffer = event.getMultiBufferSource();

        float entityWidth = entity.getBbWidth();
        float entityHeight = entity.getBbHeight();

        float minY = 0.1F;
        float maxY = entityHeight * 0.9F;

        for (int i = 0; i < numCubes; i++) {
            float dx = (random.nextFloat() - 0.5F) * entityWidth * 0.4F;
            float dy = minY + random.nextFloat() * (maxY - minY);
            float dz = (random.nextFloat() - 0.5F) * entityWidth * 0.4F;

            dx *= 0.7F + random.nextFloat() * 0.3F;
            dz *= 0.7F + random.nextFloat() * 0.3F;

            poseStack.pushPose();

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