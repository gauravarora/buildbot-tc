package com.iontrading.buildbot2;

import java.util.List;

import com.iontrading.model.Build;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

/**
 * @author A.Jassal
 * @version $Id: $
 */
public interface IFeedReader {

    List<Build> getBuilds();

}
