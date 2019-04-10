package com.davidgjm.oss.wechat.auth;

import com.davidgjm.oss.wechat.base.ApiErrorDetails;
import com.davidgjm.oss.wechat.wxuser.WxUserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter checks the value of HTTP header <code>skey</code>.
 * The filter chain will pass on if the skey is valid and a registered user exists in the system.
 * Otherwise, this filter will fail.
 */
@Slf4j
@Component
public class WxAuthenticationFilter extends OncePerRequestFilter {
    private final WxUserManagementService wxUserManagementService;
    private final ObjectMapper objectMapper=new ObjectMapper();

    public WxAuthenticationFilter(WxUserManagementService wxUserManagementService) {
        this.wxUserManagementService = wxUserManagementService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String uri=request.getRequestURI();
        String method = request.getMethod();
        String requestInfo = method + " " + uri;
        log.debug("REST API request: {}", requestInfo);
        String skey = request.getHeader("skey");
        if (skey == null || skey.isEmpty()) {
            log.error("Header \"skey\" is required!");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "skey is required!");
            return;
        }

        log.debug("Validating skey {}", skey);

        //check if session valid first
        boolean isSessionFound=wxUserManagementService.existsSessionBySkey(skey);
        if (!isSessionFound) {
            log.error("Session not found with the skey!");
            handleException(response, HttpStatus.BAD_REQUEST, new WxSkeyNotFoundException("Invalid skey"));
            return;
        }

        //check if the user exists
        boolean isRegisteredUser = wxUserManagementService.existsUserBySkey(skey);
        if (!isRegisteredUser) {
            log.error("User not found with the skey!");
            handleException(response, HttpStatus.FORBIDDEN, new WxUserNotFoundException("The user may not be in registered status!"));
            return;
        }
        log.debug("skey is valid and corresponding user exists. {}", skey);
        chain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, HttpStatus status, Exception e) throws IOException {
        ApiErrorDetails errorDetails = ApiErrorDetails.from(status, e);
        response.setStatus(status.value());
        response.getWriter().write(toJson(errorDetails));
    }

    private String toJson(ApiErrorDetails errorDetails) throws JsonProcessingException {
        return objectMapper.writeValueAsString(errorDetails);
    }

}
