package hwan.perfscale.global.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 컨트롤러 메서드 파라미터에 붙여 로그인 유저 id를 주입받는다.
 * 실제 인증(JWT)이 붙기 전까지는 {@link CurrentUserArgumentResolver}가 고정 테스트 유저를 반환한다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUserId {
}
