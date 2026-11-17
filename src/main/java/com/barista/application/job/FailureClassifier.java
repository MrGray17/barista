package com.barista.application.job;

public interface FailureClassifier {

    boolean isRetryable(Exception failure) ;

}