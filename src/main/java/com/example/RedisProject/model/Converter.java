package com.example.RedisProject.model;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("convert")
@Slf4j
@NoArgsConstructor
public class Converter {
    public UserRequestDto entityToDto(UserRequest userRequest) throws ParseException {
        String[] arrOfStr = userRequest.getUserName().split("_");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String strDate=userRequest.getDob().replaceAll("00:00:00 GMT","");
        Date date = sdf.parse(strDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new UserRequestDto(userRequest.getId(), arrOfStr[0],arrOfStr[1],
                userRequest.getAge(),cal, userRequest.getLocation());
    }

    public List<UserRequestDto> entityToDto(List<UserRequest> userRequest) {
            return userRequest.stream().map(x-> {
                        try {
                            return this.entityToDto(x);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
    }

    public UserRequest dtoToEntity(UserRequestDto userRequestDto) throws ParseException {
        String date = userRequestDto.getYear() + "-" + userRequestDto.getMonth() + "-" +userRequestDto.getDay();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar dob = Calendar.getInstance();
        dob.setTime(sdf.parse(date));
        userRequestDto.setDob(dob);
        UserRequestDto userRequestDto1 = new UserRequestDto(userRequestDto.getFirstName(), userRequestDto.getLastName(),
                userRequestDto.getLocation(), userRequestDto.getYear(), userRequestDto.getMonth(), userRequestDto.getDay(),
                Converter.calculateAge(userRequestDto.getDob()),dob);

        String name = userRequestDto.getFirstName().replaceAll("_", "-") + "_" +
                userRequestDto.getLastName().replaceAll("_", "-");

        return new UserRequest(name, userRequestDto1.getAge(), dob.getTime().toString().replaceAll("00:00:00 GMT","")
               , userRequestDto.getLocation());
    }
    public static int calculateAge(Calendar dob) {
        Calendar today = Calendar.getInstance();
        int curYear = today.get(Calendar.YEAR);
        int dobYear = dob.get(Calendar.YEAR);
        int age = curYear - dobYear;

        int curMonth = today.get(Calendar.MONTH);
        int dobMonth = dob.get(Calendar.MONTH);
        if (dobMonth > curMonth) {
            age--;
        } else if (dobMonth == curMonth) {
            int curDay = today.get(Calendar.DAY_OF_MONTH);
            int dobDay = dob.get(Calendar.DAY_OF_MONTH);
            if (dobDay > curDay) {
                age--;
            }
        }
        if(curYear<dobYear||(curYear==dobYear&&curMonth<dobMonth))
            return 0;

        return age;

    }

    public boolean checkDateAndName(UserRequestDto userRequestDto) throws ParseException {
        String date;
        if(userRequestDto.getYear()%4!=0&&userRequestDto.getMonth()==2&&userRequestDto.getDay()>=29)
        {
            log.error("not a leap yeAR....");
            return  false;
        }
        if(userRequestDto.getMonth() >9&&userRequestDto.getDay()>9)
            date = userRequestDto.getYear() + "-" + userRequestDto.getMonth() + "-" +userRequestDto.getDay();
        else if(userRequestDto.getMonth() <9&&userRequestDto.getDay()>9)
            date= userRequestDto.getYear() + "-0" + userRequestDto.getMonth() + "-" +userRequestDto.getDay();
        else if (userRequestDto.getMonth() >9&&userRequestDto.getDay()<9)
            date= userRequestDto.getYear() + "-" + userRequestDto.getMonth() + "-0" +userRequestDto.getDay();
        else  date= userRequestDto.getYear() + "-0" + userRequestDto.getMonth() + "-0" +userRequestDto.getDay();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return GenericValidator.isDate(sdf.format(sdf.parse(date)), "yyyy-MM-dd", true)
                && !Objects.equals(userRequestDto.getFirstName(), "null")&& !Objects.equals(userRequestDto.getLastName(), "null");
    }
}
