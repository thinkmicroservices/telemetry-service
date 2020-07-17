/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkmicroservices.ri.spring.telemetry.repository;

import com.thinkmicroservices.ri.spring.telemetry.model.ClientTelemetryEvent;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author cwoodward
 */
public interface ClientTelemetryEventRepository extends MongoRepository<ClientTelemetryEvent, String> {

    Page<ClientTelemetryEvent> findByLevelInAndAccountIdAndTimestampBetween(Pageable paging, List<String> eventTypes, String accountId, ZonedDateTime startDate, ZonedDateTime endDate);

}
