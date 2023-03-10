package com.reservation.knpr2211.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.reservation.knpr2211.dto.ReservationDTO;
import com.reservation.knpr2211.entity.Reservation;
import com.reservation.knpr2211.entity.User;
import com.reservation.knpr2211.repository.ReservationRepository;
import com.reservation.knpr2211.repository.UserRepository;

@Service
public class AdminMemberListService {
	@Autowired
	MountainCodeService mcs;
	@Autowired
	UserRepository ur;
	@Autowired
	ReservationRepository rr;
	@Autowired
	HttpSession session;
	

	public String paging(Model model, Integer page, Integer size, String member, String select, String search) {

		if (page == null || size == null) {
			page = 0;
			size = 10;
		}
		if (member == null || member.isEmpty()) {
			member = "all";
		}
		if (select == null || select.isEmpty()) {
			select = "id";
		}
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<User> result = ur.findAll(pageRequest);

		if (search == null || search.isEmpty()) {
			if (member.equals("all")) {
				result = ur.findByDeleted(false,pageRequest);
			} else {
				result = ur.findByMemberAndDeleted(member,false,pageRequest);
			}

		} else {
			if (member.equals("all")) {
				if (select.equals("id")) {
					result = ur.findByDeletedAndIdContaining(false,search, pageRequest);
				} else if (select.equals("name")) {
					result = ur.findByDeletedAndNameContaining(false,search, pageRequest);
				} else if (select.equals("email")) {
					result = ur.findByDeletedAndEmailContaining(false,search, pageRequest);
				} else {
					result = ur.findByDeletedAndMobileContaining(false,search, pageRequest);
				}
			} else {
				if (select.equals("id")) {
					System.out.println("durldyrl" + member + select + search);
					result = ur.findByDeletedAndMemberAndIdContaining(false,member, search, pageRequest);
				} else if (select.equals("name")) {
					result = ur.findByDeletedAndMemberAndNameContaining(false,member, search, pageRequest);
				} else if (select.equals("email")) {
					result = ur.findByDeletedAndMemberAndEmailContaining(false,member, search, pageRequest);
				} else {
					result = ur.findByDeletedAndMemberAndMobileContaining(false,member, search, pageRequest);

				}
			}
		}

		// Page<User> result = ur.findByMember("normal", pageRequest);

		List<User> members = result.getContent();

		int totalPage = result.getTotalPages();
		if (totalPage == 0) {
			totalPage = 1;
		}
		long totalElement = result.getTotalElements();

		System.out.println(members);
		for (User user : members) {
			System.out.println(user.getId());
		}

		model.addAttribute("members", members);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("totalElement", totalElement);
		model.addAttribute("search", search);
		model.addAttribute("member", member);
		model.addAttribute("select", select);

		return "ok";

	}

	public String memberModify(Model model, String memberId) {
		
			model.addAttribute("member", ur.getByid(memberId));
			return "?????? ????????????";
		
	}


	public void memberModify(Model model,String id, String name, String email, String mobile, String member,String deleted) {
		
		User user = User.builder().id(id).name(name).email(email).mobile(mobile).member(member).deleted(false).build();
		ur.save(user);
		
	}

	public void memberDelete(Model model,String id, String name, String email, String mobile, String member,String deleted) {
		User user = User.builder().id(id).name(name).email(email).mobile(mobile).member(member).deleted(true).build();
		ur.save(user);
		
	}

	public String adminMemberResedrvation(Model model, Integer page, Integer size, String memberId) {
		
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Reservation> result = rr.findByid(memberId, pageRequest);
		
		List<Reservation> reservations = result.getContent();
		int totalPage = result.getTotalPages();
		if (totalPage == 0) {
			totalPage = 1;
		}
		model.addAttribute("reservations", reservations);
		model.addAttribute("totalPage", totalPage);
		
		return null;
	}

	public String adminReservationModify(Model model, String memberId, String reserve, Integer page, Integer size, RedirectAttributes redirectAttrs) {
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Reservation> result = null;
		if(reserve.equals("future")) {
			result = rr.findByIdAndEndDayIsBefore(memberId,timestamp, pageRequest);
			
			
		}else if(reserve.equals("past")) {
			result = rr.findByIdAndEndDayIsAfter(memberId,timestamp, pageRequest);
		}
		if(result.isEmpty()) {
			redirectAttrs.addFlashAttribute("msg","???????????????????????????.");
			return "redirect:adminMemberList?page=0&size=10";
		}
		List<Reservation> reservations = result.getContent();
		int totalPage = result.getTotalPages();
		if(totalPage == 0) {
			totalPage = 1;
		}
		ArrayList<ReservationDTO> rds = new ArrayList<ReservationDTO>(); 

		for(Reservation r : reservations) {
			ReservationDTO rd = reserve(r);
			rds.add(rd);
		}
		
		model.addAttribute("reservations", rds);
		model.addAttribute("totalPage", totalPage);
	
		
		return "admin/adminReservationModify";
	}
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd[E]");
	SimpleDateFormat orderFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	String[] nights = {"1???2???","2??? 3???","3??? 4???"}; 
	public ReservationDTO reserve(Reservation r) {
		ReservationDTO rd = new ReservationDTO();
		rd.setSeq(r.getSeq());
		rd.setId(r.getId());
		rd.setCategory1(r.getCategory1());
		rd.setNameCategory1(mcs.findCategory(r.getCategory1()));
		rd.setCategory2(r.getCategory2());
		rd.setNameCategory2(mcs.findCategory(r.getCategory2()));
		rd.setCategory3(r.getCategory3());
		rd.setNameCategory3(mcs.findCategory(r.getCategory3()));
		if(r.getCategory4()==null) {
			rd.setCategory4(" ");
			rd.setNameCategory4(" ");
		}else {
			rd.setCategory4(r.getCategory4());
			rd.setNameCategory4(mcs.findCategory(r.getCategory4()));
		}
		if(r.getRoom()==null) {
			rd.setRoom(" ");
		}else rd.setRoom("- "+r.getRoom().substring(7,9));
		
		rd.setPeriod(format.format(r.getStartDay()) + "~" + format.format(r.getEndDay())+nights[Integer.parseInt(r.getAllDay())]);
	
		rd.setOrderTime(orderFormat.format(r.getOrderTime()));
		rd.setStartDay(r.getStartDay());
		
		rd.setEndDay(r.getEndDay());
		rd.setPeople(r.getPeople());
		rd.setAllDay(r.getAllDay());
		rd.setPrice(r.getPrice());
		if(r.getChecked()) {
			rd.setChecked("????????????");
		}else rd.setChecked("?????????");
		if(r.getStatus().equals("reserve")) {
			rd.setStatus("?????????");
		}else if(r.getStatus().equals("pay")) {
			rd.setStatus("????????????");
		}else if(r.getStatus().equals("cancle")) {
			rd.setStatus("??????");
		}else if(r.getStatus().equals("refund")) {
			rd.setStatus("??????");
		}
		
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(r.getStartDay().after(now)) {
			rd.setIsDone(false);
		}else rd.setIsDone(true);
		return rd;
	}

	public String adminReservationDetail(Model model, String memberId, Integer seq, RedirectAttributes redirectAttrs) {
		
		User user = ur.findByid(memberId);
		Reservation reservation = rr.findBySeqAndId(seq,memberId);
		if(reservation == null) {
			System.out.println("reservation"+reservation);
			redirectAttrs.addFlashAttribute("msg","????????? ???????????????.");
			session.invalidate();
			return "redirect:login";
		}
		ReservationDTO reservationDto = reserve(reservation);
		model.addAttribute("detail", reservationDto);
		model.addAttribute("user", user);
		
		System.out.println(reservation);
		return "admin/adminReservationDetail";
		
	}

	public String adminCancleReserveData(Model model, String seq, RedirectAttributes ra) {
		Integer i = Integer.parseInt(seq);
		Reservation reservation = rr.findBySeq(i);
		if(reservation.getStatus().equals("reserve")) {
			System.out.println("??????????");
			reservation.setStatus("cancle");
			rr.save(reservation);
		}else if(reservation.getStatus().equals("pay")) {
			reservation.setStatus("refund");
			rr.save(reservation);
		}
		ra.addFlashAttribute("msg","?????????????????????.");
	
		return "redirect:adminReservationModify?memberId="+reservation.getId()+"&reserve=future&page=0&size=10";
	}
	
}
