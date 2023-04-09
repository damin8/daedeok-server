package hours22.daedeokserver.common;

import hours22.daedeokserver.user.domain.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String phone_num() default "123456789";

    String name() default "22hours";

    Role role() default Role.ROLE_MEMBER;
}
