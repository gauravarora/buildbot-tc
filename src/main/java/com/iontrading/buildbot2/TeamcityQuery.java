package com.iontrading.buildbot2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.iontrading.buildbot.App;
import com.iontrading.model.Build;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntry;

public class TeamcityQuery implements IQuery {

    private final IFeedReader feedReader;

    /**
     * @param feedReader
     */
    public TeamcityQuery(final IFeedReader feedReader) {
        this.feedReader = feedReader;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Build> queryFails() {
        List<Build> entries = new ArrayList<Build>();
        List<SyndEntry> allBuilds = feedReader.getBuilds();
        for (SyndEntry entry : allBuilds) {
            if (entry.getAuthor().contains("Failed")) {
                Build b = new Build();
                b.setStatusText("FailedBuild");
                entries.add(b);
            }
        }
        return entries;
    }

    /** {@inheritDoc} */
    public Collection<Build> queryAll() {
        List<Build> entries = new ArrayList<Build>();
        List<SyndEntry> allBuilds = feedReader.getBuilds();
        for (final SyndEntry entry : allBuilds) {
            Build b = new Build();
            b.setStatusText("Build");
            entries.add(b);
        }
        return entries;
    }

}
