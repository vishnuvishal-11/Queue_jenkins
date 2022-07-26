package com.example.RedisProject.controller;

import com.example.RedisProject.Service.QueueInterface;
import lombok.extern.slf4j.Slf4j;
import com.example.RedisProject.model.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component("customqueue")
public class CustomQueue implements QueueSelector {


    public CustomQueue(QueueInterface queueInterface) {
       this.queueInterface=queueInterface;
    }

  //  @Autowired
   private final QueueInterface queueInterface;
    @Override
    public String enque(UserRequest userRequest) {
        queueInterface.enque(userRequest);
        return "inserted";

    }

    @Override
    public Object deque() throws NullPointerException {
        Object deletedElement =  queueInterface.deque();
        if (deletedElement == null ) {
            return "empty queue";
        } else {
            log.info(" customQ - deque has been Accessed ...");
            return deletedElement;
        }
    }

    @Override
    public int size() {
        log.info(" customQ - size has been Accessed ...");
        return queueInterface.size();
    }
}
