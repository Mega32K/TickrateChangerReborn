package net.mega.tc;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber
public class TickrateCommand {
    @SubscribeEvent
    public static void registerTickrateCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("tickrate")
                .then(Commands.literal("change").then(Commands.argument("value", FloatArgumentType.floatArg(0))
                        .executes((s) -> changeAndSend(s, FloatArgumentType.getFloat(s, "value")))
                ))
                .then(Commands.literal("jump").then(Commands.argument("value", IntegerArgumentType.integer(1, 36000))
                        .executes((s) -> jump(s, IntegerArgumentType.getInteger(s, "value")))
                ))
                .requires((s) -> s.hasPermission(2))
        );
    }

    private static int changeAndSend(CommandContext<CommandSourceStack> s, float tick) {
        tick = Float.parseFloat(String.valueOf(tick));
        s.getSource().sendSuccess(new TextComponent("tickrate changed to ").append(new TextComponent(String.valueOf(tick))).withStyle(ChatFormatting.DARK_GRAY), false);
        Main.send(tick);
        if (!s.getSource().getLevel().isClientSide) {
            for (ServerPlayer player : s.getSource().getServer().getPlayerList().getPlayers()) {
                Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new Networking(tick));
            }
        }
        return 0;
    }

    private static int jump(CommandContext<CommandSourceStack> s, int ticks) {
        s.getSource().sendSuccess(new TextComponent("tickrate jumped ").append(new TextComponent(String.valueOf(ticks))).withStyle(ChatFormatting.DARK_GRAY), false);
        Main.jump(ticks);
        return 0;
    }
}
