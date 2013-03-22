package com.iontrading.buildbot;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.iontrading.model.Build;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.sun.syndication.io.impl.Base64;

/**
 * @author g.arora@iontrading.com
 * @version $Id$
 * @since 0.7.6
 */
public class Bot extends PircBot {

    private static final String CHANNEL = "#xtp-tests";

    private static final Pattern BUILDINFO = Pattern.compile(".*::([a-zA-Z_]*) #(\\d+).*");

    private static final Pattern BUILDID = Pattern.compile(".*buildId=(\\d+)&.*");

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(1);

    private static final Set<String> REPORTED_BUILDS = new HashSet<String>();

    private static final AtomicBoolean START = new AtomicBoolean(true);

    public Bot() {
        setName("buildbot");
    }

    public static void main(String[] args) throws Exception {
        final Bot bot = new Bot();
        bot.setVerbose(true);
        // Connect to the IRC server.
        bot.connect("192.168.45.44", 6697);

        // Join the #pircbot channel.
        bot.joinChannel(CHANNEL);
        bot.sendMessage(CHANNEL, Colors.UNDERLINE + "Up & running, will report build status");
        SCHEDULED_EXECUTOR_SERVICE.scheduleWithFixedDelay(new Runnable() {

            public void run() {
                try {
                    System.out.println("Getting builds");

                    for (SyndEntry entry : getBuilds()) {
                        String key = makeKey(entry.getTitle());
                        // System.out.println("Key is " + key);
                        if (START.get()) {
                            REPORTED_BUILDS.add(key);
                        }

                        if (REPORTED_BUILDS.contains(key)) {
                            // System.out.println("Key found " + key);
                            continue;
                        }
                        // System.out.println("Seding key " + key);
                        if (entry.getAuthor().contains("Failed")) {
                            String link = entry.getLink();
                            Matcher matcher = BUILDID.matcher(link);
                            String failures = "";
                            if (matcher.matches() && matcher.groupCount() > 0) {
                                URL url = new URL("http://192.168.150.38/httpAuth/app/rest/builds/id:"
                                        + matcher.group(1));
                                System.out.println("URL is  " + url);
                                HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
                                String encoded = Base64.encode("garora:gaurav");
                                httpcon.setRequestProperty("Authorization", "Basic " + encoded);

                                Serializer serializer = new Persister();
                                Build build = serializer.read(Build.class, httpcon.getInputStream());
                                System.out.println(build.getStatusText());
                                failures = build.getStatusText();
                            }
                            bot.sendMessage(CHANNEL, Colors.RED + entry.getTitle() + " (" + link + ") [" + failures
                                    + "]");
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

    /** {@inheritDoc} */
    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        super.onMessage(channel, sender, login, hostname, message);
    }

    private static final List<SyndEntry> getFails() throws Exception {
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        for (SyndEntry entry : getBuilds()) {
            if (entry.getAuthor().contains("Failed")) {
                entries.add(entry);
            }
        }
        return entries;
    }

    private static final List<SyndEntry> getBuilds() throws Exception {
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        Iterator iterator = App.readFeed();
        while (iterator.hasNext()) {
            entries.add((SyndEntry) iterator.next());
        }
        return entries;
    }

    /** {@inheritDoc} */
    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        super.onPrivateMessage(sender, login, hostname, message);
        if (message.equals("!fails")) {
            try {
                for (SyndEntry entry : getFails()) {
                    sendMessage(CHANNEL, entry.getTitle() + "(" + entry.getLink() + ")");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (message.equals("!checkbuild")) {
            System.out.println("check builds");
            try {
                URL url = new URL("http://192.168.150.38/httpAuth/app/rest/builds/id:875130");
                HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
                String encoded = Base64.encode("garora:gaurav");
                httpcon.setRequestProperty("Authorization", "Basic " + encoded);

                Serializer serializer = new Persister();
                Build build = serializer.read(Build.class, httpcon.getInputStream());
                System.out.println(build.getStatusText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
