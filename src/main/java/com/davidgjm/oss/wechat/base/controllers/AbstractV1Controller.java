package com.davidgjm.oss.wechat.base.controllers;

import org.springframework.web.bind.annotation.RequestMapping;

import static com.davidgjm.oss.wechat.base.controllers.AbstractV1Controller.BASE_PATH;

@RequestMapping(BASE_PATH)
public abstract class AbstractV1Controller extends AbstractController {
    static final String BASE_PATH = "/v1";
}
