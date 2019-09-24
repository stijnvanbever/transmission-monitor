package be.spider.transmission.transmissionmonitor.restclient.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransmissionResponse {
    private final TransmissionResponseArguments arguments;
    private final String result;

    @JsonCreator
    public TransmissionResponse(@JsonProperty("arguments") TransmissionResponseArguments arguments,
                                @JsonProperty("result") String result) {
        this.arguments = arguments;
        this.result = result;
    }

    public TransmissionResponseArguments getArguments() {
        return arguments;
    }

    public String getResult() {
        return result;
    }
}
