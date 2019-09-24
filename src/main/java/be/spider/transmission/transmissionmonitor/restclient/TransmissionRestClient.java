package be.spider.transmission.transmissionmonitor.restclient;

import be.spider.transmission.transmissionmonitor.restclient.model.TransmissionRequest;
import be.spider.transmission.transmissionmonitor.restclient.model.TransmissionRequestArguments;
import be.spider.transmission.transmissionmonitor.restclient.model.TransmissionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class TransmissionRestClient {
    private static final Logger log = LoggerFactory.getLogger(TransmissionRestClient.class);
    private final WebClient webClient;

    @Autowired
    public TransmissionRestClient(WebClient.Builder webClientBuilder,
                                  @Value("${transmission.user}") String transmissionUser,
                                  @Value("${transmission.password}") String transmissionPassword,
                                  @Value("${transmission.ip}") String transmissionIp,
                                  @Value("${transmission.port}") String transmissionPort) {
        String transmissionUrl = "http://" + transmissionIp + ":" + transmissionPort;
        String encodedCredentials = "Basic " + Base64Utils.encodeToString(
                (transmissionUser + ":" + transmissionPassword).getBytes(StandardCharsets.UTF_8)
        );

        WebClient initWebClient = webClientBuilder.baseUrl(transmissionUrl)
                .defaultHeader("Authorization", encodedCredentials).build();

        String sessionId = obtainSessionId(initWebClient).block();
        this.webClient = webClientBuilder.baseUrl(transmissionUrl)
                .defaultHeader("Authorization", encodedCredentials)
                .defaultHeader("X-Transmission-Session-Id", sessionId)
//                .filter(logRequest())
                .build();
    }

    public Mono<TransmissionResponse> obtainTorrentsStatus() {
        TransmissionRequest transmissionRequest = new TransmissionRequest(
                TransmissionRequestArguments.forFields("id", "name", "totalSize", "rateDownload"),
                "torrent-get");

        return webClient.post().uri("transmission/rpc")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(transmissionRequest)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TransmissionResponse.class);
    }

    private Mono<String> obtainSessionId(WebClient webClient) {
        return webClient.get().uri("transmission/rpc")
                .exchange()
                .flatMap(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.CONFLICT)) {
                        return Mono.just(clientResponse.headers().header("X-Transmission-Session-Id").get(0));
                    }
                    return Mono.just("");
                });
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            log.info("Body: {}", clientRequest.body());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }
}
