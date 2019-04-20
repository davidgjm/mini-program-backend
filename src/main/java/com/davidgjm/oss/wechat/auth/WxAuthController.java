package com.davidgjm.oss.wechat.auth;

import com.davidgjm.oss.wechat.base.controllers.AbstractWechatController;
import com.davidgjm.oss.wechat.wxsession.WxSession;
import com.davidgjm.oss.wechat.wxuser.WxUser;
import com.davidgjm.oss.wechat.crypto.WxEncryptedData;
import com.davidgjm.oss.wechat.wxuser.WxUserNotFoundException;
import com.davidgjm.oss.wechat.wxuser.WxUserService;
import com.davidgjm.oss.wechat.crypto.WxCryptoService;
import com.davidgjm.oss.wechat.wxsession.WxSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
public class WxAuthController extends AbstractWechatController {
    private final WxUserManagementService wxUserManagementService;

    private final WxUserService wxUserService;
    private final WxCryptoService cryptoService;
    private final WxSessionService wxSessionService;

    private boolean useGetPhoneNumberAsUserLoginMethod;

    public WxAuthController(WxUserManagementService wxUserManagementService, WxUserService wxUserService, WxCryptoService cryptoService, WxSessionService wxSessionService) {
        this.wxUserManagementService = wxUserManagementService;
        this.wxUserService = wxUserService;
        this.cryptoService = cryptoService;
        this.wxSessionService = wxSessionService;
    }

    @PostConstruct
    private void init() {
        this.useGetPhoneNumberAsUserLoginMethod = true;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void doWxLogin(@RequestHeader @NotBlank String skey, @RequestBody @NotNull WxEncryptedData userLoginData) {
        log.debug("Using get phone number as login method: {}", this.useGetPhoneNumberAsUserLoginMethod);
        log.debug("Login data type: {}", userLoginData.getClass().getSimpleName());

        log.info("Provided skey: {}", skey);
        wxUserManagementService.checkSession(skey);

        if (this.useGetPhoneNumberAsUserLoginMethod) {
            setUserPhoneNumber(skey,userLoginData);
        } else {
            log.info("Registering/Logging user session: {}", skey);
            //validate raw data
            WxUser persisted = wxUserManagementService.registerOrLogin(skey,userLoginData);
            log.debug("Register/login completed user: {}", persisted);
        }
    }

    private void setUserPhoneNumber(String skey, WxEncryptedData encryptedData) {
        log.info("Updating phone number for user.");
        WxSession session = wxSessionService.findBySkey(skey).orElseThrow(WxUserNotFoundException::new);
        String phoneNumber = cryptoService.decryptPhoneNumber(session.getSessionKey(), encryptedData);
        log.debug("Decrypted phone number: {}", phoneNumber);


        Optional<WxUser> wxUserOptional = findUserBySkey(skey);
        WxUser wxUser=wxUserOptional.orElseGet(() -> {
            WxUser _user = new WxUser();
            _user.setActive(true);
            _user.setOpenid(session.getOpenid());
            return _user;
        });
        wxUser.setPhoneNumber(phoneNumber);
        wxUser.setLastLogin(LocalDateTime.now());
        wxUser.incrementLoginCount();
        WxUser persisted= wxUserService.saveOrUpdate(wxUser);
        log.info("Updated wx user: {}", persisted);
    }


    private Optional<WxUser> findUserBySkey(String skey) {
        WxSession sessionOptional = wxSessionService.findBySkey(skey).orElseThrow(WxUserNotFoundException::new);
        return wxUserService.findByOpenid(sessionOptional.getOpenid());
    }
}
