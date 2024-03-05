package ru.dverkask.api.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Optic Device Api",
                description = "Optic Device", version = "1.0",
                contact = @Contact(
        name = "DverkaSK",
        email = "dverkask@gmail.com"
)
        )
)
public class SwaggerConfig {

}
