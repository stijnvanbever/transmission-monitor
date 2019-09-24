package be.spider.transmission.transmissionmonitor.transmissionpolling;

import be.spider.transmission.transmissionmonitor.kafka.MessageSender;
import be.spider.transmission.transmissionmonitor.flux.FluxProvider;
import be.spider.transmission.transmissionmonitor.restclient.TransmissionRestClient;
import be.spider.transmission.transmissionmonitor.restclient.model.TransmissionResponse;
import be.spider.transmission.transmissionmonitor.restclient.model.TransmissionResponseTorrent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.LongStream;

@Component
public class TransmissionMonitor {
    private static final Logger log = LoggerFactory.getLogger(TransmissionMonitor.class);
    private final FluxProvider fluxProvider;
    private final TransmissionRestClient transmissionRestClient;
    private final MessageSender messageSender;

    @Autowired
    public TransmissionMonitor(FluxProvider fluxProvider, TransmissionRestClient transmissionRestClient,
                               MessageSender messageSender) {
        this.fluxProvider = fluxProvider;
        this.transmissionRestClient = transmissionRestClient;
        this.messageSender = messageSender;
    }

    public void startMonitoring() {
        fluxProvider.getStreamPerSecond(1L).subscribe(
                i -> transmissionRestClient.obtainTorrentsStatus().subscribe(this::handleResponse));
    }

    private void handleResponse(TransmissionResponse transmissionResponse) {
        logOverview(transmissionResponse);
        messageSender.produceMessage(transmissionResponse);
    }

    private void logOverview(TransmissionResponse transmissionResponse) {
        List<TransmissionResponseTorrent> torrents = transmissionResponse.getArguments().getTorrents();
        Long totalDownloadSpeed = torrents.stream().flatMapToLong(
                transmissionResponseTorrent -> LongStream.of(transmissionResponseTorrent.getRateDownload())).sum();
        Long activeTorrents = torrents.stream().filter(torrent -> torrent.getRateDownload() > 0L).count();
        String overview = String.format("Response: [%s], Torrents: [%d], ActiveTorrents: [%d], Downloadspeed: [%d]",
                transmissionResponse.getResult(), torrents.size(), activeTorrents, totalDownloadSpeed);
        log.info(overview);
    }
}
