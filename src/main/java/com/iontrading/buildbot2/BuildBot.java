package com.iontrading.buildbot2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import com.iontrading.model.Build;

public class BuildBot extends ListenerAdapter {

    private static final String CHANNEL = "#xtp-tests";

    private static final String NAME = "buildbot";

    private final PircBotX bot;

    private final List<String> messagesSentToIRC;

    private final IQuery query;

    public BuildBot(PircBotX botX, IQuery query) throws Exception {
        this.bot = botX;
        this.query = query;
        messagesSentToIRC = new ArrayList<String>();
    }

    public void start() throws Exception {
        // This class is a listener, so add it to the bots known listeners
        bot.getListenerManager().addListener(this);

        bot.setName(NAME);
        bot.setVerbose(true);

        bot.connect("192.168.45.44", 6697);
        bot.joinChannel(CHANNEL);
        bot.sendMessage(CHANNEL, Colors.UNDERLINE + "Up & running, will report build status");
    }

    public static void main(String[] args) throws Exception {
        BuildBot bot = new BuildBot(new PircBotX(), new TeamcityQuery(new TeamCityFeedReader()));
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
        Collection<Build> builds = null;
        if ("!fails".equalsIgnoreCase(event.getMessage())) {
            builds = query.queryFails();
        } else if ("!list".equalsIgnoreCase(event.getMessage())) {
            builds = query.queryAll();
        }

        for (Build build : builds) {
            bot.sendAction(event.getUser(), build.getStatusText());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onJoin(JoinEvent event) throws Exception {
        event.respond(Colors.UNDERLINE + "Up & running, will report build status");
    }
}
