package com.example.demo.dao;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT first_name from users WHERE mobile_number = (:mobile_number) ",nativeQuery = true)
    Collection<String> isMobileNumberPresent(@Param("mobile_number") String mobileNumber);

    @Modifying
    @Transactional
    @Query(value = "update users set  email =(:email), user_password =(:user_password), first_name =(:first_name), last_name =(:last_name)   WHERE mobile_number = (:mobile_number) ",nativeQuery = true)
    void updateUser(@Param("email") String email,@Param("user_password") String userPassword,@Param("first_name") String firstName,@Param("last_name") String lastName,@Param("mobile_number") String mobileNumber);


    @Query(value = "SELECT first_name FROM users WHERE user_no = (:user_no) ",nativeQuery = true)
    Collection<String> isUserPresent(@Param("user_no")Integer userNo);

    @Query(value = "select CONCAT(first_name,' ',last_name) from users where user_no =(:user_no)",nativeQuery = true)
    Collection<String> nameTeller(@Param("user_no")Integer userNo);

    @Query(value = "SELECT user_no FROM users WHERE mobile_number = (:mobile_number) ",nativeQuery = true)
    Collection<Integer> userNoFetcher(@Param("mobile_number") String mobileNumber);

    @Query(value = "SELECT created FROM users WHERE mobile_number = (:mobile_number) ",nativeQuery = true)
    Collection<String> createdFetcher(@Param("mobile_number") String mobileNumber);
}