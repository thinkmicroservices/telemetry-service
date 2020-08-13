
package com.thinkmicroservices.ri.spring.telemetry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author cwoodward
 */
@Profile(value = {"swagger"})
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    private Logger logger = LoggerFactory.getLogger(SwaggerConfiguration.class);
    @Bean
    public Docket productApi() {
        logger.info("create swagger2 docket");
       
            
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.thinkmicroservices.ri.spring.telemetry.controller"))
                    .build().apiInfo(apiEndpointInfo());
       

    }

    private ApiInfo apiEndpointInfo() {
        logger.info("create apiEndpointInfo");
        return new ApiInfoBuilder().title("Telemetry REST API")
                .description("provides access to telemetry facilities")
               
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }
}
