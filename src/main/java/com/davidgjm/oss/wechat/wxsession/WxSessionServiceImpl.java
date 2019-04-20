package com.davidgjm.oss.wechat.wxsession;

import com.davidgjm.oss.wechat.base.services.WxApiService;
import com.davidgjm.oss.wechat.base.util.ParamChecker;
import com.davidgjm.oss.wechat.crypto.WxCryptoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
public class WxSessionServiceImpl implements WxSessionService {
    private final WxSessionRepository wxSessionRepository;
    private final WxCryptoService cryptoService;
    private final WxApiService wxApiService;

    public WxSessionServiceImpl(WxSessionRepository wxSessionRepository, WxCryptoService cryptoService, WxApiService wxApiService) {
        this.wxSessionRepository = wxSessionRepository;
        this.cryptoService = cryptoService;
        this.wxApiService = wxApiService;
    }

    @Override
    public WxSession requestNewSession(@NotNull @NotBlank String loginCode) {
        log.debug("Requesting new wx session with login code: {}", loginCode);
        WxApiSessionResponse sessionResponse = wxApiService.getSession(loginCode);
        WxSession session = sessionResponse.toWxSession();
        session.setCode(loginCode);
        //handling skey
        cryptoService.encryptSessionKey(session);
        log.info("Saving session information into database.");
        createOrUpdate(session);
        return session;
    }

    @Override
    public WxSessionDTO createOrUpdate(WxSession session) {
        log.debug("Saving session. {}", session);
        Optional<WxSession> wxSessionOptional = wxSessionRepository.findByOpenid(session.getOpenid());
        WxSession updated = null;

        if (wxSessionOptional.isPresent()) {
            WxSession persisted = wxSessionOptional.get();
            persisted.setCode(session.getCode());
            persisted.setSessionKey(session.getSessionKey());
            persisted.setSkey(session.getSkey());
            updated = wxSessionRepository.save(persisted);
        } else {
            updated = wxSessionRepository.save(session);
        }
        return wxSessionToWxSessionDTO(updated);
    }

    @Override
    public Optional<WxSession> findByLoginCode(String loginCode) {
        ParamChecker.checkArg(loginCode, "Login code is required!");
        return wxSessionRepository.findByCode(loginCode);
    }

    @Override
    public Optional<WxSession> findByOpenid(String openid) {
        return wxSessionRepository.findByOpenid(openid);
    }

    @Override
    public Optional<WxSession> findBySkey(String skey) {
        return wxSessionRepository.findBySkey(skey);
    }

    @Override
    public void deleteByOpenid(@NotNull @NotBlank String openid) {
        log.debug("Deleting user session data with openid {}", openid);
        Optional<WxSession> wxSessionOptional = findByOpenid(openid);
        if (wxSessionOptional.isPresent()) {
            wxSessionRepository.delete(wxSessionOptional.get());
        } else {
            log.warn("Unable to delete. Provided openid not found. {}", openid);
        }
    }

    @Override
    public WxSessionDTO wxSessionToWxSessionDTO(WxSession session) {
        assert session != null;

        WxSessionDTO wxSessionDTO = new WxSessionDTO();

        wxSessionDTO.setSkey( session.getSkey() );

        return wxSessionDTO;
    }
}
