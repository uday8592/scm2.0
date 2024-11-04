package com.scm.helpers;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class Helper {

    @Value("${server.baseUrl}")
    private String baseUrl;

    public String getEmailOfLoggedInUser(Authentication authentication) {
        // Check if the user is authenticated via OAuth2
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String clientId = oauthToken.getAuthorizedClientRegistrationId();
            OAuth2User oauth2User = oauthToken.getPrincipal();
            String email = "";

            if ("google".equalsIgnoreCase(clientId)) {
                System.out.println("Getting email from Google");
                email = oauth2User.getAttribute("email");
            } else if ("github".equalsIgnoreCase(clientId)) {
                System.out.println("Getting email from GitHub");
                email = oauth2User.getAttribute("email") != null
                        ? oauth2User.getAttribute("email")
                        : oauth2User.getAttribute("login") + "@gmail.com";
            } else {
                System.out.println("Unknown OAuth provider: " + clientId);
            }

            return email;
        } else {
            System.out.println("Getting data from local database");
            return authentication.getName();
        }
    }

    public String getLinkForEmailVerification(String emailToken) {
        return this.baseUrl + "/auth/verify-email?token=" + emailToken;
    }
}
