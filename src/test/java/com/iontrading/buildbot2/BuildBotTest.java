package com.iontrading.buildbot2;

import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

import org.jibble.pircbot.Colors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * The Class BuildBotTest.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BuildBot.class)
public class BuildBotTest {

    /** The build bot. */
    private BuildBot buildBot;

    /**
     * Before method.
     * @throws Exception the exception
     */
    @Before
    public void beforeMethod() throws Exception {
        buildBot = spy(new BuildBot());
        doNothing().when((buildBot), "connect", Matchers.anyString(), Matchers.anyInt());
    }

    /**
     * Test name is set on start.
     * @throws Exception the exception
     */
    @Test
    public void testNameIsSetOnStart() throws Exception {
        buildBot.start();
        verifyPrivate(buildBot).invoke("setName", "buildbot2");
    }

    /**
     * Test bot connects on start.
     * @throws Exception the exception
     */
    @Test
    public void testBotConnectsOnStart() throws Exception {
        buildBot.start();
        verifyPrivate(buildBot).invoke("connect", Matchers.anyString(), Matchers.anyInt());
    }

    /**
     * Test bot joins our channel on start.
     * @throws Exception the exception
     */
    @Test
    public void testBotJoinsOurChannelOnStart() throws Exception {
        doNothing().when((buildBot), "joinChannel", Matchers.anyString());

        buildBot.start();
        verifyPrivate(buildBot).invoke("joinChannel", "#xtp-tests");
    }

    /**
     * Test bot sends message on channel on start.
     * @throws Exception the exception
     */
    @Test
    public void testBotSendsMessageOnChannelOnStart() throws Exception {
        doNothing().when((buildBot), "sendMessage", Matchers.anyString(), Matchers.anyString());

        buildBot.start();
        verifyPrivate(buildBot).invoke("sendMessage", "#xtp-tests",
                Colors.UNDERLINE + "Up & running, will report build status");
    }

    /**
     * Test bot reports status to irc.
     * @throws Exception the exception
     */
    @Test
    public void testBotReportsSuccessStatusToIRC() throws Exception {
        /*
         * This mocking is actually not needed as it is not doing something which can break stuff. But it is a good
         * practice to add this mock, as this makes the test case independent of the internal implementation of
         * sendMessage().
         */
        // doNothing().when((buildBot), "sendMessage", Matchers.anyString(), Matchers.anyString());
        String title = "buildTitle";
        String link = "buildLink";
        buildBot.reportSuccessStatusToIRC(title, link);
        verifyPrivate(buildBot).invoke("sendMessage", "#xtp-tests",
                Colors.GREEN + title + " (" + link + ") [" + "Success" + "]");
    }

    /**
     * Test bot reports failure status to irc.
     * @throws Exception the exception
     */
    @Test
    public void testBotReportsFailureStatusToIRC() throws Exception {
        /*
         * This mocking is actually not needed as it is not doing something which can break stuff. But it is a good
         * practice to add this mock, as this makes the test case independent of the internal implementation of
         * sendMessage().
         */
        // doNothing().when((buildBot), "sendMessage", Matchers.anyString(), Matchers.anyString());
        String title = "buildTitle";
        String link = "buildLink";
        buildBot.reportFailureStatusToIRC(title, link);
        verifyPrivate(buildBot).invoke("sendMessage", "#xtp-tests",
                Colors.RED + title + " (" + link + ") [" + "Failure" + "]");
    }
}
