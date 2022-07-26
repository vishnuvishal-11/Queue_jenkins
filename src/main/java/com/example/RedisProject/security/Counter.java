package com.example.RedisProject.security;

import lombok.Data;
import lombok.Getter;
import java.io.Serializable;
import java.util.Date;


@Data
@Getter
public class Counter implements Serializable {
    private int number;
    private Date targetDate;


    public Counter setCounter(int number, Date targetDate) {
        this.number = number;
        this.targetDate = targetDate;
        return null;
    }


}

