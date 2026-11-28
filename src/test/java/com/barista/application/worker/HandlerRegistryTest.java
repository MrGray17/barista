package com.barista.application.worker;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HandlerRegistryTest {

    @Test
    void shouldReturnRegisteredHandler() {

        // Arrange
        JobHandler handler = mock(JobHandler.class);

        when(handler.supportedType()).thenReturn("email.send");

        HandlerRegistry registry = new HandlerRegistry(List.of(handler));

        // Act
        Optional<JobHandler> result = registry.findHandler("email.send");

        // Assert
        assertTrue(result.isPresent());
        assertSame(handler, result.get());
    }

    @Test
    void shouldReturnEmptyWhenHandlerNotFound() {

        // Arrange
        JobHandler handler = mock(JobHandler.class);
        when(handler.supportedType()).thenReturn("email.send");

        HandlerRegistry registry = new HandlerRegistry(List.of(handler));

        // Act
        Optional<JobHandler> result = registry.findHandler("image.resize");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldRejectDuplicateHandlerTypes() {

        // Arrange
        JobHandler handlerA = mock(JobHandler.class);
        JobHandler handlerB = mock(JobHandler.class);

        when(handlerA.supportedType()).thenReturn("email.send");
        when(handlerB.supportedType()).thenReturn("email.send");

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> new HandlerRegistry(List.of(handlerA, handlerB)));
    }
}