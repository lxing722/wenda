package com.bj58.wenda.async;

import java.util.List;

public interface EventHandler {

    public void doHandle(EventModel model);

    List<EventType> getSupportEventTypes();
}
