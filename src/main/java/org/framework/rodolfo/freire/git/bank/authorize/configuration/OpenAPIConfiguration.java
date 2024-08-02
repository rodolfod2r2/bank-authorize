package org.framework.rodolfo.freire.git.bank.authorize.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * This class configures and returns a custom {@link OpenAPI} instance for integration with Swagger.
 * It uses the settings defined in {@link PropertiesBankAuthorize} to populate the necessary information
 * for API documentation and the associated server.
 * <p>
 * The configuration ensures that the Swagger documentation is populated with details such as the API's
 * title, description, version, contact information, license, terms of service, and server details.
 * </p>
 * <p>
 * This setup is crucial for providing a comprehensive and user-friendly API documentation interface
 * that integrates seamlessly with Swagger's UI and other tools.
 * </p>
 *
 * @see PropertiesBankAuthorize
 */
@Configuration
@OpenAPIDefinition
public class OpenAPIConfiguration {

    private final PropertiesBankAuthorize propertiesBankAuthorize;

    /**
     * Constructor that injects the configuration properties defined in {@link PropertiesBankAuthorize}.
     *
     * @param propertiesBankAuthorize Configuration properties for integration with Swagger.
     */
    public OpenAPIConfiguration(PropertiesBankAuthorize propertiesBankAuthorize) {
        this.propertiesBankAuthorize = propertiesBankAuthorize;
    }

    /**
     * Method responsible for creating and configuring a custom {@link OpenAPI} instance.
     * <p>
     * The {@code OpenAPI} instance is configured with information such as the API's title, description,
     * version, contact details, license information, terms of service URL, and server information.
     * </p>
     *
     * @return {@link OpenAPI} instance configured with the information defined in {@link PropertiesBankAuthorize}.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(propertiesBankAuthorize.getDocInfoTitle())
                        .description(propertiesBankAuthorize.getDocInfoDescription())
                        .version(propertiesBankAuthorize.getDocInfoVersion())
                        .contact(new Contact()
                                .name(propertiesBankAuthorize.getDocInfoContactName())
                                .email(propertiesBankAuthorize.getDocInfoContactEmail())
                                .url(propertiesBankAuthorize.getDocInfoContactUrl())
                        )
                        .license(new License()
                                .name(propertiesBankAuthorize.getDocInfoLicense())
                                .url(propertiesBankAuthorize.getDocInfoLicenseUrl())
                        )
                        .termsOfService(propertiesBankAuthorize.getDocInfoTermsOfServiceUrl())
                )
                .servers(Collections.singletonList(
                        new Server()
                                .url(propertiesBankAuthorize.getDocServerUrl())
                                .description(propertiesBankAuthorize.getDocServerDescription())
                ));
    }
}
