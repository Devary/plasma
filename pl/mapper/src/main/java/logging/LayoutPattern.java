package logging;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class LayoutPattern extends PatternLayout {
    @Override
    public String format(LoggingEvent event) {
        return "Time elapsed : "+ super.format(event);
    }
}
