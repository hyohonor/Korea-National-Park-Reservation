package com.reservation.knpr2211.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservation.knpr2211.notification.entity.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,String>{

}

