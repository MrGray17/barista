package com.barista.application.worker;

import com.barista.job.Job;

public interface JobHandler {

    String supportedType();

    void execute(Job job);

}