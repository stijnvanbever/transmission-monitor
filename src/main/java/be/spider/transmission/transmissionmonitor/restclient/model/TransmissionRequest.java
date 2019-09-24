package be.spider.transmission.transmissionmonitor.restclient.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransmissionRequest {
    private final TransmissionRequestArguments arguments;
    private final String method;


    @JsonCreator
    public TransmissionRequest(@JsonProperty("arguments") TransmissionRequestArguments arguments,
                               @JsonProperty("method") String method) {
        this.arguments = arguments;
        this.method = method;
    }

    public TransmissionRequestArguments getArguments() {
        return arguments;
    }

    public String getMethod() {
        return method;
    }
}

