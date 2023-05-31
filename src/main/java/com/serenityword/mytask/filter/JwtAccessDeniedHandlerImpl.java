package com.serenityword.mytask.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public interface JwtAccessDeniedHandlerImpl {
    void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException;
}
