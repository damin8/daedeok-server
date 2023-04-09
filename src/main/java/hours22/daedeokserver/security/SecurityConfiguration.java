package hours22.daedeokserver.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String ADMIN = "ADMIN";
    private static final String MEMBER = "MEMBER";
    private static final String TUTOR = "TUTOR";

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityAuthenticationFilter securityAuthenticationFilter() {
        return new SecurityAuthenticationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.PUT, "/daedeok/acinfo/**").hasAnyRole(ADMIN)
                .antMatchers("/daedeok/lecture/plan/*/attendance").hasAnyRole(MEMBER)
                .antMatchers("/daedeok/lecture/join/**").hasAnyRole(MEMBER)
                .antMatchers(HttpMethod.DELETE, "/daedeok/lecture/*").hasAnyRole(TUTOR)
                .antMatchers(HttpMethod.PUT, "/daedeok/lecture/*").hasAnyRole(TUTOR)
                .antMatchers("/daedeok/lecture/cancel/**").hasAnyRole(MEMBER, TUTOR)
                .antMatchers("/daedeok/lecture/*/user").hasAnyRole(TUTOR)
                .antMatchers("/daedeok/lecture/board/**").hasAnyRole(MEMBER, TUTOR)
                .antMatchers("/daedeok/lecture/*/custom").hasAnyRole(MEMBER, TUTOR, ADMIN)
                .antMatchers("/daedeok/lecture/*/info").hasAnyRole(MEMBER)
                .antMatchers("/daedeok/lecture/*/update").hasAnyRole(TUTOR)
                .antMatchers("/daedeok/lecture/plan/*/online").hasAnyRole(MEMBER)
                .antMatchers("/daedeok/lecture/plan/*/user").hasAnyRole(TUTOR, MEMBER)
                .antMatchers("/daedeok/lecture/*/attendance").hasAnyRole(MEMBER)
                .antMatchers("/daedeok/lecture/*/board").hasAnyRole(MEMBER, TUTOR)
                .antMatchers("/daedeok/lecture/possible").hasAnyRole(MEMBER)
                .antMatchers("/daedeok/lecture/sidebar").hasAnyRole(MEMBER, TUTOR)
                .antMatchers("/daedeok/lecture/main").hasAnyRole(MEMBER, TUTOR)
                .antMatchers("/daedeok/lecture/complete").hasAnyRole(MEMBER)
                .antMatchers("/daedeok/lecture/finish/**").hasAnyRole(TUTOR)
                .antMatchers(HttpMethod.DELETE, "/daedeok/user/**").hasAnyRole(ADMIN, MEMBER, TUTOR)
                .antMatchers("/daedeok/user/admin/**").hasAnyRole(ADMIN)
                .antMatchers("/daedeok/user/info").hasAnyRole(MEMBER, TUTOR)
                .antMatchers(HttpMethod.POST, "/daedeok/qna/**").hasAnyRole(ADMIN, MEMBER, TUTOR)
                .antMatchers(HttpMethod.PUT, "/daedeok/qna/**").hasAnyRole(ADMIN, MEMBER, TUTOR)
                .antMatchers(HttpMethod.DELETE, "/daedeok/qna/**").hasAnyRole(ADMIN, MEMBER, TUTOR)
                .antMatchers(HttpMethod.PUT, "/daedeok/popup/**").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.PUT, "/daedeok/image/**").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.POST, "/daedeok/file/certificate/**").hasAnyRole(TUTOR)
                .antMatchers(HttpMethod.DELETE, "/daedeok/file/certificate/**").hasAnyRole(TUTOR)
                .antMatchers(HttpMethod.POST, "/daedeok/faq/**").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.PUT, "/daedeok/faq/**").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.DELETE, "/daedeok/faq/**").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.DELETE, "/daedeok/division/**").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.POST, "/daedeok/division/**").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.PUT, "/daedeok/division/**").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.POST, "/daedeok/category/**").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.PUT, "/daedeok/category/**").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.DELETE, "/daedeok/category/**").hasAnyRole(ADMIN)
                .antMatchers("/daedeok/notice/tutor/**").hasAnyRole(ADMIN, TUTOR)
                .antMatchers(HttpMethod.POST, "/daedeok/notice").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.DELETE, "/daedeok/notice/*").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.PUT, "/daedeok/notice/*").hasAnyRole(ADMIN)
                .anyRequest().permitAll()
                .and()
                .formLogin().disable();

        http
                .addFilterBefore(securityAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
