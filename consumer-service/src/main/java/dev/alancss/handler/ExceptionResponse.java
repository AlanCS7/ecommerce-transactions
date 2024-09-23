package dev.alancss.handler;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ExceptionResponse {

    private Integer status;
    private Instant timestamp;
    private String error;
}
