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
        Build successBuild = mock(Build.class);
        when(successBuild.isFailure()).thenReturn(false);
        Build failedBuild = mock(Build.class);
        when(failedBuild.isFailure()).thenReturn(true);

        List<Build> expectedBuilds = Lists.newArrayList(successBuild, failedBuild);
        when(feedReader.getBuilds()).thenReturn(expectedBuilds);
        Collection<Build> builds = query.queryFails();
        verify(feedReader).getBuilds();
        Assert.assertNotNull(builds);
        Assert.assertEquals(builds.size(), 1);
    }

    @Test
    public void testQueryFailsReturnsNoBuilds() throws Exception {
        Build successBuild = mock(Build.class);
        when(successBuild.isFailure()).thenReturn(false);

        List<Build> expectedBuilds = Lists.newArrayList(successBuild);
        when(feedReader.getBuilds()).thenReturn(expectedBuilds);
        Collection<Build> builds = query.queryFails();
        verify(feedReader).getBuilds();
        Assert.assertNotNull(builds);
        Assert.assertEquals(builds.size(), 0);
    }
}
