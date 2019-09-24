package be.spider.transmission.transmissionmonitor;

import be.spider.transmission.transmissionmonitor.transmissionpolling.TransmissionMonitor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TransmissionMonitorApplication {

    public static void main(String[] args)  {
        SpringApplication.run(TransmissionMonitorApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(TransmissionMonitor transmissionMonitor) {
        return args -> transmissionMonitor.startMonitoring();
    }
}
