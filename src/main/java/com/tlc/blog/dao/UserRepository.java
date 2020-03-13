package com.tlc.blog.dao;

import com.tlc.blog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// User-操作的类型
// Long-主键类型

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserNameAndPassword(String userName, String password);

}
