package com.pgrg.springbatch.utils;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CustomMessageSource {

    private final MessageSource messageSource;

    public CustomMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String message){
        return messageSource.getMessage(message, null, Locale.ENGLISH);
    }

    public String getMessage(String message, Object... objects){
        return messageSource.getMessage(message, objects, Locale.ENGLISH);
    }

}
