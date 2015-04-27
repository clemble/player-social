package com.clemble.casino.server.social.facebook;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookAdapter;

/**
 * Created by mavarazy on 4/25/15.
 */
public class AlternativeFacebookConnectionFactory extends OAuth2ConnectionFactory<Facebook> {

    public AlternativeFacebookConnectionFactory(String appId, String appSecret) {
        this(appId, appSecret, null);
    }

    public AlternativeFacebookConnectionFactory(String appId, String appSecret, String appNamespace) {
        super("facebook", new AlternativeFacebookServiceProvider(appId, appSecret, appNamespace), new FacebookAdapter());
    }

}