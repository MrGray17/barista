package com.barista.application.worker;

import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class HandlerRegistry {

    private final Map<String, JobHandler> entries;

    public HandlerRegistry(List<JobHandler> handlers) {

        Objects.requireNonNull(handlers, "Handler list cannot be null");

        Map<String, JobHandler> registry = new HashMap<>();

        for (JobHandler handler : handlers) {

            Objects.requireNonNull(handler, "Handler cannot be null");

            String type = handler.supportedType();

            if (type == null || type.isBlank()) {
                throw new IllegalArgumentException(
                        "Handler type cannot be null or blank");
            }

            if (registry.containsKey(type)) {
                throw new IllegalArgumentException(
                        "Handler already registered for type: " + type);
            }

            registry.put(type, handler);
        }

        this.entries = Map.copyOf(registry);
    }

    public Optional<JobHandler> findHandler(String type) {

        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException(
                    "Job type cannot be null or blank"
                );
        }

        return Optional.ofNullable(entries.get(type));

    }
}