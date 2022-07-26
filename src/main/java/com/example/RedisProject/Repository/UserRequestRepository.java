package com.example.RedisProject.Repository;

import com.example.RedisProject.model.UserRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRequestRepository extends CrudRepository<UserRequest,Long> {

//void saveObj(UserRequest userRequest);

}
