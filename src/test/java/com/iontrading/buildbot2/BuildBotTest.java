package com.iontrading.buildbot2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author A.Jassal
 * @version $Id: $
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(BuildBot.class)
public class BuildBotTest {

    /**
     * Checks if is start invoked.
     * @throws Exception the exception
     */
    @Test
    public void isStartInvoked() throws Exception {

        BuildBot buildBot = spy(new BuildBot());
        doNothing().when((buildBot), "connect", Matchers.anyString(), Matchers.anyInt());
        buildBot.start();
        verifyPrivate(buildBot).invoke("setName", "buildbot2");

        // This verification will faile as setName() is not invoked with "buildbot" arguments.
        // verifyPrivate(buildBot).invoke("setName", "buildbot");
        verifyPrivate(buildBot).invoke("connect", Matchers.anyString(), Matchers.anyInt());

    }
}
