package com.example.RedisProject.model;
import lombok.*;


import javax.persistence.Id;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Calendar;

@NoArgsConstructor
@Getter
@Setter
@Data
public class UserRequestDto implements Serializable {


    @NotEmpty
    @Id
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    @NotNull(message = "loc cannot be null")
    private String location;
    @Min(1700)
    int year;
    @Max(12)
    @Min(1)
    int month;
    @Max(31)
    @Min(1)
    int day;
    private int age = 0;

    Calendar dob;

    private Long id;



    @Override
    public String toString() {
        return "UserRequestDto{" +
                ", FirstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", location='" + location + '\'' +
                '}';
    }



    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public UserRequestDto(String firstName,String lastName, String location,  int year, int month,int day, int age, Calendar dob) {
        this.firstName = firstName;
        this.lastName=lastName;
        this.age = age;
        this.location = location;
        this.day = day;
        this.year = year;
        this.month = month;
    }
    public UserRequestDto(Long id,String firstName,String lastName,  int age, @NotEmpty Calendar dob, String location) {
        this.id=id;
        this.firstName = firstName;
        this.lastName=lastName;
        this.age=age;
        this.dob=dob;
        this.location = location;

    }

}
