package com.javatechie.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OutBoxMessageConsumer {


    @KafkaListener(topics = "unprocessed-order-events",groupId = "jt-group")
    public void consume(String payload){
        log.info("Event consumed {} ",payload);
    }
}
