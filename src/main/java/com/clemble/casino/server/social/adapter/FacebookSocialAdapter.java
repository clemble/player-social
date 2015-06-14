package com.clemble.casino.server.social.adapter;

import static com.clemble.casino.utils.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.PagingParameters;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import com.clemble.casino.player.PlayerGender;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.social.SocialAdapter;

public class FacebookSocialAdapter implements SocialAdapter<Facebook> {

    final private OAuth2ConnectionFactory<Facebook> facebookConnectionFactory;

    public FacebookSocialAdapter(OAuth2ConnectionFactory<Facebook> facebookConnectionFactory) {
        this.facebookConnectionFactory = checkNotNull(facebookConnectionFactory);
    }

    @Override
    public ConnectionFactory<Facebook> getConnectionFactory() {
        return facebookConnectionFactory;
    }

    private DateTime readDate(String facebookBirthDate) {
        if (!isNullOrEmpty(facebookBirthDate)) {
            String[] date = facebookBirthDate.split("/");
            if(date.length != 3) {
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Integer.valueOf(date[2]), 1 + Integer.valueOf(date[0]), Integer.valueOf(date[1]));
                return new DateTime(calendar.getTime());
            }
        }
        return null;
    }

    @Override
    public PlayerProfile fetchPlayerProfile(Facebook facebook) {
        // Step 1. Retrieving facebook profile for associated user
        User facebookProfile = facebook.userOperations().getUserProfile();
        // Step 1.1. Checking facebook timezone
        int hours = facebookProfile.getTimezone().intValue();
        int minutes = (int) Math.abs((facebookProfile.getTimezone().doubleValue() - hours) * 60);
        DateTimeZone timeZone = DateTimeZone.forOffsetHoursMinutes(hours, minutes);
        // Step 2. Generating appropriate GameProfile to return
        return new PlayerProfile()
                .addSocialConnection(toConnectionKey(facebookProfile.getId()))
                .setFirstName(facebookProfile.getFirstName())
                .setNickName(facebookProfile.getName())
                .setLastName(facebookProfile.getLastName())
                .setBirthDate(readDate(facebookProfile.getBirthday()))
                .setGender(PlayerGender.parse(facebookProfile.getGender()))
                .setTimezone(timeZone);
    }

    @Override
    public String getEmail(Facebook facebook) {
        return facebook.userOperations().getUserProfile().getEmail();
    }

    @Override
    public String toImageUrl(Connection<Facebook> connectionKey) {
        String primaryImage = "http://graph.facebook.com/" + connectionKey.getKey().getProviderUserId() + "/picture?redirect=1&height={height}&type=small&width={width}";
        return primaryImage;
    }

    @Override
    public Collection<ConnectionKey> fetchConnections(Facebook api) {
        Collection<ConnectionKey> connections = new ArrayList<>();
        // Step 1. Fetching all friend connections
        PagingParameters pagingParameters = null;
        PagedList<String> friends = api.friendOperations().getFriendIds();
        do {
            for(String facebookId: friends)
                connections.add(toConnectionKey(facebookId));
            pagingParameters = friends.getNextPage();
        } while(pagingParameters != null && (pagingParameters.getLimit() > pagingParameters.getOffset()));
        // Step 2. Returning created PlayerProfile
        return connections;
    }

    @Override
    public String share(String userId, String message, Facebook api) {
        return api.feedOperations().post(userId, message);
    }

}
