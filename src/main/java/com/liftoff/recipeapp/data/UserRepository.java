package com.liftoff.recipeapp.data;

import com.liftoff.recipeapp.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);

}
