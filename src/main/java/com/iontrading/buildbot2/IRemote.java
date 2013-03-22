package com.iontrading.buildbot2;

import java.util.Collection;

import com.sun.syndication.feed.synd.SyndEntry;

/**
 * @author g.arora@iontrading.com
 * @version $Id$
 * @since 0.7.6
 */
public interface IRemote {

    Collection<SyndEntry> getBuilds();
}
