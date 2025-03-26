package com.marsol.domain.handler;

import com.marsol.application.dto.ArticleDTO;
import com.marsol.domain.model.PLU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseHandler {
    protected BaseHandler nextHandler;
    protected static final Logger logger = LoggerFactory.getLogger(BaseHandler.class);

    public BaseHandler setNext(BaseHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public abstract void handle(PLU plu, ArticleDTO articleDTO);
}
