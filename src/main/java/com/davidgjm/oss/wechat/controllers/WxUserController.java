package com.davidgjm.oss.wechat.controllers;

import com.davidgjm.oss.wechat.domain.WxSession;
import com.davidgjm.oss.wechat.domain.WxUser;
import com.davidgjm.oss.wechat.dto.WxSessionDTO;
import com.davidgjm.oss.wechat.dto.WxSkeyDTO;
import com.davidgjm.oss.wechat.services.WxUserService;
import com.davidgjm.oss.wechat.services.WxUserManagementService;
import com.davidgjm.oss.wechat.services.WxSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
public class WxUserController extends AbstractWechatController {

    private final WxUserService wxUserService;
    private final WxSessionService wxSessionService;
    private final WxUserManagementService wxUserManagementService;

    public WxUserController(WxUserService wxUserService, WxSessionService wxSessionService, WxUserManagementService wxUserManagementService) {
        this.wxUserService = wxUserService;
        this.wxSessionService = wxSessionService;
        this.wxUserManagementService = wxUserManagementService;
    }

    @DeleteMapping
    public void deleteUser(@RequestBody @NotNull @Validated WxSkeyDTO skeyDTO) {
        log.info("Removing user [{}] from system", skeyDTO);
        WxSession wxSession = wxSessionService.findBySkey(skeyDTO.getSkey());

        Optional<WxUser> userOptional = wxUserService.findByOpenid(wxSession.getOpenid());
        log.info("Attempting to delete user from system");
        userOptional.ifPresent(wxUserService::deleteUser);

        log.debug("Deleting session data");
        wxSessionService.deleteByOpenid(wxSession.getOpenid());

        log.info("User is removed successfully.");
    }

    @GetMapping("/check_status")
    @ResponseStatus(HttpStatus.OK)
    public WxSessionDTO checkUserStatus(@RequestParam @NotNull @NotBlank String skey) {
        log.debug("Validating session and registration status for {}", skey);
        return wxUserManagementService.checkSession(skey);
    }

}
