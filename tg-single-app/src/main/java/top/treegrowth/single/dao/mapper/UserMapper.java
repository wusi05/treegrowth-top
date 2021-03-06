package top.treegrowth.single.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.treegrowth.model.entity.User;

import java.util.List;


@Mapper
public interface UserMapper {

    void createUser(User user);

    void deleteUserBy(@Param("userId") String userId);

    void updateUser(User user);

    User getUserBy(@Param("userId") String userId);

    User getUserByPhone(@Param("phone") String phone);

    List<User> getUsers();
}
