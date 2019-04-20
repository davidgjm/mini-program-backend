package com.davidgjm.oss.wechat.auth;

import com.davidgjm.oss.wechat.crypto.DigestUtil;
import com.davidgjm.oss.wechat.crypto.WxCryptoService;
import com.davidgjm.oss.wechat.crypto.WxEncryptedData;
import com.davidgjm.oss.wechat.crypto.WxSecurityException;
import com.davidgjm.oss.wechat.wxsession.WxSession;
import com.davidgjm.oss.wechat.wxsession.WxSessionDTO;
import com.davidgjm.oss.wechat.wxsession.WxSessionService;
import com.davidgjm.oss.wechat.wxuser.WxUser;
import com.davidgjm.oss.wechat.wxuser.WxUserNotFoundException;
import com.davidgjm.oss.wechat.wxuser.WxUserService;
import com.davidgjm.oss.wechat.wxuserinfo.WxUserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Slf4j
@Service
public class WxUserManagementServiceImpl implements WxUserManagementService {

    private final WxSessionService wxSessionService;
    private final WxCryptoService cryptoService;
    private final WxUserService wxUserService;

    public WxUserManagementServiceImpl(WxSessionService wxSessionService, WxCryptoService cryptoService, WxUserService wxUserService) {
        this.wxSessionService = wxSessionService;
        this.cryptoService = cryptoService;
        this.wxUserService = wxUserService;
    }

    @Override
    public WxSessionDTO startSession(String loginCode) {
        WxSession session = wxSessionService.requestNewSession(loginCode);
        return wxSessionService.createOrUpdate(session);
    }

    @Override
    public WxSessionDTO checkSession(String skey) {
        WxSession wxSession = wxSessionService.findBySkey(skey).orElseThrow(WxUserNotFoundException::new);
        WxSessionDTO sessionDTO = new WxSessionDTO();
        sessionDTO.setSkey(skey);
        log.debug("Checking user registration status");
        return sessionDTO;
    }

    @Override
    public boolean validateSignature(String skey, WxEncryptedData wxLoginDTO) {
        WxSession session = wxSessionService.findBySkey(skey).orElseThrow(WxUserNotFoundException::new);

        String computedSignature = computeSignature(wxLoginDTO, session);
        if (!computedSignature.equals(wxLoginDTO.getSignature())) {
            log.error("Signature validation failed. \nsignature: [{}]\ncomputed: [{}]", wxLoginDTO.getSignature(), computedSignature);
            throw new WxSecurityException();
        }
        return true;
    }

    private String computeSignature(WxEncryptedData wxLoginDTO, WxSession session) {

        return DigestUtil.HASH_SHA1.hash(wxLoginDTO.getRawData() + session.getSessionKey());
    }

    @Override
    public WxUser registerOrLogin(String skey, WxEncryptedData wxLoginDTO) {
        log.debug("Validating login data...");
        validateSignature(skey,wxLoginDTO);

        WxSession session = wxSessionService.findBySkey(skey).orElseThrow(WxUserNotFoundException::new);
        WxUserInfoDTO userInfoDTO = cryptoService.decryptUserInfo(session.getSessionKey(), wxLoginDTO);
        log.debug("Decrypted user data. {}", userInfoDTO);


        return wxUserService.saveOrUpdate(userInfoDTO);
    }

    @Override
    public boolean existsUserBySkey(@NotNull @NotBlank String skey) {
        log.debug("Checking if a user exists with skey {}", skey);
        WxSession session=getSessionBySkey(skey);
        if (session == null) {
            log.warn("Session not found");
            return false;
        }
        return wxUserService.findByOpenid(session.getOpenid()).isPresent();
    }

    @Override
    public boolean existsSessionBySkey(@NotNull @NotBlank String skey) {
        log.debug("Check if a session exists with the skey {}", skey);
        return null != getSessionBySkey(skey);
    }

    private WxSession getSessionBySkey(String skey) {
        WxSession session;
        try {
            session = wxSessionService.findBySkey(skey).orElseThrow(WxUserNotFoundException::new);
        } catch (InvalidSkeyException e) {
            log.warn("skey not found! {}", skey);
            return null;
        }
        return session;
    }

    @Override
    public WxUser findUserBySkey(@NotNull @NotBlank String skey) throws WxUserNotFoundException {
        boolean isUserExists = existsUserBySkey(skey);
        if (!isUserExists) throw new WxUserNotFoundException("WxUser not found with the provided skey!");
        log.debug("Finding user with skey: {}", skey);
        return wxUserService.findByOpenid(wxSessionService.findBySkey(skey).orElseThrow(WxUserNotFoundException::new).getOpenid()).orElseThrow(WxUserNotFoundException::new);
    }

}
