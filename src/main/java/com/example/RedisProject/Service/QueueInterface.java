
package com.example.RedisProject.Service;

import java.util.List;

public interface QueueInterface<T> {

 void  enque(T obj);



    T deque();
    T peek();
    List<T> display();
    int size();

}
