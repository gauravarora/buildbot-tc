package com.iontrading.buildbot2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BuildBot.class)
public class BuildBotTest {

    private PircBotX buildBot;

    private BuildBot bb;

    @Before
    public void beforeMethod() throws Exception {
        buildBot = spy(new PircBotX());
        bb = new BuildBot(buildBot);
        doNothing().when((buildBot), "connect", Matchers.anyString(), Matchers.anyInt());
    }

    @Test
    public void testNameIsSetOnStart() throws Exception {
        bb.start();
        verifyPrivate(buildBot).invoke("setName", "buildbot");
    }

    @Test
    public void testBotConnectsOnStart() throws Exception {
        bb.start();
        verifyPrivate(buildBot).invoke("connect", Matchers.anyString(), Matchers.anyInt());
    }

    @Test
    public void testBotJoinsOurChannelOnStart() throws Exception {
        doNothing().when((buildBot), "joinChannel", Matchers.anyString());

        bb.start();
        verifyPrivate(buildBot).invoke("joinChannel", "#xtp-tests");
    }

    @Test
    public void testBotSendsMessageOnChannelOnStart() throws Exception {
        doNothing().when((buildBot), "sendMessage", Matchers.anyString(), Matchers.anyString());

        bb.start();
        verifyPrivate(buildBot).invoke("sendMessage", "#xtp-tests",
                Colors.UNDERLINE + "Up & running, will report build status");
    }
}
