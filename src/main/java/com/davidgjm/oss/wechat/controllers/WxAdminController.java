package com.davidgjm.oss.wechat.controllers;

import com.davidgjm.oss.wechat.domain.WxSession;
import com.davidgjm.oss.wechat.domain.WxUser;
import com.davidgjm.oss.wechat.services.WxUserService;
import com.davidgjm.oss.wechat.services.WxSessionService;
import com.davidgjm.oss.wechat.exception.WxUserPhoneNumberNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/admin")
public class WxAdminController extends AbstractWechatController {
    private final WxUserService wxUserService;
    private final WxSessionService wxSessionService;

    public WxAdminController(WxUserService wxUserService, WxSessionService wxSessionService) {
        this.wxUserService = wxUserService;
        this.wxSessionService = wxSessionService;
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserByPhone(@RequestParam @NotNull @NotBlank @Pattern(regexp = "\\d{11}") String phone) {
        log.info("Attempting to delete user data by phone number. {}", phone);
        Optional<WxUser> wxUserOptional = wxUserService.findUserByPhone(phone);
        WxUser wxUser = wxUserOptional.orElseThrow(WxUserPhoneNumberNotFoundException::new);
        log.debug("Persisted user details: {}", wxUser);

        log.debug("Deleting session data");
        wxSessionService.deleteByOpenid(wxUser.getOpenid());

        log.debug("Deleting user data");
        wxUserService.deleteUser(wxUser);

        log.info("User data deleted from system.");
    }

    @GetMapping
    @ResponseStatus
    public void queryUserInfo(@RequestParam @NotNull @NotBlank String openid) {
        log.info("Checking user status by openid. {}", openid);

        log.info("Checking session information");
        Optional<WxSession> sessionOptional=wxSessionService.findByOpenid(openid);
        sessionOptional.ifPresent(s -> {
            log.info("Session details: code={}, skey={}, accessCount={}", s.getCode(), s.getSkey(), s.getAccessCount());
        });

        log.info("Checking user entity...");
        Optional<WxUser> wxUserOptional = wxUserService.findByOpenid(openid);
        wxUserOptional.ifPresent(u ->{
            log.info("User details: phone={}, created={}, lastLogin={}", u.getPhoneNumber(), u.getCreationDate(), u.getLastLogin());
        });
    }
}
