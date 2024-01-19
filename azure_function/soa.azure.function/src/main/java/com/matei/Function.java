package com.matei;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.net.http.HttpHeaders;
import java.util.Optional;

import javax.management.RuntimeErrorException;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it
     * using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET,
                    HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        final String query = request.getQueryParameters().get("name");
        final String name = request.getBody().orElse(query);

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }
    }

    @FunctionName("extractUsername")
    public HttpResponseMessage extractUsername(
            @HttpTrigger(name = "extractUsername", methods = {
                    HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a extractUsername request.");

        try {
            // Parse query parameter
            final String authHeader = request.getHeaders().get("Authorization");
            if (authHeader == null) {
                throw new RuntimeException("No auth header");
            }

            final String username = JwtUtil.extractUsername(authHeader);
            if (username == null) {
                throw new RuntimeException("Username was null");
            }

            return request.createResponseBuilder(HttpStatus.OK)
                    .body(new Response(username))
                    .header("Content-Type", "application/json")
                    .build();
        } catch (RuntimeException ex) {
            context.getLogger().throwing("Function", "extractUsername", ex);
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Error").build();
        }
    }

    @FunctionName("encodeUsername")
    public HttpResponseMessage encodeUsername(
            @HttpTrigger(name = "encodeUsername", methods = {
                    HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a encodeUsername request.");

        try {
            final String username = request.getQueryParameters().get("username");
            if (username == null) {
                throw new RuntimeException("No username");
            }

            final String authToken = JwtUtil.generateToken(username);
            if (authToken == null) {
                throw new RuntimeException("Auth token was null");
            }

            return request.createResponseBuilder(HttpStatus.OK)
                    .body(new Response(authToken))
                    .header("Content-Type", "application/json")
                    .build();
        } catch (RuntimeException ex) {
            context.getLogger().throwing("Function", "encodeUsername", ex);
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Error").build();
        }
    }
}
