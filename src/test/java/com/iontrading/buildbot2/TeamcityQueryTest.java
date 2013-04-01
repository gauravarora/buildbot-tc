package com.iontrading.buildbot2;

import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.iontrading.model.Build;
import com.sun.syndication.feed.synd.SyndEntry;

public class TeamcityQueryTest {

    private TeamcityQuery query;

    private IFeedReader feedReader;

    @Before
    public void beforeMethod() throws Exception {
        feedReader = mock(IFeedReader.class);
        query = new TeamcityQuery(feedReader);
    }

    @Test
    public void testQueryFailsReturnsFailedBuilds() throws Exception {
        SyndEntry successBuild = mock(SyndEntry.class);
        when(successBuild.getAuthor()).thenReturn("Success");
        SyndEntry failedBuild = mock(SyndEntry.class);
        when(failedBuild.getAuthor()).thenReturn("Failed");

        List<SyndEntry> expectedBuilds = Lists.newArrayList(successBuild, failedBuild);
        when(feedReader.getBuilds()).thenReturn(expectedBuilds);
        Collection<Build> builds = query.queryFails();
        verify(feedReader).getBuilds();
        Assert.assertNotNull(builds);
        Assert.assertEquals(builds.size(), 1);
    }

    @Test
    public void testQueryFailsReturnsNoBuilds() throws Exception {
        SyndEntry successBuild = mock(SyndEntry.class);
        when(successBuild.getAuthor()).thenReturn("Success");

        List<SyndEntry> expectedBuilds = Lists.newArrayList(successBuild);
        when(feedReader.getBuilds()).thenReturn(expectedBuilds);
        Collection<Build> builds = query.queryFails();
        verify(feedReader).getBuilds();
        Assert.assertNotNull(builds);
        Assert.assertEquals(builds.size(), 0);
    }
}
