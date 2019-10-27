package com.flashsale.excception;

import com.flashsale.result.CodeMsg;

public class GlobalException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public GlobalException(CodeMsg error) {
        super(error.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }

}
