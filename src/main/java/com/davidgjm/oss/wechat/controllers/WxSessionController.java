package com.davidgjm.oss.wechat.controllers;

import com.davidgjm.oss.wechat.domain.WxSession;
import com.davidgjm.oss.wechat.dto.WxSessionDTO;
import com.davidgjm.oss.wechat.model.WxSessionMapper;
import com.davidgjm.oss.wechat.services.WxUserManagementService;
import com.davidgjm.oss.wechat.services.WxSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/session")
public class WxSessionController extends AbstractWechatController {
    private final WxUserManagementService wxUserManagementService;
    private final WxSessionService wxSessionService;
    private WxSessionMapper sessionMapper;

    public WxSessionController(WxUserManagementService wxUserManagementService, WxSessionService wxSessionService) {
        this.wxUserManagementService = wxUserManagementService;
        this.wxSessionService = wxSessionService;
    }

    @Autowired
    public void setSessionMapper(WxSessionMapper sessionMapper) {
        this.sessionMapper = sessionMapper;
    }

    @GetMapping("/get_session")
    @ResponseStatus(HttpStatus.CREATED)
    public WxSessionDTO getNewSession(@RequestParam @NotNull @NotBlank String code) {
        log.info("Requesting session key for {}", code);


        /*
         * 1. First check if an existing session associated with this code exists. Return this existing session if found
         * 2. Request for a new session if not found.
         */
        WxSessionDTO session = null;
        Optional<WxSession> wxSessionOptional = wxSessionService.findByLoginCode(code);
        if (wxSessionOptional.isPresent()) {
            session = sessionMapper.wxSessionToWxSessionDTO(wxSessionOptional.get());
            log.debug("Found existing session. Reusing existing session {}", session.getSkey());
        } else {
            log.info("Requesting new session with code: {}", code);
            session = wxUserManagementService.startSession(code);
        }
        log.info("Retrieved skey: {}", session.getSkey());
        return session;
    }


    @GetMapping("/check_status")
    @ResponseStatus(HttpStatus.OK)
    public void checkSessionStatus(@RequestParam @NotNull @NotBlank String skey) {
        log.debug("Checking session for skey: {}", skey);
        WxSession wxSession = wxSessionService.findBySkey(skey);
        log.info("session last modified at {}", wxSession.getLastModifiedDate());
    }

}