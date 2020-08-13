 
package com.thinkmicroservices.ri.spring.telemetry.model;

import java.util.Date;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author cwoodward
 */
@Slf4j
@Data
public class ClientTelemetryEventRequest {
   private Date timestamp;
   private String source;
   private String accountId;
   private String level;
   private String message;
   private String[] details;
}
