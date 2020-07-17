/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkmicroservices.ri.spring.telemetry.model;

import java.time.ZonedDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author cwoodward
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "client-telemetry-event")

public class ClientTelemetryEvent {

    @Id
    private @NonNull  String id;
    private String source;
    private ZonedDateTime timestamp;
    private String accountId;
    private String level;
    private String message;
    private String[] details;
}
