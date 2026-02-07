package com.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "My Service API",
                description = """
            My Service 백엔드 API 문서입니다.

            - 내부 / 외부 공용
            - 인증 방식: JWT Bearer
        """,
                version = "v1.0.0",
                contact = @Contact(
                        name = "Backend Team",
                        email = "backend@company.com"
                )
        )
)
public class SwaggerConfig {

}
