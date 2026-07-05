package com.fantasy.fuojbackendjudgeservice.judge;

import com.fantasy.fuojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.fantasy.fuojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.fantasy.fuojbackendjudgeservice.judge.strategy.JudgeContext;
import com.fantasy.fuojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.fantasy.fuojbackendmodel.model.codesandbox.JudgeInfo;
import com.fantasy.fuojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
