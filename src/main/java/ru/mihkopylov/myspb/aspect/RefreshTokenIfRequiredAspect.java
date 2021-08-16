package ru.mihkopylov.myspb.aspect;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import ru.mihkopylov.myspb.interceptor.SessionContext;
import ru.mihkopylov.myspb.service.LoginService;
import ru.mihkopylov.myspb.service.dto.Token;

import javax.servlet.http.HttpSession;

/**
 * If request to OurSpb is failed because of access token expired,
 * this aspect will try to refresh the token and repeat the original request.
 */
@Aspect
@Component
@AllArgsConstructor
public class RefreshTokenIfRequiredAspect {
    @NonNull
    private final SessionContext sessionContext;
    @NonNull
    private final LoginService loginService;
    @NonNull
    private final HttpSession httpSession;

    @Around("@annotation(ru.mihkopylov.myspb.aspect.RefreshTokenIfRequired)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (HttpClientErrorException.Unauthorized e) {
            if (sessionContext.getToken().isPresent()) {
                Token newToken = Token.fromResponse(loginService.refreshToken());
                sessionContext.setToken(newToken);
                return joinPoint.proceed();
            } else {
                httpSession.invalidate();
                throw e;
            }
        }
    }
}
