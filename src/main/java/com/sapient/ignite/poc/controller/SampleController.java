package com.sapient.ignite.poc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.ignite.poc.beans.User;
import com.sapient.ignite.poc.repositories.UserRepository;

@RestController
public class SampleController {
	
	@Autowired
	private ApplicationContext context;

	@RequestMapping("/hello")
    public String Hello() {
        return "Greetings from Spring Boot!";
    }
	
	@RequestMapping(value = "/getusers", produces = {
    "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<List<User>> GetUsers() {
		UserRepository  userRepository = context.getBean(UserRepository.class);
		List<User> userList = userRepository.getUsers();
		return new ResponseEntity<>(userList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/finduser", produces = {
    "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<User> FindUser(@RequestParam(value="id", defaultValue="1") String id) {
		UserRepository  userRepository = context.getBean(UserRepository.class);
		User user = userRepository.findUser(id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/insertuser", produces = {"application/json"}, method = RequestMethod.GET)
	public ResponseEntity<User> InsertUser() {
		UserRepository  userRepository = context.getBean(UserRepository.class);
		User user = userRepository.insertUser();
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	@RequestMapping(value = "/updateuser", produces = {"application/json"}, method = RequestMethod.GET)
	public ResponseEntity<User> UpdateUser(@RequestParam(value="firstName") String firstName, @RequestParam(value="id") String id) {
		UserRepository  userRepository = context.getBean(UserRepository.class);
		User user = userRepository.findUser(id);
		user.setFirstName(firstName);
		userRepository.updateUser(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	

}
