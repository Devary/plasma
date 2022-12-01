package com.atlassian.jira.rest.client.app.logging;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class LayoutPattern extends PatternLayout {
    @Override
    public String format(LoggingEvent event) {
        return super.format(event);
    }
}
