package com.serenityword.mytask.controller;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.Map;

@RestController
public class LoginController {

    private final OAuth2AuthorizedClientService auth2AuthorizedClientService;

    public LoginController(OAuth2AuthorizedClientService auth2AuthorizedClientService) {
        this.auth2AuthorizedClientService = auth2AuthorizedClientService;
    }



    @RequestMapping(value = "/**")
    @RolesAllowed("USER")
    public String getUser(){
        return "Welcome, User";
    }
    @RequestMapping(value = "/admin")
    @RolesAllowed("ADMIN")
    public String getAdmin(){
        return "Welcome, Admin";
    }
    @RequestMapping(value = "/*")
    public String getUserInfo(Principal user){
        StringBuffer userinfo = new StringBuffer();

        if (user instanceof UsernamePasswordAuthenticationToken){
            userinfo.append(getUsernamePasswordLoginInfo(user));
        } else if (user instanceof OAuth2AuthenticationToken) {
            userinfo.append(getOauth2LoginInfo(user));
        }
        return userinfo.toString();
    }

    private StringBuffer getOauth2LoginInfo(Principal user){

        StringBuffer protectedInfo = new StringBuffer();
        OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
        OAuth2User principal = ((OAuth2AuthenticationToken)user).getPrincipal();


        OAuth2AuthorizedClient authClient = this.auth2AuthorizedClientService
                .loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(),authToken.getName());
        if(authToken.isAuthenticated()){

            Map<String,Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();

            String userToken = authClient.getAccessToken().getTokenValue();
            protectedInfo.append("Welcome, " + userAttributes.get("name")+"<br><br>");
            protectedInfo.append("e-mail: " + userAttributes.get("email")+"<br><br>");
            protectedInfo.append("Access Token: " + userToken+"<br><br>");

            OidcIdToken idToken = getIdToken(principal);

            if (idToken != null) {
                protectedInfo.append("idToken value: " + idToken.getTokenValue() + "<br><br>");

                Map<String, Object> claims = idToken.getClaims();

                for (String key : claims.keySet()){
                    protectedInfo.append("       " + key +":        " + claims.get(key) + "<br>");
                }
                return protectedInfo;
            }
        }
        else{
            protectedInfo.append("NA");
        }
        return protectedInfo;
    }

    private StringBuffer getUsernamePasswordLoginInfo(Principal user)
    {
        StringBuffer usernameInfo = new StringBuffer();

        UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) user);
        if(token.isAuthenticated()){
            User u = (User) token.getPrincipal();
            usernameInfo.append("Welcome, " + u.getUsername());
        } else{
            usernameInfo.append("NA");
        }
        return usernameInfo;
    }

    private OidcIdToken getIdToken(OAuth2User principal) {
        if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            return oidcUser.getIdToken();
        }
        return null;
    }

}
