package br.gov.agu.samir.new_samir_back.infra.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
