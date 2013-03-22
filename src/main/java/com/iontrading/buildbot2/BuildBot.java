package com.iontrading.buildbot2;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;

/**
 * @author g.arora@iontrading.com
 * @version $Id$
 * @since 0.7.6
 */
public class BuildBot extends PircBot {

    private static final String CHANNEL = "#xtp-tests";

    private static final String NAME = "buildbot2";

    private static final Pattern BUILDINFO = Pattern.compile(".*::([a-zA-Z_]*) #(\\d+).*");

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(1);

    private static final Set<String> REPORTED_BUILDS = new HashSet<String>();

    private static final AtomicBoolean START = new AtomicBoolean(true);

    public BuildBot() throws Exception {
        start();
    }

    private void start() throws Exception {
        setName(NAME);
        setVerbose(true);

        connect("192.168.45.44", 6697);
        joinChannel(CHANNEL);
        sendMessage(CHANNEL, Colors.UNDERLINE + "Up & running, will report build status");
    }

    public static void main(String[] args) throws Exception {
        new BuildBot();
    }
}
