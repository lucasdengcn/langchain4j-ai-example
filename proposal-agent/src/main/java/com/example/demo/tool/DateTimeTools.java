package com.example.demo.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Method as Tools in Spring Ai
 */
@Slf4j
@Component
public class DateTimeTools {

    public static final String GET_CURRENT_DATE_TIME = "getCurrentDateTime";
    public static final String SET_ALARM = "setAlarm";

    @Tool(name = GET_CURRENT_DATE_TIME, value = "Get the current date and time in the user's timezone")
    public String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

    @Tool(name = SET_ALARM, value = "Set a user alarm for the given time, provided in ISO-8601 format, UTC+8")
    public void setAlarm(@P(value = "Time in ISO-8601 format, UTC+8") String time){
        log.info("Setting alarm for: {}", time);
        LocalDateTime alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        log.info("Alarm set for: {}", alarmTime);
    }
}
