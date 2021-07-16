package com.sp.fc.web;


import com.sp.fc.web.paper.Paper;
import com.sp.fc.web.paper.PaperRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AuthorityACLApplication.class)
public class CacheTest {

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private CacheManager cacheManager;

    public Optional<Paper> getPaper(Long id){
        return Optional.ofNullable(cacheManager.getCache("papers").get(id,Paper.class));
    }

    @DisplayName("1.조회한 Paper는 캐시에서 가져와야 한다. ")
    @Test
    void test_1(){
        EhCacheCacheManager manager;
        Paper paper1 = Paper.builder().id(1L).title("paper1").build();
        paperRepository.save(paper1);

        assertEquals(Optional.empty(),getPaper(paper1.getId()));
        Optional<Paper> paperById = paperRepository.findById(paper1.getId());

        assertTrue(getPaper(1L).isPresent());

    }

}
