package com.example.RedisProject.controller;
import com.example.RedisProject.model.UserRequestDto;
import com.example.RedisProject.Service.QueueInterface;
import com.example.RedisProject.model.Converter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.example.RedisProject.model.UserRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/queue")
@Slf4j
public class QueueClass {
  //  @Autowired
    private final QueueInterface queueInterface;


    public QueueClass( @Qualifier("customqueue")QueueSelector queuecustom, @Qualifier("rabbitq") QueueSelector queuerabbit,@Qualifier("convert") Converter converter,@Qualifier("impl")QueueInterface queueInterface) {
     this.converter=converter;
        this.queuecustom = queuecustom;
        this.queuerabbit = queuerabbit;
        this.queueInterface=queueInterface;
    }

    public void setQueuestring(String queuestring) {
        this.queuestring = queuestring;
    }
    String queuestring;

    @Value("${dynamic.queue}")
    private  String queuetype;
//    @Autowired
//    @Qualifier("customqueue")
    private final QueueSelector queuecustom;
//    @Autowired
//    @Qualifier("rabbitq")
    private final QueueSelector queuerabbit;

    @Autowired
    private RabbitTemplate rabbitTemplate;
//    @Autowired
    private final Converter converter;


    @GetMapping("/display")
    public List<UserRequestDto> display() {
        queuestring=queuetype;
        List<UserRequest> findAll = queueInterface.display();
        return converter.entityToDto(findAll);
    }

    @GetMapping("/size")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public int size() {
        if(queuetype!=null)
        queuestring=queuetype;
        int size = 0;
        if (queuestring.equalsIgnoreCase("rabbit")) {
            size = queuerabbit.size();
        } else {
            size = queuecustom.size();
        }
        return size;
    }

    @PostMapping(value = "/enque", produces = {MediaType.APPLICATION_JSON_VALUE})
    @SneakyThrows
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String enque(@Valid @RequestBody UserRequestDto userRequestDto) {
     //   if (converter.checkDateAndName(userRequestDto)) {
        queuestring=queuetype;
        log.info("1 "+queuestring);
        log.info("2 "+queuetype);
            UserRequest userRequest= converter.dtoToEntity(userRequestDto);
            if (queuestring.equalsIgnoreCase("rabbit")) {
                return  queuerabbit.enque(userRequest);
               // userRequest.getId();
            } else {
                return  queuecustom.enque(userRequest);
              //  return userRequest.getId();
            }
    }

    @DeleteMapping("/deque")
    public UserRequestDto deque() {
        Object deque = null;
        queuestring=queuetype;
        try {
            if (queuestring.equalsIgnoreCase("rabbit")) {
                deque = queuerabbit.deque();
            } else {
                deque = queuecustom.deque();
            }
            return converter.entityToDto((UserRequest) deque);
        } catch (Exception e) {
            return new UserRequestDto();
        }
    }
}
