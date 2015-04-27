package com.clemble.casino.server.social.adapter;

import com.clemble.casino.player.PlayerGender;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.social.SocialAdapter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.vkontakte.api.PostData;
import org.springframework.social.vkontakte.api.VKontakte;
import org.springframework.social.vkontakte.api.VKontakteDate;
import org.springframework.social.vkontakte.api.VKontakteProfile;
import org.springframework.social.vkontakte.connect.VKontakteConnectionFactory;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by mavarazy on 11/24/14.
 */
public class VKontakteSocialAdapter implements SocialAdapter<VKontakte> {

    final private VKontakteConnectionFactory vKontakteConnectionFactory;

    public VKontakteSocialAdapter(VKontakteConnectionFactory vKontakteConnectionFactory) {
        this.vKontakteConnectionFactory = vKontakteConnectionFactory;
    }

    public ConnectionFactory<VKontakte> getConnectionFactory() {
        return vKontakteConnectionFactory;
    }

    @Override
    public PlayerProfile fetchPlayerProfile(VKontakte api) {
        VKontakteProfile profile = api.usersOperations().getUser();
        // TODO add timezone to VK
        // Step 2. Generating appropriate GameProfile to return
        return new PlayerProfile()
            .addSocialConnection(toConnectionKey(profile.getUid()))
            .setFirstName(profile.getFirstName())
            .setNickName(profile.getScreenName())
            .setLastName(profile.getLastName())
            .setBirthDate(toDate(profile.getBirthDate()))
            .setGender(PlayerGender.parse(profile.getGender()));
    }

    public String getEmail(VKontakte api) {
        return null;
    }

    @Override
    public Collection<ConnectionKey> fetchConnections(VKontakte api) {
        Collection<ConnectionKey> connections = api.
            friendsOperations().
            get().
            stream().
            map((profile) -> toConnectionKey(profile.getUid())).
            collect(Collectors.toList());
        return connections;
    }

    @Override
    public Pair<String, String> toImageUrl(Connection<VKontakte> connectionKey) {
        // Step 1. Fetching profile
        VKontakteProfile profile = connectionKey.getApi().usersOperations().getUser();
        // Step 2. Processing images
        return new ImmutablePair<String, String>(profile.getPhotoBig(), profile.getPhotoMedium());
    }

    private DateTime toDate(VKontakteDate date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth(), date.getDay());
        return new DateTime(calendar.getTime());
    }

    public String share(String userId, String message, VKontakte vk) {
        return vk.wallOperations().post(new PostData(userId, message)).getPostId();
    }
}
