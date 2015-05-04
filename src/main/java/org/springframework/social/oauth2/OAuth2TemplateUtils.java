package org.springframework.social.oauth2;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by mavarazy on 5/4/15.
 */
public class OAuth2TemplateUtils {

    public static void increaseTimeout(OAuth2Template auth2Template, int timeout) {
        RestTemplate restTemplate = auth2Template.getRestTemplate();
        HttpComponentsClientHttpRequestFactory httpRequestFactory = (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
        httpRequestFactory.setReadTimeout(timeout);
        httpRequestFactory.setConnectTimeout(timeout);
    }
}
