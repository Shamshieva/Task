package com.manas.dto.response;

import java.util.Date;

public record ErrorMessageToken (
    int statusCode,
    Date timestamp,
    String message,
    String description ) {

}
