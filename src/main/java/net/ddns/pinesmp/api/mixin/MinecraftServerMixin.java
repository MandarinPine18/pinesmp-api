package net.ddns.pinesmp.api.mixin;

import net.ddns.pinesmp.api.util.MinecraftServerInterface;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements MinecraftServerInterface {
}
