package com.flashsale.excception;

import com.flashsale.result.CodeMsg;

public class ForwardException extends GlobalException {
    public ForwardException(CodeMsg error) {
        super(error);
    }
}
