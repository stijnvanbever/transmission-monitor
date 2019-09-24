package be.spider.transmission.transmissionmonitor.restclient.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

public class TransmissionRequestArguments {
    private List<String> fields;


    @JsonCreator
    public static TransmissionRequestArguments forFields(@JsonProperty("fields") String... fields) {
        TransmissionRequestArguments arguments = new TransmissionRequestArguments();
        arguments.fields = Arrays.asList(fields);
        return arguments;
    }

    public List<String> getFields() {
        return fields;
    }
}
