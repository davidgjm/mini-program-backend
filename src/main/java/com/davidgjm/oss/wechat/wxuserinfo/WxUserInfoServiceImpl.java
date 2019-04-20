package com.davidgjm.oss.wechat.wxuserinfo;

import com.davidgjm.oss.wechat.auth.WxSkeyNotFoundException;
import com.davidgjm.oss.wechat.crypto.WxCryptoService;
import com.davidgjm.oss.wechat.crypto.WxEncryptedData;
import com.davidgjm.oss.wechat.wxsession.WxSession;
import com.davidgjm.oss.wechat.wxsession.WxSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class WxUserInfoServiceImpl implements WxUserInfoService {
    private final WxUserInfoRepository repository;
    private final WxSessionService sessionService;
    private final WxCryptoService cryptoService;

    public WxUserInfoServiceImpl(WxUserInfoRepository repository, WxSessionService sessionService, WxCryptoService cryptoService) {
        this.repository = repository;
        this.sessionService = sessionService;
        this.cryptoService = cryptoService;
    }

    @Override
    public WxUserInfo save(String skey, WxEncryptedData encryptedUserInfo) {
        log.debug("Saving user info...");
        WxSession wxSession = sessionService.findBySkey(skey).orElseThrow(WxSkeyNotFoundException::new);

        log.info("Validating signature...");
        cryptoService.validateSignature(encryptedUserInfo.getSignature(), encryptedUserInfo.getRawData(), wxSession.getSessionKey());

        log.info("Decrypting user info...");
        WxUserInfoDTO userInfoDTO = cryptoService.decryptUserInfo(wxSession.getSessionKey(), encryptedUserInfo);


        String openid = userInfoDTO.getOpenId();
        Optional<WxUserInfo> wxUserInfoOptional = repository.findByOpenId(openid);
        if (wxUserInfoOptional.isPresent()) {
            WxUserInfo persisted = wxUserInfoOptional.get();
            BeanUtils.copyProperties(userInfoDTO, persisted);
            log.debug("Updated user info: {}", persisted);
            return repository.save(persisted);

        } else {
            WxUserInfo userInfo = fromDto(userInfoDTO);
            return repository.save(userInfo);
        }
    }


    private WxUserInfo fromDto(WxUserInfoDTO dto) {
        WxUserInfo userInfo = new WxUserInfo();
        BeanUtils.copyProperties(dto, userInfo);
        return userInfo;
    }
}
