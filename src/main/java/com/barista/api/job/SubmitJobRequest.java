package com.barista.api.job;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubmitJobRequest(

                @NotBlank(message = "Job type cannot be blank")
                String type,

                @NotNull(message = "Payload cannot be null")
                Map<String, Object> payload

) {
}