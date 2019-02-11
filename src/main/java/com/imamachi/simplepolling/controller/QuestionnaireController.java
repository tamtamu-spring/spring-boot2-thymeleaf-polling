package com.imamachi.simplepolling.controller;

import com.imamachi.simplepolling.form.ResultRootForm;
import com.imamachi.simplepolling.model.CurrentQuestionnaire;
import com.imamachi.simplepolling.model.Question;
import com.imamachi.simplepolling.service.QuestionService;
import com.imamachi.simplepolling.service.QuestionnaireService;
import com.imamachi.simplepolling.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    private QuestionnaireService questionnaireService;
    private QuestionService questionService;
    private ResultService resultService;

    @Autowired
    public QuestionnaireController(QuestionnaireService questionnaireService,
                                   QuestionService questionService,
                                   ResultService resultService){
        this.questionnaireService = questionnaireService;
        this.questionService = questionService;
        this.resultService = resultService;
    }

    // アンケートトップページ
    @GetMapping("/top")
    public String getTopPage(Model model){
        // アンケートの情報を取得
        CurrentQuestionnaire currentQuestionnaire = this.questionnaireService.getCurrentQuestionnaire();

        // アンケートのタイトルを取得する
        model.addAttribute("title", currentQuestionnaire.getQuestionnaire().getTitle());
        return "/questionnaire/top";
    }

    // アンケートフォームへの遷移
    @PostMapping("/top")
    public String getForm(@RequestParam(name = "title") String title,
                          @RequestParam(name = "username") String username,
                          Model model){
        model.addAttribute("title", title);
        model.addAttribute("username", username);

        List<Question> questionList = questionService.getQuestionnaireInfo();
        model.addAttribute("questionList", questionList);
        model.addAttribute("resultRootForm", new ResultRootForm());

        return "/questionnaire/form";
    }

    // アンケートフォームの送信
    @PostMapping("/form")
    public String postFrom(@Validated @ModelAttribute ResultRootForm resultRootForm,
                           Model model){
        // 空欄チェック
        if(resultService.existFormError((resultRootForm))){
            model.addAttribute("title", resultRootForm.getQuestionnaireTitle());
            model.addAttribute("username", resultRootForm.getUsername());

            List<Question> questionList = questionService.getQuestionnaireInfo();
            model.addAttribute("questionList", questionList);
            model.addAttribute("resultRootForm", resultRootForm);
            return "/questionnaire/form";
        }

        // アンケート結果の登録
        System.out.println(resultRootForm.getQuestionnaireTitle());
        return "/questionnaire/submitted";
    }
}
