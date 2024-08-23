package com.anastasia.smart.exceptions;

import java.util.List;

public class EventStreamException extends RuntimeException {


    public EventStreamException(List<?> errorsList) {
        super(errorsList.toString());
    }
}
