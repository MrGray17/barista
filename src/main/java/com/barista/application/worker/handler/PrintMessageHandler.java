package com.barista.application.worker.handler;

import com.barista.application.worker.JobHandler;
import com.barista.job.Job;

public class PrintMessageHandler implements JobHandler {

    @Override
    public String supportedType() {
        return "message.print";
    }

    @Override
    public void execute(Job job) {
        Object message = job.getPayload().get("message");
        if (!(message instanceof String)) {
            throw new IllegalArgumentException(
                    "Payload field 'message' must be a String");
        }

        String value = (String) message;
        System.out.println(value);
    }
}
