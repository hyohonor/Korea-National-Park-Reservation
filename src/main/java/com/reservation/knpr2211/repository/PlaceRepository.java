
package com.reservation.knpr2211.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reservation.knpr2211.dto.PlaceDTO;
import com.reservation.knpr2211.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {
	List<Place> findAll();


	List<Place> findByCategory1AndCategory2AndCategory3(String category1, String category2, String category3); 
	List<Place> findByCategory1AndCategory2AndCategory3AndCategory4(String category1, String category2, String category3, String category4); 


	//List<Place> findByCategory2AndCategory3(String category2, String category3); 
	

	
	ArrayList<Place> findByCategory2(String parkId);
	ArrayList<Place> findByCategory3(String parkId);
	
	
	@Query(nativeQuery=true, value = "select distinct category2 from place where category1 = ?1")
	ArrayList<String> findDistintCategory2(String category1);
	
	@Query(nativeQuery=true, value = "select distinct category3 from place where category2 = ?1")
	ArrayList<String> findDistintCategory3(String category2);

	@Query(nativeQuery=true, value = "select * from place where category2 like CONCAT('%',:category2,'%')")
	ArrayList<Place> find(@Param("category2") String category2);
	
	List<PlaceDTO> findAllByCategory2(String category2);
	
}
