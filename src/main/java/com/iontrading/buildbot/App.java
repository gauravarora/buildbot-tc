package com.iontrading.buildbot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.sun.syndication.io.impl.Base64;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        Iterator itEntries = readFeed();

        while (itEntries.hasNext()) {
            SyndEntry entry = (SyndEntry) itEntries.next();
            // System.out.println(entry);
            System.out.println(entry.getTitle());
            System.out.println(entry.getLink());
            System.out.println(entry.getAuthor());
            System.out.println();
        }
    }

    /**
     * @return
     * @throws MalformedURLException
     * @throws IOException
     * @throws FeedException
     */
    public static Iterator readFeed() throws MalformedURLException, IOException, FeedException {
        URL url = new URL(
                "http://pauli.iontrading.com/httpAuth/feed.html?projectId=project175&itemsType=builds&buildStatus=successful&buildStatus=failed&userKey=feed");
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        String encoded = Base64.encode("garora:gaurav"); 
        httpcon.setRequestProperty("Authorization", "Basic "+encoded);
        
        // Reading the feed
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(httpcon));
        List entries = feed.getEntries();
        Iterator itEntries = entries.iterator();
        return itEntries;
    }
}
