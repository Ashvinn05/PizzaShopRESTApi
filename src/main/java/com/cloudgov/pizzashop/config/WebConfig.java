/**
 * Configuration class for Spring WebFlux settings.
 * This class configures resource handling for static files and web resources.
 */
package com.cloudgov.pizzashop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Web configuration class that implements WebFluxConfigurer to customize
 * Spring WebFlux settings.
 *
 * This class configures resource handlers for serving static content from
 * multiple locations in the classpath.
 */
@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {
    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);

    /**
     * Configures resource handlers for serving static content.
     *
     * @param registry The ResourceHandlerRegistry to configure
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("[START] addResourceHandlers - Configuring resource handlers");
        
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/META-INF/resources/")
            .addResourceLocations("classpath:/resources/")
            .addResourceLocations("classpath:/static/")
            .addResourceLocations("classpath:/public/");

        log.info("[END] addResourceHandlers - Successfully configured resource handlers");
    }
}