package be.spider.transmission.transmissionmonitor.restclient.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TransmissionResponseArguments {
    private final List<TransmissionResponseTorrent> torrents;

    @JsonCreator
    public TransmissionResponseArguments(@JsonProperty("torrents") List<TransmissionResponseTorrent> torrents) {
        this.torrents = torrents;
    }

    public List<TransmissionResponseTorrent> getTorrents() {
        return torrents;
    }
}
