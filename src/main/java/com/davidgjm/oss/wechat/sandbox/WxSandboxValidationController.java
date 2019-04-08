package com.davidgjm.oss.wechat.sandbox;

import com.davidgjm.oss.wechat.controllers.AbstractV1Controller;
import com.davidgjm.oss.wechat.wxapi.WxApiAccessTokenResponse;
import com.davidgjm.oss.wechat.services.WxApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test/wx")
public class WxSandboxValidationController extends AbstractV1Controller {

    private final WxApiService wxApiService;

    public WxSandboxValidationController(WxApiService wxApiService) {
        this.wxApiService = wxApiService;
    }

    @GetMapping("/token")
    public WxApiAccessTokenResponse getWxToken() {
        return wxApiService.getAccessToken();
    }
}
