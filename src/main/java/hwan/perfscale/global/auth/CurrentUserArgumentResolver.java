package hwan.perfscale.global.auth;

import hwan.perfscale.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 인증 계층이 붙기 전까지 쓰는 자리표시자.
 * 실제 JWT 인증을 붙일 때는 이 클래스 내부(resolveArgument)만 SecurityContext에서
 * 유저 id를 꺼내는 로직으로 교체하면 되고, 컨트롤러 시그니처(@CurrentUserId Long userId)는 그대로 유지된다.
 */
@Component
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUserId.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return userRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("테스트 유저가 없습니다. DataInitializer 확인 필요"))
                .getId();
    }
}
