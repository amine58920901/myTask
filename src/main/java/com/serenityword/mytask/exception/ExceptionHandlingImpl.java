package com.serenityword.mytask.exception;

import org.springframework.boot.web.servlet.error.ErrorController;

public interface ExceptionHandlingImpl extends ErrorController {
    String getErrorPath();
}
