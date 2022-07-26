package com.example.RedisProject.ServiceImpl;

import com.example.RedisProject.Repository.UserRequestRepository;
import com.example.RedisProject.Service.QueueInterface;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.ArrayList;
import java.util.List;

@Validated
@Slf4j
@Service
@Component
public class Implementation<T> implements QueueInterface<T> {
    @Autowired
    private UserRequestRepository userRequestRepository;

    private static int front = -1, rear = -1;
    List<T> arrli;


    public Implementation() {
        arrli = new ArrayList();
    }

    @Override
    public synchronized void enque( T obj) {
        if (obj != null) {
            rear++;
            if (rear == 0) front = 0;
            arrli.add(rear, obj);
           // userRequestRepository.save(((UserRequest) obj));
        }

    }

    @Override
    public synchronized T deque() {  T obj = null;
        int temp1 = front;
        T temp2;
        if (rear == -1 || (arrli.get(rear) == null)) {
            return null;
        }

        if (rear == front) {
            temp2 = arrli.get(front);
            arrli.set(front, null);
            rear = -1;
            front = -1;
          //  userRequestRepository.deleteById(((UserRequest) temp2).getId());
//            obj = (T) (temp2).toString().replaceAll("null", " ")
//                    .replaceAll("@0@", "null").replaceAll("@", " ").trim();

            return temp2;
        }
        temp2 = arrli.get(front);
        front++;
        arrli.set(temp1, null);
       // userRequestRepository.deleteById(((UserRequest) temp2).getId());
//        obj = (T) (temp2).toString().replaceAll("null", " ")
//                .replaceAll("@0@", "").replaceAll("@", " ").trim();

        return temp2;
    }


    @Override
    public T peek() {
        if (rear < 0) {
            return null;
        } else if (arrli.get(rear) == null) return null;
        else
            return (T) arrli.get(rear).toString().replaceAll("null", "")
                    .replaceAll("@0@", "").replaceAll("@", " ").trim();
    }

    public int size() {
        if (rear == -1) return (0);
        else
            return rear - front + 1;
    }

    @SneakyThrows
    public List<T> display() {
        int j;
        List<T> list = new ArrayList<>();
        for (j = front; j <= rear; j++) {
            if (j != -1) {
                if (arrli.get(j) != null) {
                    list.add(arrli.get(j));
//                             .replaceAll("null", "")
//                            .replaceAll("@0@", "null").replaceAll("@", " ").trim());
                }
            }
        }
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        //return gson.toJson(this);
//        String jsonStr = gson.toJson(list);
        return   list;
    }

}