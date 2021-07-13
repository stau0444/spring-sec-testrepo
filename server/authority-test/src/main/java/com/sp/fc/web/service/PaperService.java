package com.sp.fc.web.service;

import com.sp.fc.web.domain.Paper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaperService implements InitializingBean {

   HashMap<Long,Paper> papers = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Paper paper1 = Paper.builder()
                .paperId(1L)
                .title("컴퓨터시험")
                .tutorId("tutor1")
                .studentIds(List.of("student1", "student2"))
                .state(Paper.State.PREPARE)
                .build();

        Paper paper2 = Paper.builder()
                .paperId(2L)
                .title("수학시험")
                .tutorId("tutor1")
                .studentIds(List.of("student2"))
                .state(Paper.State.PREPARE)
                .build();
        papers.putAll(Map.of(1L,paper1,2L,paper2));
    }

    public void setPaper(Paper paper){
        papers.put(paper.getPaperId(),paper);
    }

    public List<Paper> getPapers(String username){
        return papers.values().stream()
                .filter(paper -> paper.getStudentIds().contains(username))
                .collect(Collectors.toList());
    }

    public Paper getPaper(Long paperId){
        return papers.get(paperId);
    }

}
