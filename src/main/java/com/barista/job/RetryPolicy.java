package com.barista.job;

import java.time.Duration;
import java.util.Objects;

public class RetryPolicy {
    private final Duration initialDelay;
    private final Duration maxDelay;

    public RetryPolicy(Duration initialDelay, Duration maxDelay) {
        Objects.requireNonNull(initialDelay);
        Objects.requireNonNull(maxDelay);

        if (initialDelay.isNegative() || initialDelay.isZero()) {
            throw new IllegalArgumentException(
                    "Initial delay must be positive");
        }

        if (initialDelay.compareTo(maxDelay) > 0) {
            throw new IllegalArgumentException(
                    "Maximum delay cannot be smaller than the initial one");
        }

        this.initialDelay = initialDelay ;
        this.maxDelay = maxDelay ;
    }

    public Duration calculateDelay(int attempts) {
        if (attempts < 1) {
            throw new IllegalArgumentException(
                    "Attempts must be at least 1");
        }
        Duration delay = initialDelay;
        for (int i = 1; i < attempts; i++) {
            if (delay.compareTo(maxDelay.minus(delay)) >= 0) {
                // compare delay to (maxDelay - delay) instead of
                // (2*delay) to maxDelay to avoid overflow
                return maxDelay;
            }

            delay = delay.multipliedBy(2);
        }
        return delay;
    }
}
