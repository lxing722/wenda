package com.bj58.wenda;

import com.bj58.wenda.dao.QuestionDAO;
import com.bj58.wenda.dao.UserDAO;
import com.bj58.wenda.model.Question;
import com.bj58.wenda.model.User;
import com.bj58.wenda.model.ViewObject;
import com.bj58.wenda.service.QuestionService;
import com.bj58.wenda.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WendaApplication.class)

public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Test
    public void contextLoads() {
//        Random random = new Random();
 //       for (int i = 0; i < 11; ++i) {
//            User user = new User();
//            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
//            user.setName(String.format("USER%d", i));
//            user.setPassword("");
//            user.setSalt("");
//            userDAO.addUser(user);
//
////            user.setPassword("newpassword");
////            user.setId(1);
////            userDAO.updatePassword(user);
//
//            Question question = new Question();
//            question.setCommentCount(i);
//            Date date = new Date();
//            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
//            question.setCreatedDate(date);
//            question.setUserId(i + 1);
//            question.setTitle(String.format("TITLE{%d}", i));
//            question.setContent(String.format("Balaababalalalal Content %d", i));
//            questionDAO.addQuestion(question);
//
//        }

//        Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());
//        userDAO.deleteById(1);
//        Assert.assertNull(userDAO.selectById(1));
        //System.out.println(questionDAO.selectLatestQuestions(0, 0, 10));
        List<Question> questionList = questionService.getLatestQuestions(0, 0, 10);
        List<ViewObject> vos = new ArrayList<>();
        for(Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        System.out.println(vos);
    }
}
