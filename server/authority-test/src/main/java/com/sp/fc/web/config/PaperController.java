package com.sp.fc.web.config;

import com.sp.fc.web.domain.Paper;
import com.sp.fc.web.service.PaperService;
import lombok.RequiredArgsConstructor;
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

    @PreAuthorize("isStudent()")
    @GetMapping("/paper/my-papers")
    public List<Paper> getPapers(@AuthenticationPrincipal User user){
        return paperService.getPapers(user.getUsername());
    }

}
