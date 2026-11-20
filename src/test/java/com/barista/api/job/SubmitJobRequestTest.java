
package com.barista.api.job;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

class SubmitJobRequestTest {

    @Test
    void rejectsBlankJobType() {
        SubmitJobRequest request = new SubmitJobRequest("   ", Map.of());

        Set<ConstraintViolation<SubmitJobRequest>> violations = validate(request);

        assertEquals(1, violations.size());
        assertEquals(
                "type",
                violations.iterator().next()
                        .getPropertyPath().toString());
    }

    @Test
    void rejectsNullPayload() {
        SubmitJobRequest request = new SubmitJobRequest("email.send", null);

        Set<ConstraintViolation<SubmitJobRequest>> violations = validate(request);

        assertEquals(1, violations.size());
        assertEquals(
                "payload",
                violations.iterator().next()
                        .getPropertyPath().toString());
    }

    @Test
    void acceptsValidRequest() {
        SubmitJobRequest request = new SubmitJobRequest(
                "email.send",
                Map.of("recipient", "user@example.com"));

        Set<ConstraintViolation<SubmitJobRequest>> violations = validate(request);

        assertTrue(violations.isEmpty());
    }

    private Set<ConstraintViolation<SubmitJobRequest>> validate(
            SubmitJobRequest request) {

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {

            return factory.getValidator().validate(request);
        }
    }
}
