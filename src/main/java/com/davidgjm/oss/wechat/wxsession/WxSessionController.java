package com.davidgjm.oss.wechat.wxsession;

import com.davidgjm.oss.wechat.base.controllers.AbstractWechatController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/session")
public class WxSessionController extends AbstractWechatController {
    private final WxSessionService wxSessionService;

    public WxSessionController(WxSessionService wxSessionService) {
        this.wxSessionService = wxSessionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public WxSessionDTO getNewSession(@RequestBody @NotNull @NotBlank WxLoginCodeDTO loginCodeDTO) {
        log.info("Requesting session key for {}", loginCodeDTO);

        String code = loginCodeDTO.getLoginCode();
        WxSessionDTO session;
        Optional<WxSession> wxSessionOptional = wxSessionService.findByLoginCode(code);
        if (wxSessionOptional.isPresent()) {
            session = wxSessionService.wxSessionToWxSessionDTO(wxSessionOptional.get());
            log.debug("Found existing session. Reusing existing session {}", session.getSkey());
        } else {
            log.info("Requesting new session with code: {}", code);
            session = wxSessionService.wxSessionToWxSessionDTO(wxSessionService.requestNewSession(code));
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
