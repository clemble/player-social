package com.clemble.casino.server.social;

import java.util.EnumMap;

import com.clemble.casino.error.ClembleErrorCode;
import com.clemble.casino.error.ClembleException;
import com.clemble.casino.social.SocialProvider;

public class SocialAdapterRegistry {

    final private EnumMap<SocialProvider, SocialAdapter<?>> ADAPTERS_MAP = new EnumMap<SocialProvider, SocialAdapter<?>>(SocialProvider.class);

    public SocialAdapterRegistry() {
    }

    public SocialAdapter<?> register(SocialAdapter<?> socialAdapter) {
        SocialProvider provider = SocialProvider.valueOf(socialAdapter.getConnectionFactory().getProviderId());
        return ADAPTERS_MAP.put(provider, socialAdapter);
    }

    public SocialAdapter<?> getSocialAdapter(SocialProvider provider) {
        // Step 1. Fetching SocialConnectionAdapter
        SocialAdapter<?> connectionAdapter = ADAPTERS_MAP.get(provider);
        // Step 2. Sanity check
        if (connectionAdapter == null)
            throw ClembleException.fromError(ClembleErrorCode.SocialConnectionProviderNotSupported);
        // Step 3. Returning found ConnectionAdapters
        return connectionAdapter;
    }

}
