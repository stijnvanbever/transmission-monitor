package be.spider.transmission.transmissionmonitor.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Component
public class MessageSender {
    private static final String TOPIC = "torrents";
    private final Producer<String, Object> producer;

    public MessageSender(@Value("${kafka.ip}") String kafkaIp,
                         @Value("${kafka.port}") String kafkaPort) {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaIp + ":" + kafkaPort);

        this.producer = new KafkaProducer<>(props, new StringSerializer(), new JsonSerializer());
    }

    public void produceMessage(Object message) {
        producer.send(new ProducerRecord<>(TOPIC, null, message));
    }

    private class JsonSerializer implements Serializer<Object> {
        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {

        }

        @Override
        public byte[] serialize(String topic, Object data) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writer().writeValueAsString(data).getBytes();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() {

        }
    }
}
