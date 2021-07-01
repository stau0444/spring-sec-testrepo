package com.sp.fc.web.teacher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sp.fc.web.student.Student;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    private String id;
    private String username;
    @JsonIgnore
    private Set<GrantedAuthority> role;

}
