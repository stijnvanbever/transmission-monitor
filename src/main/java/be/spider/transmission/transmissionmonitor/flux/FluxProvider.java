package be.spider.transmission.transmissionmonitor.flux;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
public class FluxProvider {
    public Flux<Long> getStreamPerSecond(Long timeFrame) {
        return Flux.interval(Duration.ofSeconds(timeFrame));
    }
}
