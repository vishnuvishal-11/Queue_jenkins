package com.example.RedisProject.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.RedisProject.security.AccessList;


@Repository
public interface AccessListRepository extends CrudRepository<AccessList,String> {

}
