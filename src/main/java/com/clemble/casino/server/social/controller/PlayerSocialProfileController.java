package com.clemble.casino.server.social.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.server.ExternalController;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.social.SocialConnectionData;
import com.clemble.casino.social.service.PlayerSocialProfileService;
import com.clemble.casino.server.social.SocialConnectionDataAdapter;
import com.clemble.casino.WebMapping;
import static com.clemble.casino.social.SocialWebMapping.*;

@RestController
public class PlayerSocialProfileController implements PlayerSocialProfileService, ExternalController {

    final private SocialConnectionDataAdapter socialConnectionDataAdapter;

    public PlayerSocialProfileController(SocialConnectionDataAdapter socialConnectionDataAdapter) {
        this.socialConnectionDataAdapter = checkNotNull(socialConnectionDataAdapter);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = SOCIAL_PLAYER, produces = WebMapping.PRODUCES)
    public SocialConnectionData add(@PathVariable("player") String playerId, @RequestBody SocialConnectionData socialConnectionData) {
        return socialConnectionDataAdapter.add(playerId, socialConnectionData);
    }

}
