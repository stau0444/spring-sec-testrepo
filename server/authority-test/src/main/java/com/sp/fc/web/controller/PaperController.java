package com.sp.fc.web.controller;

import com.sp.fc.web.domain.Paper;
import com.sp.fc.web.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;


    @PreAuthorize("hasPermission(#paperId,'paper','read')")
    @GetMapping("/paper/{paperId}")
    public Paper getPaper(@AuthenticationPrincipal User user,@PathVariable Long paperId){
        return paperService.getPaper(paperId);
    }

    //@PreAuthorize("isStudent()")
    @PostFilter("notPrepareState(filterObject) && filterObject.studentIds.contains(#user.username)")
    @GetMapping("/paper/my-papers")
    public List<Paper> getPapers(@AuthenticationPrincipal User user){
        return paperService.getPapers(user.getUsername());
    }

    @Secured({"SCHOOL_PRIMARY"})
    @GetMapping("/paper/papers-primary")
    public List<Paper> getPapersByPrimary(@AuthenticationPrincipal User user){
        return paperService.getPapersByPrimary();
    }

}
