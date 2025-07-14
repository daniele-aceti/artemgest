package artemgest.artemgest.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("api")  // Nome del gruppo nel Swagger UI
                .packagesToScan("artemgest.artemgest.controller.api")
                .pathsToMatch("/api/**")
                .build();
    }
}
