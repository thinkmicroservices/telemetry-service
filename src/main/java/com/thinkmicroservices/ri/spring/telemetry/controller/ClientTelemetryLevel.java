 
package com.thinkmicroservices.ri.spring.telemetry.controller;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author cwoodward
 */
@Slf4j
@Builder
@Data
public class ClientTelemetryLevel {
    
    private String level;

     
}
