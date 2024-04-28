package com.stanislav.smart_analytics.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.stanislav.smart_analytics")
@PropertySource("classpath:application.properties")
public class SmartAnalyticsConfig {


}
