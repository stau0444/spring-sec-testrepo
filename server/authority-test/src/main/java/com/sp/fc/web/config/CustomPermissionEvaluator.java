package com.sp.fc.web.config;

import com.sp.fc.web.domain.Paper;
import com.sp.fc.web.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final PaperService paperService;


    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {



        Paper paper = paperService.getPaper((Long)targetId);

        if (paper == null){
            throw new AccessDeniedException("시험지 없음");
        }

        if (isStudent(authentication) && !inPreparation(paper)){
            return isMatch(paper,authentication);
        }

        if(isTeacher(authentication)){
            return true;
        }
        return false;
    }


    public boolean isTeacher(Authentication authentication){
       return authentication.getAuthorities().stream()
                .anyMatch(a->a.getAuthority().equals("ROLE_TUTOR"));
    }
    public boolean isStudent(Authentication authentication){
        return authentication.getAuthorities().stream()
                .anyMatch(a->a.getAuthority().equals("ROLE_STUDENT"));
    }
    public boolean inPreparation(Paper paper){
        return paper.getState() == Paper.State.PREPARE;
    }
    public boolean isMatch(Paper paper,Authentication authentication){
        return paper.getStudentIds().stream().anyMatch(id -> id.equals(authentication.getName()));
    }
}
