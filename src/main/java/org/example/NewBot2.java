package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NewBot2 implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final String channelId;

    public NewBot2(String botToken, String channelId) {
        this.channelId = channelId;
        telegramClient = new OkHttpTelegramClient(botToken);
        scheduleMessage(new SendMessage(channelId, getLPInfo()));
    }

    private String getLPInfo(){
        StringBuilder res = new StringBuilder();
        LaunchPoolInfo.readFile();
        for (LaunchPoolInfo launchPool : LaunchPoolInfo.getLaunchPools()) {
            res.append(launchPool.toString() + '\n');
        }
        return res.toString();
    }

    @Override
    public void consume(Update update) {
    }

    private void sendMessage(SendMessage message) {
        System.out.println(message);
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LaunchPoolInfo.readFile();
        System.out.println("Enter bot token.");
        String botToken = new Scanner(System.in).nextLine();
        System.out.println("Enter channel token.");
        String channelId = new Scanner(System.in).nextLine();
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new NewBot2(botToken, channelId));
            System.out.println("NewBot2 successfully started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scheduleMessage(SendMessage message) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        };
        long period = 6 * 60 * 60;
        scheduler.scheduleAtFixedRate(task, 0, period, TimeUnit.SECONDS);
    }
}
