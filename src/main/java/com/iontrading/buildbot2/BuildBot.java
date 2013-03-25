package com.iontrading.buildbot2;

import org.pircbotx.Colors;
import org.pircbotx.PircBotX;

public class BuildBot {

    private static final String CHANNEL = "#xtp-tests";

    private static final String NAME = "buildbot";

    private final PircBotX bot;

    public BuildBot(final PircBotX botX) throws Exception {
        this.bot = botX;
    }

    public void start() throws Exception {
        bot.setName(NAME);
        bot.setVerbose(true);

        bot.connect("192.168.45.44", 6697);
        bot.joinChannel(CHANNEL);
        bot.sendMessage(CHANNEL, Colors.UNDERLINE + "Up & running, will report build status");
    }

    public static void main(String[] args) throws Exception {
        BuildBot bot = new BuildBot(new PircBotX());
        bot.start();
    }
}
