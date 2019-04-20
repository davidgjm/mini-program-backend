package com.davidgjm.oss.wechat.wxuserinfo;

import com.davidgjm.oss.wechat.crypto.WxEncryptedData;

public interface WxUserInfoService {
    WxUserInfo save(String skey, WxEncryptedData encryptedUserInfos);
}
