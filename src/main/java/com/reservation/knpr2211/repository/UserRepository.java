package com.reservation.knpr2211.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservation.knpr2211.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, String>{

		User findByid(String id);
		
	
}
