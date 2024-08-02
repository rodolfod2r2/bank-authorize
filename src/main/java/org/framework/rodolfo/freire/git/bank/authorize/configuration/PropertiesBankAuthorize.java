package org.framework.rodolfo.freire.git.bank.authorize.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * This class represents property settings for integration with Swagger
 * in the specific context of the BankAuthorize system.
 * <p>
 * It uses values injected via {@code @Value} annotations to define information about the documentation
 * and the server associated with Swagger. This configuration class allows for externalizing Swagger
 * configuration properties, making it easier to manage and customize the API documentation.
 * </p>
 * <p>
 * The properties are typically defined in a configuration file (e.g., {@code application.properties})
 * and are used to set up Swagger's documentation details and server information dynamically.
 * </p>
 *
 * @see <a href="https://swagger.io/">Swagger</a>
 * @see org.springframework.beans.factory.annotation.Value
 * @see org.springframework.context.annotation.Configuration
 */
@Primary
@Configuration
@Getter
@NoArgsConstructor
public class PropertiesBankAuthorize {

    /**
     * The title of the Swagger API documentation.
     * <p>
     * This value is injected from the property {@code bank-authorize-swagger.info.title}.
     * </p>
     */
    @Value("${bank-authorize-swagger.info.title}")
    private String docInfoTitle;

    /**
     * A brief description of the Swagger API documentation.
     * <p>
     * This value is injected from the property {@code bank-authorize-swagger.info.description}.
     * </p>
     */
    @Value("${bank-authorize-swagger.info.description}")
    private String docInfoDescription;

    /**
     * The version of the API as specified in the Swagger documentation.
     * <p>
     * This value is injected from the property {@code bank-authorize-swagger.info.version}.
     * </p>
     */
    @Value("${bank-authorize-swagger.info.version}")
    private String docInfoVersion;

    /**
     * The URL to the terms of service for the API.
     * <p>
     * This value is injected from the property {@code bank-authorize-swagger.info.termsOfServiceUrl}.
     * </p>
     */
    @Value("${bank-authorize-swagger.info.termsOfServiceUrl}")
    private String docInfoTermsOfServiceUrl;

    /**
     * The name of the contact person or organization for the API.
     * <p>
     * This value is injected from the property {@code bank-authorize-swagger.info.contact.name}.
     * </p>
     */
    @Value("${bank-authorize-swagger.info.contact.name}")
    private String docInfoContactName;

    /**
     * The URL for contacting the API support.
     * <p>
     * This value is injected from the property {@code bank-authorize-swagger.info.contact.url}.
     * </p>
     */
    @Value("${bank-authorize-swagger.info.contact.url}")
    private String docInfoContactUrl;

    /**
     * The email address for contacting the API support.
     * <p>
     * This value is injected from the property {@code bank-authorizes-wagger.info.contact.email}.
     * </p>
     */
    @Value("${bank-authorizes-wagger.info.contact.email}")
    private String docInfoContactEmail;

    /**
     * The license under which the API is provided.
     * <p>
     * This value is injected from the property {@code bank-authorize-swagger.info.license}.
     * </p>
     */
    @Value("${bank-authorize-swagger.info.license}")
    private String docInfoLicense;

    /**
     * The URL to the license information for the API.
     * <p>
     * This value is injected from the property {@code bank-authorize-swagger.info.licenseUrl}.
     * </p>
     */
    @Value("${bank-authorize-swagger.info.licenseUrl}")
    private String docInfoLicenseUrl;

    /**
     * The URL of the server where the API is hosted.
     * <p>
     * This value is injected from the property {@code bank-authorize-swagger.server.url}.
     * </p>
     */
    @Value("${bank-authorize-swagger.server.url}")
    private String docServerUrl;

    /**
     * A description of the server where the API is hosted.
     * <p>
     * This value is injected from the property {@code bank-authorize-swagger.server.description}.
     * </p>
     */
    @Value("${bank-authorize-swagger.server.description}")
    private String docServerDescription;

}
