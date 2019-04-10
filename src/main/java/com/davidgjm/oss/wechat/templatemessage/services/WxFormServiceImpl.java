package com.davidgjm.oss.wechat.templatemessage.services;

import com.davidgjm.oss.wechat.templatemessage.WxFormIdAlreadyExistsException;
import com.davidgjm.oss.wechat.templatemessage.WxFormIdNotFoundException;
import com.davidgjm.oss.wechat.templatemessage.domain.WxForm;
import com.davidgjm.oss.wechat.templatemessage.repositories.WxFormRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WxFormServiceImpl implements WxFormService {
    private final WxFormRepository repository;

    public WxFormServiceImpl(WxFormRepository repository) {
        this.repository = repository;
    }

    @Override
    public WxForm addFormid(@NotNull @NotBlank String openid, @NotNull @NotBlank String formId) {
        boolean isMockFormId = formId.contains("mock one");
        if (isMockFormId) {
            log.warn("The incoming formId is mocked ({}). It won't be saved.", formId);
            return null;
        }

        if (repository.findByOpenidAndFormid(openid, formId).isPresent()) {
            log.error("formid already exists! {}", formId);
            throw new WxFormIdAlreadyExistsException("WeChat formid already exists!");
        }
        log.debug("Adding new formid {}", formId);
        return repository.saveAndFlush(new WxForm(openid, formId, false));
    }

    @Override
    public void markAsUsed(@NotNull @NotBlank String openid, @NotNull @NotBlank String formid) {
        Optional<WxForm> wxFormOptional = repository.findByOpenidAndFormid(openid, formid);
        if (!wxFormOptional.isPresent()) {
            log.error("Formid not found {}", formid);
            throw new WxFormIdNotFoundException("Formid not found");
        }

        log.info("Marking formid as used {}", formid);
        WxForm persisted = wxFormOptional.get();
        persisted.setUsed(true);
        repository.saveAndFlush(persisted);
    }

    @Override
    public void remove(@NotNull @NotBlank String openid, @NotNull @NotBlank String formid) {
        Optional<WxForm> wxFormOptional = repository.findByOpenidAndFormid(openid, formid);
        if (!wxFormOptional.isPresent()) {
            log.error("Formid not found {}", formid);
            throw new WxFormIdNotFoundException("Formid not found");
        }

        log.debug("Deleting formid from system - {}", formid);
        repository.deleteById(wxFormOptional.get().getId());
    }

    @Override
    public Optional<String> getAnyAvailableFormid(@NotNull @NotBlank String openid) {
        log.debug("Finding available formid for user {}", openid);
        List<WxForm> wxForms = repository.findAllByOpenidAndIsUsedFalse(openid);
        if (wxForms.isEmpty()) {
            log.error("No formid is available for user {}", openid);
            return Optional.empty();
        }

        return wxForms.stream().findAny().map(WxForm::getFormid);
    }
}
