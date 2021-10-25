package project.vhp.dolarapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfiguration implements WebMvcConfigurer {

    @Value("${origem-permitida}")
    private String origemPermitida;

    private final Logger logger = LoggerFactory.getLogger(CorsConfiguration.class);

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.info("Origem permitida: {}", origemPermitida);
        registry.addMapping("/**")
                .allowedOrigins(origemPermitida)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
    }
}
