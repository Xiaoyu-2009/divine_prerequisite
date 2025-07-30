package divine.prerequisite.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import divine.prerequisite.Divine;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Divine.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModShaders {
    private static ShaderInstance frostShader;

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) {
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(),
                    new ResourceLocation(Divine.MOD_ID, "shaders/core/frost_overlay"),
                    DefaultVertexFormat.POSITION_TEX),
                    shader -> frostShader = shader);
        } catch (Exception e) {}
    }

    public static ShaderInstance getFrostShader() {
        return frostShader;
    }
}