/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
