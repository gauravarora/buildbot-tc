package com.iontrading.buildbot2;

import java.util.ArrayList;
import java.util.List;

import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class BuildBot extends ListenerAdapter {

    private static final String CHANNEL = "#xtp-tests";

    private static final String NAME = "buildbot";

    private final PircBotX bot;

    private final List<String> messagesSentToIRC;

    public BuildBot(final PircBotX botX) throws Exception {
        this.bot = botX;
        messagesSentToIRC = new ArrayList<String>();
    }

    public void start() throws Exception {

        // This class is a listener, so add it to the bots known listeners
        bot.getListenerManager().addListener(this);

        bot.setName(NAME);
        bot.setVerbose(true);

        bot.connect("192.168.45.44", 6697);
        bot.joinChannel(CHANNEL);
    }

    public static void main(String[] args) throws Exception {
        BuildBot bot = new BuildBot(new PircBotX());
        bot.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        super.onMessage(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
        super.onPrivateMessage(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onJoin(JoinEvent event) throws Exception {
        event.respond(Colors.UNDERLINE + "Up & running, will report build status");
    }

    public void reportFailureStatusToIRC(final String title, final String link) throws Exception {
        String message = Colors.RED + populateMessage(title, link, "Failure");
        sendMessage(CHANNEL, message);
        messagesSentToIRC.add(message);
    }

    public void reportSuccessStatusToIRC(final String title, final String link) throws Exception {
        String message = Colors.GREEN + populateMessage(title, link, "Success");
        sendMessage(CHANNEL, message);
        messagesSentToIRC.add(message);
    }

    private String populateMessage(final String title, final String link, final String status) {
    }

}
