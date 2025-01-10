package itu.auth.mg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Applique CORS à tous les chemins
                .allowedOrigins("*") // Autorise toutes les origines
                .allowedMethods("*") // Autorise toutes les méthodes HTTP (GET, POST, etc.)
                .allowedHeaders("*") // Autorise tous les en-têtes
                .allowCredentials(false); // Désactive l'utilisation des cookies
    }
}
