package com.iontrading.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author g.arora@iontrading.com
 * @version $Id$
 * @since 0.7.6
 */
@Root(strict = false)
public class Build {

    @Element
    private String statusText;

    /**
     * @return the statusText
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * @param statusText the statusText to set
     */
    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
}
