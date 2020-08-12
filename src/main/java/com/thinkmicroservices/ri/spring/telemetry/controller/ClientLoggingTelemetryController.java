package com.thinkmicroservices.ri.spring.telemetry.controller;

import com.thinkmicroservices.ri.spring.telemetry.model.ClientTelemetryEvent;
import com.thinkmicroservices.ri.spring.telemetry.model.ClientTelemetryEventRequest;
import com.thinkmicroservices.ri.spring.telemetry.service.ClientTelemetryEventService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author cwoodward
 */
@RestController

@Slf4j
public class ClientLoggingTelemetryController {

    @Value("${client.logging.level:INFO}")
    private String loggingLevel;

    @Autowired
    private ClientTelemetryEventService clientTelemetryEventService;

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/level/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "returns the current telemetry level " )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                  required = true, dataType = "string", paramType = "header" ,defaultValue="bearer <token-value>")})
 
    public ResponseEntity<ClientTelemetryLevel> getLevel(String id) throws Exception {

        return ResponseEntity.ok(ClientTelemetryLevel.builder()
                .level(loggingLevel)
                .build()
        );
    }

    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/log", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "logs a telemetry event " )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                  required = true, dataType = "string", paramType = "header" ,defaultValue="bearer <token-value>")})
 
    public ResponseEntity receiveEvent(@RequestBody ClientTelemetryEventRequest request) throws Exception {

        log.debug("telemetry,{}", request);
        ClientTelemetryEvent newEvent = ClientTelemetryEvent.builder()
                .id(UUID.randomUUID().toString())
                .source(request.getSource())
                .level(request.getLevel())
                .message(request.getMessage())
                .accountId(request.getAccountId())
                .timestamp(ZonedDateTime.ofInstant(request.getTimestamp().toInstant(), ZoneId.systemDefault()))
                .details(request.getDetails())
                .build();

        clientTelemetryEventService.create(newEvent);

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    
    @ResponseBody
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                required = true, dataType = "string", paramType = "header" ,defaultValue="bearer <token-value>")})
    @ApiOperation(value = "get a telemetry event by its id " )
    @RequestMapping(value = "event/{id}", method = RequestMethod.POST)
    public ResponseEntity<ClientTelemetryEvent> find(@PathVariable("id") String id) throws Exception {
        Optional<ClientTelemetryEvent> event = clientTelemetryEventService.find(id);
        if (event.isPresent()) {
            return new ResponseEntity<>(event.get(), HttpStatus.OK);
            
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    /**
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping(path = "/findByAccountId")
    @ResponseBody
    @ApiOperation(value = "retrieve the telemetry events for a given account id " )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                  required = true, dataType = "string", paramType = "header" ,defaultValue="bearer <token-value>")})
 
    public ResponseEntity<PagedTelemetryEventResponse> findByAccountId(@RequestBody PagedTelemetryEventRequest request, HttpServletRequest httpServletRequest) {

        if (request.getStartDate() == null) {
            log.debug("applying default start date");
            request.setStartDate(ZonedDateTime.ofInstant((new Date(0)).toInstant(), ZoneId.systemDefault())); // set the start to the epoch date
        }
        // set a default end date if not present
        if (request.getEndDate() == null) {
            log.debug("applying default end date");
            request.setEndDate(ZonedDateTime.now()); // set the end to the current date
        }

        Page<ClientTelemetryEvent> pagedResult = this.clientTelemetryEventService.findByAccountId(request.getPage(),
                request.getPageSize(),
                request.getSortBy(),
                request.getStartDate(),
                request.getEndDate(),
                request.getLevel(),
                request.getAccountId());

        PagedTelemetryEventResponse<ClientTelemetryEvent> pagedResultResponse = new PagedTelemetryEventResponse<ClientTelemetryEvent>();

        if (pagedResult.hasContent()) {
            pagedResultResponse.setResultsAvailable(true);
            pagedResultResponse.setResultList(pagedResult.getContent());
            pagedResultResponse.setTotalPages(pagedResult.getTotalPages());
            pagedResultResponse.setTotalElements(pagedResult.getTotalElements());
            pagedResultResponse.setCurrentPage(pagedResult.getNumber());
            pagedResultResponse.setCurrentPageElementNumber(pagedResult.getNumberOfElements());
        } else {
            pagedResultResponse.setResultList(new ArrayList<ClientTelemetryEvent>());
        }

        return ResponseEntity.ok(pagedResultResponse);
    }
}
