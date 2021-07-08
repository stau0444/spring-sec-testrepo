package com.sp.fc.web.controller;

import org.springframework.security.access.AccessDeniedException;

public class CanNotAccessUserPage extends AccessDeniedException {

    public CanNotAccessUserPage(String msg) {
        super(msg);
    }
}
