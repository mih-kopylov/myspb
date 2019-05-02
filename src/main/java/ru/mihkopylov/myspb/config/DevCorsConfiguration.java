package ru.mihkopylov.myspb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.mihkopylov.myspb.Const;

@Configuration
@Profile("dev")
public class DevCorsConfiguration {
    @Bean
    public WebMvcConfigurer devCorsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings( CorsRegistry registry ) {
                registry.addMapping( "/**" ).allowedHeaders( "*" ).exposedHeaders( Const.TOKEN ).allowedOrigins( "*" ).allowCredentials( true );
            }
        };
    }
}
