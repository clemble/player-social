package com.clemble.casino.server.social.controller;

import static com.clemble.casino.social.SocialWebMapping.*;
import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.registration.service.PlayerSocialRegistrationService;
import com.clemble.casino.server.social.SocialConnectionDataAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.registration.PlayerSocialGrantRegistrationRequest;
import com.clemble.casino.registration.PlayerSocialRegistrationRequest;
import com.clemble.casino.server.ServerController;
import com.clemble.casino.WebMapping;

import javax.validation.Valid;

@RestController
public class PlayerSocialRegistrationController implements PlayerSocialRegistrationService, ServerController {

    final private SocialConnectionDataAdapter registrationService;

    public PlayerSocialRegistrationController(
        final SocialConnectionDataAdapter registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = SOCIAL_REGISTRATION_DESCRIPTION, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public String register(@Valid @RequestBody PlayerSocialRegistrationRequest socialRegistrationRequest) {
        // Step 1. Checking if this user already exists
        String player = registrationService.register(socialRegistrationRequest.getSocialConnectionData());
        // Step 2. All done continue
        return player;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = SOCIAL_REGISTRATION_GRANT, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public String register(@Valid @RequestBody PlayerSocialGrantRegistrationRequest grantRegistrationRequest) {
        // Step 1. Checking if this user already exists
        String player = registrationService.register(grantRegistrationRequest.getAccessGrant());
        // Step 2. All done continue
        return player;
    }

}
