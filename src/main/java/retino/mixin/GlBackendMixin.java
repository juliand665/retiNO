package retino.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets = "com.mojang.blaze3d.platform.Window")
public abstract class GlBackendMixin {
	@ModifyArg(
		method = "createWindow",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/renderpearl/api/device/GpuBackend;createWindow(Ljava/lang/String;IIJ)J"
		),
		index = 3
	)
	private long disableHighPixelDensity(long flags) {
		try {
			Class<?> sdlVideo = Class.forName("org.lwjgl.sdl.SDLVideo");
			long highPixelDensity =
				sdlVideo.getField("SDL_WINDOW_HIGH_PIXEL_DENSITY").getLong(null);

			return flags & ~highPixelDensity;
		} catch (ReflectiveOperationException e) {
			return flags;
		}
	}
}