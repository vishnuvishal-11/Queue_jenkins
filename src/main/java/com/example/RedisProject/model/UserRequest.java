package com.example.RedisProject.model;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Validated
@RedisHash("items")
@Entity
public class UserRequest implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @NotNull(message = "name cannot be null")
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    String userName;

    int age = 0;

    @NotEmpty
    String dob;


    public UserRequest(String userName, int age, String dob, @NonNull String location) {
        this.userName = userName;
        this.age = age;
        this.dob = dob;
        this.location = location;
    }

    @NotEmpty
    @NonNull
    String location;

    @Override
    public String toString() {
        return "UserRequest{ " +
                " id= " + id +
                ", userName= " + userName  +
                ", age= " + age +
                ", dob= " + dob  +
                ", location= " + location  +
                " }";
    }




}



