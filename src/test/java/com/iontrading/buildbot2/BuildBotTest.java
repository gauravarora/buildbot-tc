package com.iontrading.buildbot2;

import org.jibble.pircbot.Colors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BuildBot.class)
public class BuildBotTest {

    private BuildBot buildBot;
    
    @Before
    public void beforeMethod() throws Exception {
        buildBot = spy(new BuildBot());
        doNothing().when((buildBot), "connect", Matchers.anyString(), Matchers.anyInt());
    }
    
    @Test
    public void testNameIsSetOnStart() throws Exception {
        buildBot.start();
        verifyPrivate(buildBot).invoke("setName", "buildbot2");
    }
    
    @Test
    public void testBotConnectsOnStart() throws Exception {
        buildBot.start();
        verifyPrivate(buildBot).invoke("connect", Matchers.anyString(), Matchers.anyInt());
    }

    @Test
    public void testBotJoinsOurChannelOnStart() throws Exception {
        doNothing().when((buildBot), "joinChannel", Matchers.anyString());

        buildBot.start();
        verifyPrivate(buildBot).invoke("joinChannel", "#xtp-tests");
    }

    @Test
    public void testBotSendsMessageOnChannelOnStart() throws Exception {
        doNothing().when((buildBot), "sendMessage", Matchers.anyString(), Matchers.anyString());

        buildBot.start();
        verifyPrivate(buildBot).invoke("sendMessage", "#xtp-tests", Colors.UNDERLINE + "Up & running, will report build status");
    }
}
