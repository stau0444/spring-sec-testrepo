package com.sp.fc.web.config;

import com.sp.fc.web.domain.Paper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private MethodInvocation invocation;

    public CustomMethodSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        super(authentication);
        this.invocation = invocation;
    }

    public boolean isStudent(){
        return getAuthentication().getAuthorities().stream()
                .anyMatch(a-> a.getAuthority().equals("ROLE_STUDENT"));
    }
    public boolean notPrepareState(Paper paper){
        return paper.getState() != Paper.State.PREPARE;
    }
    @Override
    public Object getThis() {
        return this;
    }
}
