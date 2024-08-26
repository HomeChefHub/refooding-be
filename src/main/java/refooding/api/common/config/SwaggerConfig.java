package refooding.api.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${SWAGGER_LOCAL_URL}")
    private String localUrl;
    @Value("${SWAGGER:qq_URL}")
    private String url;


    @Bean
    public OpenAPI openAPI() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url(localUrl));
        servers.add(new Server().url(url));
        return new OpenAPI()
                .info(getApiInfo())
                .servers(servers);
    }

    private Info getApiInfo(){
        return new Info()
                .title("리푸딩 API 문서")
                .version("1.0.0");
    }
}