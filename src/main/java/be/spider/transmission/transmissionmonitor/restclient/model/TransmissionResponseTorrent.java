package be.spider.transmission.transmissionmonitor.restclient.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransmissionResponseTorrent {
    private final Long id;
    private final String name;
    private final Long rateDownload;
    private final Long totalSize;

    @JsonCreator
    public TransmissionResponseTorrent(@JsonProperty("id") Long id,
                                       @JsonProperty("name") String name,
                                       @JsonProperty("rateDownload") Long rateDownload,
                                       @JsonProperty("totalSize") Long totalSize) {
        this.id = id;
        this.name = name;
        this.rateDownload = rateDownload;
        this.totalSize = totalSize;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getRateDownload() {
        return rateDownload;
    }

    public Long getTotalSize() {
        return totalSize;
    }
}
