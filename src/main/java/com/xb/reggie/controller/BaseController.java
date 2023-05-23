package com.xb.reggie.controller;

import jakarta.servlet.http.HttpSession;

public class BaseController {
    protected final Long getUidFromSession(HttpSession session){
        return Long.parseLong(session.getAttribute("id").toString());
    }
    protected final String getUnameFromSession(HttpSession session){
        return session.getAttribute("username").toString();
    }
}
