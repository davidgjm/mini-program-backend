package com.davidgjm.oss.wechat.controllers;

import org.springframework.web.bind.annotation.RequestMapping;

import static com.davidgjm.oss.wechat.controllers.AbstractV1Controller.BASE_PATH;

@RequestMapping(BASE_PATH)
public abstract class AbstractV1Controller extends AbstractController {
    static final String BASE_PATH = "/v1";
}
