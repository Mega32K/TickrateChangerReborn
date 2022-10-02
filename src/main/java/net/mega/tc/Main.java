package net.mega.tc;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mod(Main.MODID)
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class Main {
    public static float PERCENT = 20F;
    public static double millisF = 0F;
    public static long millis = 0;
    public static Timer timer;
    public static ScheduledExecutorService service;
    public static final String MODID = "tickrate_changer_reborn";

    public static void send(Object o) {
        System.out.println("TickrateChanger Mod: " + o + ".");
    }

    public Main() {
        create();
    }

    public static void create() {
        if (service == null)
            service = Executors.newSingleThreadScheduledExecutor();

        if (timer == null)
            timer = new Timer();
        try {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    service.scheduleAtFixedRate(Main::update, 1L, 1L, TimeUnit.MILLISECONDS);
                }
            }, 1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void update() {
        float p = PERCENT / 20.0F;
        millisF = p + millisF;
        millis = (long) millisF;
    }

    public static void changeAll(float percent) {
        PERCENT = percent;
        send("PER " + PERCENT);
        send("add " + percent / 20.0F);
        send("TickLengthChanged ->" + PERCENT);
    }

    public static void jump(int ticks) {
        millisF += ticks * 50L;
    }
}
