package com.fantasy.fuojbackendserviceclient.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fantasy.fuojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.fantasy.fuojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.fantasy.fuojbackendmodel.model.entity.QuestionSubmit;
import com.fantasy.fuojbackendmodel.model.entity.User;
import com.fantasy.fuojbackendmodel.model.vo.QuestionSubmitVO;

/**
 * @author Fantasy
 * @description 针对表【question_submit(题目提交)】的数据库操作Service
 * @createDate 2026-06-07 23:05:51
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest 题目提交信息
     * @param loginUser                登录用户
     * @return 提交记录的 id
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
