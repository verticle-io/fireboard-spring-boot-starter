package io.verticle.oss.fireboard.springboot.starter.logger.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.verticle.oss.fireboard.client.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jens Saade
 */
public class FireboardAppender extends AppenderBase {

    PatternLayoutEncoder encoder;

    //defaults
    String ident = "application.spring.boot.ident";
    String category = "application.spring.boot.category";
    String logLevel = "warn";
    boolean debug = false;



    @Override
    protected void append(Object o) {

        if (o instanceof ILoggingEvent){
            ILoggingEvent event = (ILoggingEvent) o;

            Level level = Level.toLevel(logLevel);
            if (! event.getLevel().isGreaterOrEqual(level)){
                return;
            }

            // output the events as formatted by our layout
            try {
                this.encoder.doEncode(event);

                if (debug)
                    System.out.println("FIREBOARD-DEBUG sending msg> " + event.getMessage());

                int severity = 1;

                StatusEnum status = StatusEnum.success;
                if (event.getLevel().equals(Level.TRACE)) {
                    status = StatusEnum.success;
                    severity = 1;
                } else  if (event.getLevel().equals(Level.ERROR)) {
                    status = StatusEnum.error;
                    severity = 4;
                } else  if (event.getLevel().equals(Level.WARN)) {
                    status = StatusEnum.warn;
                    severity = 3;
                } else  if (event.getLevel().equals(Level.INFO)) {
                    status = StatusEnum.info;
                    severity = 2;
                }

                List<MessagePropertySection> sectionList = new ArrayList<MessagePropertySection>();
                sectionList.addAll(new MessagePropertyHelper()
                        .section("source")
                        .property("logger", event.getLoggerName())
                        .property("thread", event.getThreadName())
                        .property("time", new Date(event.getTimeStamp()).toString())
                        .build());

                StringBuffer stackBuffer = new StringBuffer();
                for (StackTraceElement element : event.getCallerData()){
                    stackBuffer.append(element.toString());
                    stackBuffer.append(" ");
                }

                sectionList.addAll(new MessagePropertyHelper()
                        .section("Exception")
                        .property("stacktrace", stackBuffer.toString())
                        .build());

                String message = event.getMessage() != null ? event.getMessage() : "";
                System.out.println("category: " + category);
                System.out.println("ident: " + ident);
                try {
                    FireboardClient.post(
                            new FireboardMessageBuilder()
                                    .event(event.getLevel().toString() + " " + StringUtils.abbreviate(event.getMessage(), 20))
                                    .category(category)
                                    .severity(severity)
                                    .ident(ident)
                                    .message(message)
                                    .status(status)
                                    .link(new URL("https://fireboard.verticle.io"))
                                    .properties(sectionList)
                                    .build()
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void start() {
        if (this.encoder == null) {
            addError("No encoder set for the appender named ["+ name +"].");
            return;
        }

        try {
            encoder.init(System.out);
        } catch (IOException e) {
            System.out.println("Error initializing encoder: " + e.getMessage());
        }

        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }


    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }

}
