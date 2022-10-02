package net.mega.tc.mixin;

import net.mega.tc.Main;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = Util.class, priority = Integer.MAX_VALUE)
public abstract class UtilMixin {

    /**
     * @author mega
     * @reason change the add speed
     */
    @Overwrite
    public static long getMillis() {
        return Main.millis;
    }
}
