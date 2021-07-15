package com.sp.fc.web.config;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.Locale;

public class CustomVoter implements AccessDecisionVoter<MethodInvocation>{

    AbstractSecurityInterceptor interceptor;
    private final String PREFIX = "SCHOOL_";

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute.getAttribute().startsWith(PREFIX);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MethodInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public int vote(Authentication authentication, MethodInvocation object, Collection<ConfigAttribute> attributes) {
        String role = attributes.stream().filter(a -> a.getAttribute().startsWith(PREFIX))
                .map(a -> a.getAttribute().substring(PREFIX.length()))
                .findFirst().get();

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + role.toUpperCase(Locale.ROOT)))) {
            return ACCESS_GRANTED;
        }
       return ACCESS_DENIED;
    }
}
