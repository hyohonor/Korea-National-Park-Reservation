package com.reservation.knpr2211.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reservation.knpr2211.dto.PlaceDTO;
import com.reservation.knpr2211.entity.Place;
import com.reservation.knpr2211.service.MountainCodeService;
import com.reservation.knpr2211.service.ReservationService;

@Controller
public class ReservationController {
	@Autowired
	ReservationService rs;
	
	// 예약(예외처리)
	@RequestMapping( value = "reservation")
	public String reservation() {
		return "reservation/campsite";
	}

	// 생태탐방원 예약
	@GetMapping(value = "ecoReservation")
	public String GetEcoReservation(String category, Model model) {
		if (category == null) {
			category = "C08";
		}
		// 제목 category1(대분류), category2(중분류) 코드 해석
		String[] result = rs.transtitleCode(category); 
		model.addAttribute("category1", result[0]);
		model.addAttribute("category2", result[1]);
		// 룸타입 가져오기
		List<PlaceDTO> roomTypeList = rs.selectEcoRoomType(category);
		model.addAttribute("roomTypeList", roomTypeList);
		return "reservation/ecoReservation";
	}
	@ResponseBody
	@PostMapping(value="ecoReservation", produces="application/json; charset=UTF-8")
	public String PostEcoReservation(@RequestBody(required = false) String code){
		String result = rs.selectCategory3(code).getPriceDay();
		return result;
	}
	
	// 민박촌 예약
	@GetMapping(value = "cottageReservation")
	public String getCottageReservation(String category, Model model) {
		System.out.println(category);
		if (category == null) {
			category = "D01";
		}
		// category2(중분류) 코드해석
		String[] result = rs.transtitleCode(category); 
		model.addAttribute("category1", result[0]);
		model.addAttribute("category2", result[1]);
		// 룸타입 가져오기
		List<PlaceDTO> roomTypeList = rs.selectCotRoomType(category);
		model.addAttribute("roomTypeList", roomTypeList);
		return "reservation/cottageReservation";
	}
	@PostMapping(value = "cottageReservation", produces = "text/html; charset=UTF-8")
	public String postCottageReservation(@RequestBody(required = false) HashMap<String, String> keyData) {
		rs.mol(keyData.get("category3"), keyData.get("startDay"), keyData.get("endDay"));
		return "reservation/cottageReservation";
	}

	
	
	
	// (시작)작성자: 최현하 ==============================================

	// 야영장 예약페이지 열림
	@RequestMapping("campsite")
	public String campsite() {
		return "reservation/campsite";
	}

	// 대피소 예약페이지 열림
	@RequestMapping("shelter")
	public String shelter() {
		return "reservation/shelter";
	}

	
	//야영장 산이름-지역명 클릭하면 테이블뷰 출력
	@ResponseBody
	@PostMapping(value = "campsiteView")
	public Map<String, Object> campsiteView(@RequestParam Map<String, String> map) throws Exception {
		
		Map<String, Object> result = new HashMap<>();
		String code = map.get("code");
		
		List<PlaceDTO> list = rs.campsiteView(code);
		List<String> checkList = rs.checkBoxList(code);
		//System.out.println(list.toString());

		result.put("list", list);
		result.put("checkList", checkList);
		return result;
	}
	
	
	//야영장 산이름-지역명-야영장명으로 클릭하면 방 출력
	@ResponseBody
	@PostMapping(value = "roomView")
	public Map<String, Object> roomView(@RequestParam Map<String, String> map) throws Exception {
		
		String code = map.get("code");
	
		List<PlaceDTO> rooms = rs.roomView(code);
		
		Map<String, Object> result = new HashMap<>();
		result.put("rooms", rooms);
		
		return result;
	}
	
	
	//야영장 산이름-지역명-야영장명-방번호 배열로 현재 예약 현황 조회
	@ResponseBody
	@PostMapping(value = "reservationState")    
    public Map<String, Object> reservationState(@RequestParam String[] rooms) {
		Map<String, Object> map = rs.reservationState(rooms);
		
		@SuppressWarnings("unchecked")
		List<String> reservations = (List<String>) map.get("reservations");
		@SuppressWarnings("unchecked")
		List<String> roomList =  (List<String>) map.get("roomList");
		@SuppressWarnings("unchecked")
		List<String> dateList = (List<String>) map.get("dateList");
		//데이터 전송, 응답에 문제없음을 확인
		
		//List<String> reservations = rs.reservationState(rooms);
		String roomMax = rs.campsiteCount(rooms[0]);
		
		Map<String, Object> result = new HashMap<>();
		result.put("reservations", reservations);
		result.put("roomList", roomList);
		result.put("dateList", dateList);
		result.put("roomMax", roomMax);
		
        
        return result;
    }
	
	
	//야영장 1박2일 가능한지 조회
	@ResponseBody
	@PostMapping(value = "oneNightCheck" )	
	public Map<String, Object> oneNightCheck(@RequestParam Map<String, Object> map) {
		
		Map<String, Object> result = new HashMap<>();
		
		String room = (String)map.get("room"); 
	    String date = (String)map.get("date");
 
	    Map<String, String>dateList = rs.oneNightCheck(room, date);
	        
		result.put("dateList", dateList);
		return result;
	}
	
	
	//야영장 선택시, 예약이 가능할 경우, 선택한 장소 정보 출력
	@ResponseBody
	@PostMapping(value = "inputSelectInfo" )	
	public Map<String, Object> inputSelectInfo(@RequestParam String room) {
		
		Map<String, Object> result = new HashMap<>();
		
		Map<String, String> infoMap = rs.inputSelectInfo(room);
		
		result.put("infoMap", infoMap);
		return result;
		
	}
	
	
	//사용자 입력받은 예약 일정 service로 전달
	@ResponseBody
	@PostMapping(value = "reservationSave" )	
	public String reservationSave(@RequestParam Map<String, Object> map) {
 
	    String result = rs.reservationSave(map);
	        
		return result;
	}
	


	
	
	// (끝)작성자: 최현하 ==============================================
	
	
}
