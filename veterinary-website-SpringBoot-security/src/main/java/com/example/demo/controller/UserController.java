package com.example.demo.controller;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.model.Customer;
import com.example.demo.model.Pet;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@RequestMapping(value = "/show-user/{email}", method = RequestMethod.GET)
	public String UserShowPanel(@PathVariable String email, Map<String, Object> map) throws SQLException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("title", "Doktor Detayları");
		List<User> users=userRepository.getUserByEmail(email);
		if(users.size()>0) {
			if(auth.getName().equals(users.get(0).getEmail())) {
				map.put("message", email+" Hoşgeldiniz.");
				map.put("user", users.get(0));
			}else {
				map.put("message", email+" email adresi size ait değildir.");
			}
		}else {
			
			map.put("message", email+" email adresine ait kayıt bulunamamıştır..");
		}
		return "user/user-details";
	}
	
	@RequestMapping(value = "/update-user/{email}", method = RequestMethod.GET)
	public String UserUpdatePanel(@PathVariable String email, Map<String, Object> map) throws SQLException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("title", "Doktor Kayıt Güncelleme Sayfası");
		
		List<User> users=userRepository.getUserByEmail(email);
		if(users.size()>0) {
			if(auth.getName().equals(users.get(0).getEmail())) {
				map.put("user", users.get(0));
				return "user/user-update-panel";
			}else {
				map.put("message", email+" email adresi size ait değildir.");
			}
		}else {
			
			map.put("message", email+" email adresine ait kayıt bulunamamıştır..");
		}
		return "user/user-details";
	}
	
	@RequestMapping(value = "/update-user/", method = RequestMethod.POST)
	public String UserUpdate(@Valid @ModelAttribute("user") User user, BindingResult result,
			Map<String, Object> map)  throws SQLException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("title", "Doktor Kayıt Güncelleme Sayfası");
		
		List<User> users=userRepository.getUserByEmail(auth.getName());
		if(auth.getName().equals(users.get(0).getEmail())) {

			if (result.hasErrors()) {
				map.put("message", "Kayıt işlmei başarısız..");
				return "user/user-details";
			}

			user.setPassword(users.get(0).getPassword());
			map.put("message", "Kayıt işlemi başarılı. Tekrar giriş yapınız.");
			userRepository.save(user);
			return "redirect:/logout";
		}else {
			map.put("message", users.get(0).getEmail()+" email adresi size ait değildir.");
		}

		return "user/user-details";

	}
	
	
	@RequestMapping(value = "/update-user-password/{email}", method = RequestMethod.GET)
	public String UserPasswordUpdatePanel(@PathVariable String email, Map<String, Object> map) throws SQLException {

		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("title", "Doktor Detayları");
		List<User> users=userRepository.getUserByEmail(email);
		if(users.size()>0) {
			if(auth.getName().equals(users.get(0).getEmail())) {
				map.put("message", email+" Hoşgeldiniz.");
				map.put("user", users.get(0));
				return "user/user-password-update-panel";
			}else {
				map.put("message", email+" email adresi size ait değildir.");
			}
		}else {
			map.put("message", email+" email adresine ait kayıt bulunamamıştır..");
		}
		return "user/user-details";
	}
	
	@RequestMapping(value = "/update-user-password", method = RequestMethod.POST)
	public String UserPasswordUpdate(Map<String, Object> map,WebRequest request) throws SQLException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<User> users=userRepository.getUserByEmail(auth.getName());
		map.put("user", users.get(0));
		map.put("adminname", auth.getName());
		map.put("title", "Doktor Detayları");
		if(request.getParameter("oldpassword1").equals(request.getParameter("oldpassword2"))){
			boolean control=passwordEncoder.matches( request.getParameter("oldpassword1"),users.get(0).getPassword());
			if(control) {
				//changing password
				users.get(0).setReel_password(request.getParameter("password"));
				users.get(0).setPassword(passwordEncoder.encode(request.getParameter("password")));
				userRepository.save(users.get(0));
				map.put("message", " Şifreniz başarıyla güncellenmiştir..");
				return "redirect:/logout";
			}else {
				map.put("message", "Mevcut şifrenizi yanlış girdiniz.");
			}
		}else {
			map.put("message", "Mevcut şifreler uyuşmuyor.");
		}
		return "user/user-details";
	}
}
