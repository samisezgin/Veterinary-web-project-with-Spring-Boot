package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;

@SpringBootApplication
public class SpringJsfExample2Application implements CommandLineRunner  {

	@Autowired
	RoleRepository rolerepository;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringJsfExample2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Role role1=new Role();
		role1.setRole("ADMIN");
		role1.setRole_id(1);
		Role role2=new Role();
		role2.setRole("USER");
		role2.setRole_id(2);
		Role role3=new Role();
		role3.setRole("EDITOR");
		role3.setRole_id(3);
		rolerepository.save(role1);
		rolerepository.save(role2);
		rolerepository.save(role3);
		
	}

}

