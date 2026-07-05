package com.fantasy.fuojbackendjudgeservice.judge.codesandbox;

import com.fantasy.fuojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.fantasy.fuojbackendmodel.model.codesandbox.ExecuteCodeResponse;

public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param request
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest request);
}
