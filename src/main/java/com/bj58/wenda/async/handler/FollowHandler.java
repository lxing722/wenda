package com.bj58.wenda.async.handler;

import com.bj58.wenda.async.EventHandler;
import com.bj58.wenda.async.EventModel;
import com.bj58.wenda.async.EventType;
import com.bj58.wenda.enums.EntityType;
import com.bj58.wenda.model.Message;
import com.bj58.wenda.model.User;
import com.bj58.wenda.service.MessageService;
import com.bj58.wenda.service.UserService;
import com.bj58.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class FollowHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        if(user == null) {
            System.out.printf("null user, id:%d\n", model.getActorId());
        }

        if (model.getEntityType() == EntityType.ENTITY_QUESTION.getCode()) {
            message.setContent("用户" + user.getName()
                    + "关注了你的问题,http://127.0.0.1:8080/question/" + model.getEntityId());
        } else if (model.getEntityType() == EntityType.ENTITY_USER.getCode()) {
            message.setContent("用户" + user.getName()
                    + "关注了你,http://127.0.0.1:8080/user/" + model.getActorId());
        }

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
