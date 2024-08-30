package br.gov.agu.samir.new_samir_back.infra.exceptions;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StandardError {

    private Instant timestamp;

    private Integer status;

    private String error;

    private String message;

    private String path;
}
