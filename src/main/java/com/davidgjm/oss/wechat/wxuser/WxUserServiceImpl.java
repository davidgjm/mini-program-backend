package com.davidgjm.oss.wechat.wxuser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Optional;

@Slf4j
@Service
public class WxUserServiceImpl implements WxUserService {
    private final UserRepository userRepository;

    private WxUserMapper userMapper;

    public WxUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserMapper(WxUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Optional<WxUser> findByOpenid(@NotEmpty String openid) {
        return userRepository.findByOpenid(openid);
    }

    @Override
    public WxUser saveOrUpdate(WxUser user) {
        Optional<WxUser> wxUserOptional = userRepository.findByOpenid(user.getOpenid());
        WxUser persisted = null;
        if (wxUserOptional.isPresent()) {
            persisted = wxUserOptional.get();
            persisted.setPhoneNumber(user.getPhoneNumber());
        } else {
            persisted = user;
        }
//        persisted.setLastLogin(LocalDateTime.now());
        return userRepository.save(persisted);
    }

    @Override
    public WxUser saveOrUpdate(WxUserInfoDTO userInfoDTO) {
        WxUser user = userMapper.toWxUser(userInfoDTO);
        return saveOrUpdate(user);
    }

    @Override
    public Optional<WxUser> findUserByPhone(@NotNull @NotBlank @Pattern(regexp = "\\d{11}") String phoneNumber) {
        log.debug("Locating user with phone number: {}", phoneNumber);
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public void deleteUser(@NotNull WxUser wxUser) {
        log.info("Deleting user from system. {}", wxUser.getOpenid());
        userRepository.delete(wxUser);
    }

    @Transactional
    @Override
    public void deleteUserByPhoneNumber(@NotNull @NotBlank @Pattern(regexp = "\\d{11}") String phoneNumber) {
        log.debug("Attempting to delete user data for user with phone number: {}", phoneNumber);
        Optional<WxUser> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        WxUser user = userOptional.orElseThrow(WxUserPhoneNumberNotFoundException::new);
        log.warn("Dangerous. Deleting user...");
        userRepository.deleteById(user.getId());
    }

    @Override
    public boolean isRegisteredUser(@NotNull @NotBlank String openid) {
        log.debug("Checking if user {} is registered or not", openid);
        return userRepository.existsByOpenidAndPhoneNumberNotNull(openid);
    }
}
