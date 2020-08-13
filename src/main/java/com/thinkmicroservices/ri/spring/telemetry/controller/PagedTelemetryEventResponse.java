 
package com.thinkmicroservices.ri.spring.telemetry.controller;
 
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *
 * @author cwoodward
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class PagedTelemetryEventResponse<T> {
     private boolean resultsAvailable=false;
    private List<T> resultList;
    private long totalElements;
    private long totalPages;
    private long currentPage;
    private long currentPageElementNumber;
  
}
