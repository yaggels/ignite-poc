package com.sapient.ignite.poc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sapient.ignite.poc.beans.User;
import com.sapient.ignite.poc.repositories.UserRepository;

//@RunWith(SpringJUnit4ClassRunner.class)  
//@ContextConfiguration(locations={"classpath:application-context-test.xml"})
//@Transactional
public class UserTest {
	
	private static Logger logger = LogManager.getLogger(UserTest.class);
	
	//@Autowired
    private ApplicationContext context;

	//@Test
	public void testGetUsers() {
		UserRepository  userRepository = context.getBean(UserRepository.class);
		List<User> userList = userRepository.getUsers();
		assertThat(userList.isEmpty(), is(false));
		for(User user : userList) {
			logger.info(user);
		}
	}
	
	//@Test
	//@Rollback(false)
	public void testInsertUser() {
		UserRepository repository = context.getBean(UserRepository.class);
		User user = repository.insertUser();
		assertThat(user,is(not(null)));
	}

}
