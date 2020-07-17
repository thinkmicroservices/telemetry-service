/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkmicroservices.ri.spring.telemetry.service;

import com.thinkmicroservices.ri.spring.telemetry.model.ClientTelemetryEvent;
import com.thinkmicroservices.ri.spring.telemetry.repository.ClientTelemetryEventRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author cwoodward
 */
@Service
@Slf4j
public class ClientTelemetryEventService {

    @Autowired
    private ClientTelemetryEventRepository clientTelemetryEventRepository;

    @Autowired
    private MeterRegistry meterRegistry;

    private Counter telemetryEventCreatedCounter;
    private Counter telemetryQueryByIdCounter;
    private Counter telemetryDeleteCounter;
    private Counter telemetryQueryByAccountCounter;

    /**
     *
     * @param event
     * @return
     */
    public ClientTelemetryEvent create(ClientTelemetryEvent event) {
        log.debug("create {}", event);

        clientTelemetryEventRepository.save(event);
        telemetryEventCreatedCounter.increment();
        return event;
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<ClientTelemetryEvent> find(String id) {
        log.debug("find {}", id);
        telemetryQueryByIdCounter.increment();
        return clientTelemetryEventRepository.findById(id);
    }

    /**
     *
     * @param id
     */
    public void delete(String id) {
        log.debug("delete {}", id);
        clientTelemetryEventRepository.deleteById(id);
        telemetryDeleteCounter.increment();
    }

    /**
     *
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param start
     * @param end
     * @param level
     * @param accountId
     * @return
     */
    public Page<ClientTelemetryEvent> findByAccountId(Integer pageNo,
            Integer pageSize,
            String sortBy,
            ZonedDateTime start,
            ZonedDateTime end,
            List<String> level,
            String accountId) {
        log.debug("find by accountId ");
        if (sortBy == null) {
            sortBy = "accountId";
        }
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<ClientTelemetryEvent> pagedResult = clientTelemetryEventRepository.findByLevelInAndAccountIdAndTimestampBetween(paging, level, accountId, start, end);

        telemetryQueryByAccountCounter.increment();
        return pagedResult;
    }

    @PostConstruct
    private void initializeMetrics() {

        telemetryEventCreatedCounter = Counter.builder("telemetry.event.created")
                .description("The number of telemetry events created.")
                .register(meterRegistry);

        telemetryQueryByIdCounter = Counter.builder("telemetry.query.id")
                .description("The number of telemetry queries by id.")
                .register(meterRegistry);

        telemetryDeleteCounter = Counter.builder("telemetry.delete")
                .description("The number of telemetry delete calls.")
                .register(meterRegistry);
        telemetryQueryByAccountCounter = Counter.builder("telemetry.query.account")
                .description("The number of telemetry queries by account id.")
                .register(meterRegistry);
    }
}
