package com.davidgjm.oss.wechat.wxuserinfo;

import com.davidgjm.oss.wechat.base.controllers.AbstractWechatController;
import com.davidgjm.oss.wechat.crypto.WxCryptoService;
import com.davidgjm.oss.wechat.crypto.WxEncryptedData;
import com.davidgjm.oss.wechat.wxsession.WxSessionService;
import com.davidgjm.oss.wechat.wxuser.WxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/userinfo")
public class WxUserInfoController extends AbstractWechatController {
    private final WxUserInfoService userInfoService;

    private final WxUserService wxUserService;
    private final WxCryptoService cryptoService;
    private final WxSessionService wxSessionService;

    public WxUserInfoController(WxUserInfoService userInfoService, WxUserService wxUserService, WxCryptoService cryptoService, WxSessionService wxSessionService) {
        this.userInfoService = userInfoService;
        this.wxUserService = wxUserService;
        this.cryptoService = cryptoService;
        this.wxSessionService = wxSessionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void doWxLogin(@RequestHeader @NotBlank String skey, @RequestBody @NotNull WxEncryptedData encryptedUserInfo) {
        log.debug("Received user info: {}", encryptedUserInfo);
        log.info("Provided skey: {}", skey);

//        //validate raw data
        WxUserInfo persisted = userInfoService.save(skey, encryptedUserInfo);
        log.debug("User info: {}", persisted);

    }


}
