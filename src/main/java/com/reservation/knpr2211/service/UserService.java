package com.reservation.knpr2211.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.reservation.knpr2211.entity.Favorite;
import com.reservation.knpr2211.entity.User;
import com.reservation.knpr2211.repository.FavoriteRepository;
import com.reservation.knpr2211.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	@Autowired 
	FavoriteRepository fr;
	@Autowired
	HttpSession session;
	@Autowired
	MountainCodeService mcs;
	 

	// 회원가입
	public String register(String id, String pw, String pwcon, String name, String email, String mobile,
			String member) {
		member = "normal";
		if (id == null || id.isEmpty())
			return "아이디를 입력하세요.";

		if (pw == null || pw.isEmpty())
			return "비밀번호를 입력하세요.";
		
		if (pw.equals(pwcon)==false)
			return "비밀번호가 일치하지않습니다.";

	
		if (name == null || name.isEmpty())
			return "이름을 입력하세요.";

		if (email == null || email.isEmpty())
			return "이메일을 입력하세요.";
		
		if (mobile == null || mobile.isEmpty())
			return "연락처를 입력하세요.";

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String securePw = encoder.encode(pw);
		
		User entity = User.builder().id(id).pw(securePw).name(name).email(email).mobile(mobile).member(member).deleted(false).build();
		userRepository.save(entity);

		return "회원가입 성공";
	}

	// 회원정보 수정 
		public String UserModify(String id, String pw, String pwcon, String name, String email, String mobile,
				String member) {
		
			if (pw == null || pw.isEmpty())
				return "비밀번호를 입력하세요.";
			
			if (pw.equals(pwcon)==false)
				return "비밀번호가 일치하지않습니다.";

		
			if (name == null || name.isEmpty())
				return "이름을 입력하세요.";

			if (email == null || email.isEmpty())
				return "이메일을 입력하세요.";
			
			if (mobile == null || mobile.isEmpty())
				return "연락처를 입력하세요.";

			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			
			String securePw = encoder.encode(pw);
			
			User entity = User.builder().id(id).pw(securePw).name(name).email(email).mobile(mobile).member(member).build();
			userRepository.save(entity);

			return "회원정보 수정 성공";
		}
	
	// 로그인
	public String login(String id, String pw) {
		String msg = "";
		User user = userRepository.findByid(id);
		if (user == null) {
			
			msg = "없는 계정입니다.";
			
			return msg;
			
		}if(user.getDeleted() == true) {
			
			msg = "삭제된 아이디 입니다";
			
			return msg;
		}

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		if (encoder.matches(pw, user.getPw())) {
			
			session.setAttribute("id", user.getId());
			session.setAttribute("email", user.getEmail());
			session.setAttribute("mobile", user.getMobile());
			session.setAttribute("name", user.getName());
			session.setAttribute("member", user);
			if(user.getMember().equals("admin")) {
				
				msg = "어드민 계정 로그인 성공";
				
			}else if(user.getMember().equals("normal")) {
			
				msg = "회원 로그인 성공";
				
			}
			return msg;
			
			
		}

		return "아이디 또는 비밀번호를 확인하세요.";
	}

	// 아이디 중복체크
	public String IdConfirm(String id) {

		if (userRepository.findById(id) == null) {

			return "사용가능한 아이디입니다";
		}

		return "중복된 아이디 입니다.";
		

	}
	// 아이디 중복체크
		public String PwConfirm(String pw , String pwcon) {
			System.out.println(pw);
			System.out.println(pwcon);
			if (pw.equals(pwcon))
				return "비밀번호가 일치합니다.";

			

			return "비밀번호가 일치하지 않습니다.";
		}
		// (시작)작성자:공주원 ==============================================
		//즐겨찾기 리스트
		public String favoriteList(Model model) {
			
			if(session.getAttribute("id")==null) return "redirect:login";
			
			User entity = userRepository.findByid((String)session.getAttribute("id"));
			List<Favorite> list = fr.findByFavoriteAndChecked(entity,true);
			
			
			ArrayList<String> strTypes = new ArrayList<String>();
			ArrayList<String> strFavorites= new ArrayList<String>();
			ArrayList<String> parkDetails= new ArrayList<String>();
			
			for(Favorite f : list) {
				String type = mcs.findCategory(f.getPlace().substring(0,1));
				String cat2 = mcs.findCategory(f.getPlace().substring(0,3));
				String cat3 = "";
				String parkDetail = f.getPlace();
				if(f.getPlace().substring(0,1).equals("C")) {
					cat3 = cat2+"  "+type;
				}
				else { cat3 = "[ "+cat2+" ]  "+mcs.findCategory(f.getPlace())+type; }
				
				strTypes.add(type);				
				strFavorites.add(cat3);	
				parkDetails.add(parkDetail);	
			}
			
			model.addAttribute("types",strTypes);
			model.addAttribute("favorites",strFavorites);
			model.addAttribute("parkDetails",parkDetails);
			return "user/favorite";
			
		}
		
		
		// (끝)작성자:공주원 ==============================================		
		
	
}
