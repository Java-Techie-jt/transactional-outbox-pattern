package com.javatechie.service;

import com.javatechie.entity.Outbox;
import com.javatechie.publisher.MessagePublisher;
import com.javatechie.repository.OutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
@Slf4j
public class OrderPollerService {

    @Autowired
    private OutboxRepository repository;

    @Autowired
    private MessagePublisher messagePublisher;


    @Scheduled(fixedRate = 60000)
    public void pollOutboxMessagesAndPublish() {

        //1. fetch unprocessed record
        List<Outbox> unprocessedRecords = repository.findByProcessedFalse();

        log.info("unprocessed record count : {}", unprocessedRecords.size());

        //2. publish record to kafka/queue

        unprocessedRecords.forEach(outbox -> {
            try {
                messagePublisher.publish(outbox.getPayload());
                //update the message status to processed=true to avoid duplicate message processing
                outbox.setProcessed(true);
                repository.save(outbox);

            } catch (Exception ignored) {
                log.error(ignored.getMessage());
            }
        });


    }
}
