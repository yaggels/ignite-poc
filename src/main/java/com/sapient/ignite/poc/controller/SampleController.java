package com.sapient.ignite.poc.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.MemoryMetrics;
import org.apache.ignite.cache.CacheMetrics;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
	private static Logger logger = LogManager.getLogger(SampleController.class);
	
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
	@RequestMapping(value="/getmetrics", produces = {"application/json"}, method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> GetMetrics(){
		Ignite ignite = Ignition.ignite("hibernate-grid");
		
		// Get the metrics of all the memory regions defined on the node.
		Collection<MemoryMetrics> memoryMetrics = ignite.memoryMetrics();
		// Get the cache metrics
		CacheMetrics cacheMetrics = ignite.cache("com.sapient.ignite.poc.beans.User").metrics();
		
		Map<String, Object> metricsMap = new HashMap<>();
		metricsMap.put("MemoryMetrics", memoryMetrics);
		metricsMap.put("cacheMetrics", cacheMetrics);
		
		return new ResponseEntity<>(metricsMap, HttpStatus.OK);
	}
	@RequestMapping(value="/gc", method=RequestMethod.GET)
	public void GarbageCollect() {
		System.gc();
	}
}
