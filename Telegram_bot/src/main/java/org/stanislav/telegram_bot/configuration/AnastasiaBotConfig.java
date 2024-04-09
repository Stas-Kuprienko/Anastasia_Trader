package org.stanislav.telegram_bot.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("org.stanislav.telegram_bot")
public class AnastasiaBotConfig {}
