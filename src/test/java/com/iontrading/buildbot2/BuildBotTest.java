package com.iontrading.buildbot2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BuildBot.class)
public class BuildBotTest {

    private PircBotX buildBot;

    private BuildBot bb;

    private IQuery query;

    @Before
    public void beforeMethod() throws Exception {
        buildBot = spy(new PircBotX());
        query = Mockito.mock(IQuery.class);
        bb = new BuildBot(buildBot, query);
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

    @Test
    public void testBotQueriesForFailingTestsOnPriveMessage() throws Exception {
        PrivateMessageEvent<PircBotX> event = new PrivateMessageEvent<PircBotX>(buildBot, null, "some message");
        bb.onPrivateMessage(event);
        Mockito.verify(query).queryFails();
    }
}
