package com.iontrading.buildbot2;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import org.pircbotx.Colors;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.managers.ListenerManager;

import com.google.common.collect.Lists;
import com.iontrading.model.Build;

public class BuildBotTest {

    private PircBotX buildBot;

    private BuildBot bb;

    private IQuery query;

    @Before
    public void beforeMethod() throws Exception {
        buildBot = mock(PircBotX.class);
        query = mock(IQuery.class);
        ListenerManager listenerManager = mock(ListenerManager.class);
        bb = new BuildBot(buildBot, query);

        when(buildBot.getListenerManager()).thenReturn(listenerManager);
        doNothing().when(buildBot).connect(anyString());
        doNothing().when(buildBot).sendAction(isA(User.class), anyString());
    }

    @Test
    public void testNameIsSetOnStart() throws Exception {
        bb.start();
        verify(buildBot).setName("buildbot");
    }

    @Test
    public void testBotConnectsOnStart() throws Exception {
        bb.start();
        verify(buildBot).connect(anyString(), anyInt());
    }

    @Test
    public void testBotJoinsOurChannelOnStart() throws Exception {
        doNothing().when(buildBot).joinChannel(anyString());

        bb.start();
        verify(buildBot).joinChannel("#xtp-tests");
    }

    @Test
    public void testBotSendsMessageOnChannelOnStart() throws Exception {
        doNothing().when(buildBot).sendMessage(anyString(), anyString());

        bb.start();
        verify(buildBot).sendMessage("#xtp-tests",
                Colors.UNDERLINE + "Up & running, will report build status");
    }

    @Test
    public void testBotReportsFailingTestsOnPriveMessage() throws Exception {
        User user = mock(User.class);
        Build build = mock(Build.class);
        String statusTxt = "Status text for test build";

        Collection<Build> builds = Lists.newArrayList(build);
        when(query.queryFails()).thenReturn(builds);
        when(build.getStatusText()).thenReturn(statusTxt);

        PrivateMessageEvent<PircBotX> event = new PrivateMessageEvent<PircBotX>(buildBot, user, "some message");
        bb.onPrivateMessage(event);

        verify(query).queryFails();
        verify(buildBot).sendAction(user, statusTxt);
    }
}
