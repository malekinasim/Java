package com.common.service.client;

 public class AuthProviderDto {
    public String registrationId;
    public String authorizationUri;
    public String tokenUri;
    public String scopes;
    public boolean enabled;
    public String type;

     public void setRegistrationId(String registrationId) {
         this.registrationId = registrationId;
     }

     public void setAuthorizationUri(String authorizationUri) {
         this.authorizationUri = authorizationUri;
     }

     public void setTokenUri(String tokenUri) {
         this.tokenUri = tokenUri;
     }

     public void setScopes(String scopes) {
         this.scopes = scopes;
     }

     public void setEnabled(boolean enabled) {
         this.enabled = enabled;
     }

     public void setType(String type) {
         this.type = type;
     }

     public String getRegistrationId() {
         return registrationId;
     }

     public String getAuthorizationUri() {
         return authorizationUri;
     }

     public String getTokenUri() {
         return tokenUri;
     }

     public String getScopes() {
         return scopes;
     }

     public boolean isEnabled() {
         return enabled;
     }

     public String getType() {
         return type;
     }

     public enum ProviderType{
        OIDC,JWT;
    }
}