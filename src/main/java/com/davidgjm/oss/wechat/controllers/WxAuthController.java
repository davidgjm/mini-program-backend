package com.davidgjm.oss.wechat.controllers;

import com.davidgjm.oss.wechat.domain.WxSession;
import com.davidgjm.oss.wechat.domain.WxUser;
import com.davidgjm.oss.wechat.model.WxEncryptedData;
import com.davidgjm.oss.wechat.dto.WxLoginDTO;
import com.davidgjm.oss.wechat.services.WxUserService;
import com.davidgjm.oss.wechat.services.WxUserManagementService;
import com.davidgjm.oss.wechat.services.WxCryptoService;
import com.davidgjm.oss.wechat.services.WxSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
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
    public void doWxLogin(@RequestBody @NotNull WxEncryptedData userLoginData) {
        log.debug("Using get phone number as login method: {}", this.useGetPhoneNumberAsUserLoginMethod);
        log.debug("Login data type: {}", userLoginData.getClass().getSimpleName());

        String skey = userLoginData.getSkey();
        log.info("Provided skey: {}", skey);
        wxUserManagementService.checkSession(skey);

        if (this.useGetPhoneNumberAsUserLoginMethod) {
            setUserPhoneNumber(userLoginData);
        } else {
            if (userLoginData instanceof WxLoginDTO) {
                WxLoginDTO wxLoginDTO = (WxLoginDTO) userLoginData;
                log.info("Registering/Logging user session: {}", userLoginData.getSkey());

                //validate raw data
                WxUser persisted = wxUserManagementService.registerOrLogin(wxLoginDTO);
                log.debug("Register/login completed user: {}", persisted);

            }
        }
    }

    private void setUserPhoneNumber(@RequestBody @NotNull WxEncryptedData encryptedData) {
        log.info("Updating phone number for user.");
        WxSession session = wxSessionService.findBySkey(encryptedData.getSkey());
        String phoneNumber = cryptoService.decryptPhoneNumber(session, encryptedData);
        log.debug("Decrypted phone number: {}", phoneNumber);


        Optional<WxUser> wxUserOptional = findUserBySkey(encryptedData.getSkey());
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
        WxSession sessionOptional = wxSessionService.findBySkey(skey);
        return wxUserService.findByOpenid(sessionOptional.getOpenid());
    }
}
