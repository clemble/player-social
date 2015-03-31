package com.clemble.casino.server.social.listener;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.server.event.player.SystemPlayerConnectionsFetchedEvent;
import com.clemble.casino.server.social.SocialAdapter;
import com.clemble.casino.server.social.SocialAdapterRegistry;
import com.clemble.casino.social.SocialProvider;
import org.springframework.social.ApiBinding;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import com.clemble.casino.server.event.player.SystemPlayerSocialAddedEvent;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.notification.SystemNotificationService;

public class SystemPlayerSocialAddedEventListener implements SystemEventListener<SystemPlayerSocialAddedEvent> {

    final private SocialAdapterRegistry socialAdapterRegistry;
    final private UsersConnectionRepository usersConnectionRepository;
    final private SystemNotificationService notificationService;

    public SystemPlayerSocialAddedEventListener(
        SocialAdapterRegistry socialAdapterRegistry,
        UsersConnectionRepository usersConnectionRepository,
        SystemNotificationService notificationService) {
        this.socialAdapterRegistry = checkNotNull(socialAdapterRegistry);
        this.notificationService = checkNotNull(notificationService);
        this.usersConnectionRepository = checkNotNull(usersConnectionRepository);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void onEvent(SystemPlayerSocialAddedEvent event) {
        SocialProvider provider = SocialProvider.valueOf(event.getConnection().getProviderId());
        // Step 1. Finding appropriate SocialConnectionAdapter
        SocialAdapter socialAdapter = socialAdapterRegistry.getSocialAdapter(provider);
        // Step 2. Fetching connection
        ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(event.getPlayer());
        Connection<?> connection = connectionRepository.getConnection(event.getConnection());
        // Step 3. Fetching PlayerSocialNetwork and existing connections
        Collection<ConnectionKey> connectionKeys = socialAdapter.fetchConnections((ApiBinding) connection.getApi());
        // Step 7. Finding difference
        notificationService.send(new SystemPlayerConnectionsFetchedEvent(event.getPlayer(), event.getConnection(), connectionKeys));
    }

    @Override
    public String getChannel() {
        return SystemPlayerSocialAddedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerSocialAddedEvent.CHANNEL + " > player:social";
    }

}
