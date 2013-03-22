package com.iontrading.buildbot2;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;

import com.sun.syndication.feed.synd.SyndEntry;

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

        SCHEDULED_EXECUTOR_SERVICE.scheduleWithFixedDelay(new Runnable() {

            public void run() {
                try {
                    System.out.println("Getting builds");

                    for (SyndEntry entry : getBuilds()) {
                        String key = makeKey(entry.getTitle());
                        System.out.println("Key is " + key);
                        if (START.get()) {
                            REPORTED_BUILDS.add(key);
                        }

                        if (REPORTED_BUILDS.contains(key)) {
                            System.out.println("Key found " + key);
                            continue;
                        }
                        System.out.println("Seding key " + key);
                        if (entry.getAuthor().contains("Failed")) {
                            bot.sendMessage(CHANNEL, Colors.RED + entry.getTitle() + " (" + entry.getLink() + ")");
                        }
                        if (entry.getAuthor().contains("Success")) {
                            bot.sendMessage(CHANNEL, Colors.DARK_GREEN + entry.getTitle() + " (" + entry.getLink()
                                    + ")");
                        }
                        REPORTED_BUILDS.add(key);
                    }
                    START.set(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private String makeKey(String title) {
                Matcher matcher = BUILDINFO.matcher(title);
                matcher.matches();
                matcher.groupCount();
                return matcher.group(1) + "_" + matcher.group(2);
            }
        }, 1, 120, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws Exception {
        new BuildBot();
    }
}
