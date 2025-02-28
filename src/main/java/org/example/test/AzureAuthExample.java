package org.example.test;

import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.InteractiveBrowserCredential;
import com.azure.identity.InteractiveBrowserCredentialBuilder;
import com.microsoft.graph.models.MessageCollectionResponse;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

public class AzureAuthExample {
    public static void main(String[] args) {
        // Build the InteractiveBrowserCredential
        InteractiveBrowserCredential credential = new InteractiveBrowserCredentialBuilder()
                .redirectUrl("http://localhost:8080")  // Ensure this matches your registered app redirect URI
                .build();

        // Define the required scope
        String scope = "https://management.azure.com/.default"; // Replace with the required API scope

        // Request the token
        Mono<String> tokenMono = credential.getToken(new TokenRequestContext()
                        .setScopes(Collections.singletonList(scope)))
                .map(accessToken -> accessToken.getToken());

        // Retrieve and use the token
        tokenMono.subscribe(token -> {
            System.out.println("Access Token: " + token);

            GraphServiceClient graphServiceClient = new GraphServiceClient(credential);
            MessageCollectionResponse result = graphServiceClient.me().messages().get(requestConfiguration -> {
                requestConfiguration.queryParameters.select = new String []{"sender", "subject"};
            });

            result.getValue()
                    .stream()
                    .forEach(m -> {
                        System.out.println(m.getSubject());
                    });
            // Use this token to make authenticated API calls
        });

        // Keep the program alive for async token retrieval
        try {
            Thread.sleep(50000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
