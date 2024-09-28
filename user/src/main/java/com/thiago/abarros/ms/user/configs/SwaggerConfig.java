
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {
  
  @Bean
  public OpenAPI userMsOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("User MS API")
            .description("User MS API")
            .version("v0.0.1")
            .license(new License().name("Apache 2.0").url("http://springdoc.org")));
  }
}