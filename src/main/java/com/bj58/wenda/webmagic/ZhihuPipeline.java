package com.bj58.wenda.webmagic;

import com.bj58.wenda.dao.CommentDAO;
import com.bj58.wenda.dao.QuestionDAO;
import com.bj58.wenda.dao.UserDAO;
import com.bj58.wenda.enums.EntityType;
import com.bj58.wenda.model.Comment;
import com.bj58.wenda.model.Question;
import com.bj58.wenda.model.User;
import com.bj58.wenda.service.CommentService;
import com.bj58.wenda.service.QuestionService;
import com.bj58.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author star
 * @date 2018/12/28 17:01
 */
@Component
public class ZhihuPipeline implements Pipeline {

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    public ZhihuPipeline() {

    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String url = resultItems.get("url");
        System.out.println(url);
        if(url == null || !url.matches("https://www\\.zhihu\\.com/question/\\d+")) {
            System.out.println("skip query page");
            return;
        }
        //Pattern urlPattern = Pattern.compile("https://www\\.zhihu\\.com/question/(.*?)");
//        Matcher urlMatcher = urlPattern.matcher(url);
//        if(urlMatcher.find()) {
//            System.out.println(urlMatcher.group(1));
//        }


        List<String> items = resultItems.get("items");
        if(items == null || items.isEmpty()) {
            System.out.println("null or empty item list");
            return;
        }
        String title = resultItems.get("title");
        String content = resultItems.get("content");
        Question question = new Question();
        question.setTitle(title);
        question.setContent(content);
        question.setCreatedDate(new Date());
        question.setCommentCount(items.size());
        question.setUserId((int)(Math.random()*userService.userCount()) + 2);
        int qid = questionService.addQuestion(question);

        for(String item:items) {
            Pattern pattern = Pattern.compile("<div.*? name=\"(.*?)\" .*?<meta itemProp=\"name\" content=\"(.*?)\">.*?" +
                    "<meta itemProp=\"image\" content=\"(.*?)\">.*?<span class=\"RichText ztext CopyrightRichText-richText\" " +
                    "itemprop=\"text\">(.*?)</span>.*?</div>", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(item);
            while(matcher.find()) {
                for(int i = 1; i <= matcher.groupCount(); i++) {
                    System.out.println(matcher.group(i));
                    String name = matcher.group(2);
                    String img = matcher.group(3);
                    String comment = matcher.group(4);
                    User user = userService.selectByName(name);
                    int uid;
                    if(user == null) {
                        User newUser = new User();
                        newUser.setName(name);
                        newUser.setHeadUrl(img);
                        uid = userService.addUser(newUser);
                    } else {
                        uid = user.getId();
                    }

                    Comment newComment = new Comment();
                    newComment.setUserId(uid);
                    newComment.setContent(comment);
                    newComment.setCreatedDate(new Date());
                    newComment.setEntityId(qid);
                    newComment.setEntityType(EntityType.ENTITY_QUESTION.getCode());
                    commentService.addComment(newComment);
                }
            }
            //System.out.println(item);
        }
    }
}
