package com.iontrading.buildbot2;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.iontrading.model.Build;

public class TeamcityQueryTest {

    private TeamcityQuery query;

    @Before
    public void beforeMethod() throws Exception {
        query = new TeamcityQuery();
    }

    @Test
    public void testQueryFailsReturnsFailedBuilds() throws Exception {
        Collection<Build> builds = query.queryFails();
        Assert.assertNotNull(builds);
    }
}
