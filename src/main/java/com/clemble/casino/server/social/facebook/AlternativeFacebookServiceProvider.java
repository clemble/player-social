package com.clemble.casino.server.social.facebook;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;

/**
 * Created by mavarazy on 4/25/15.
 */
public class AlternativeFacebookServiceProvider  extends AbstractOAuth2ServiceProvider<Facebook> {
    private String appNamespace;

    public AlternativeFacebookServiceProvider(String appId, String appSecret, String appNamespace) {
        super(getOAuth2Template(appId, appSecret));
        this.appNamespace = appNamespace;
    }

    private static OAuth2Template getOAuth2Template(String appId, String appSecret) {
        OAuth2Template oAuth2Template = new OAuth2Template(appId, appSecret, "https://www.facebook.com/v2.3/dialog/oauth", "https://graph.facebook.com/v2.3/oauth/access_token") {

            protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
                return postForAccessGrant(accessTokenUrl, parameters, 5);
            }

            protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters, int retries) {
                try {
                    return super.postForAccessGrant(accessTokenUrl, parameters);
                } catch (Throwable throwable) {
                    if (retries > 0) {
                        return postForAccessGrant(accessTokenUrl, parameters, retries - 1);
                    } else {
                        throw throwable;
                    }
                }
            }

        };
        oAuth2Template.setUseParametersForClientAuthentication(true);
        return oAuth2Template;
    }

    public Facebook getApi(String accessToken) {
        return new FacebookTemplate(accessToken, this.appNamespace);
    }
}