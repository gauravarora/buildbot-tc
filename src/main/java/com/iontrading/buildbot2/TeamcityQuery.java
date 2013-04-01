package com.iontrading.buildbot2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.iontrading.buildbot.App;
import com.iontrading.model.Build;

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
        List<Build> allBuilds = feedReader.getBuilds();
        for (Build entry : allBuilds) {
            if (entry.isFailure()) {
                entries.add(entry);
            }
        }
        return entries;
    }

    /** {@inheritDoc} */
    public Collection<Build> queryAll() {
        List<Build> allBuilds = feedReader.getBuilds();
        return allBuilds;
    }

}
