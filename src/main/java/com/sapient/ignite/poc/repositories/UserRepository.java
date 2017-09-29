package com.sapient.ignite.poc.repositories;



import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sapient.ignite.poc.beans.User;


@Repository
@Transactional
public class UserRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<User> getUsers() {
		String qlString = "FROM User";
		TypedQuery<User> query = entityManager.createQuery(qlString, User.class);			 
		return query.getResultList();
	}
	
	public User findUser(String id) {
		return entityManager.find(User.class, Integer.valueOf(id));
	}
	
	
	@Transactional
	public User insertUser() {
		
		User user = new User("CHOW", "Johnson", "Chow"); 
		entityManager.persist(user);
		/*
		int result = 1;
		entityManager.joinTransaction();
		Query query = entityManager.createNativeQuery("INSERT INTO USER_TABLE(USER_ID, USER_NAME, FIRST_NAME, LAST_NAME) VALUES (USER_ID_SEQ.NEXTVAL, ?, ? ,?) ");
		query.setParameter(1, "TEST USER");
		query.setParameter(2, "ALPHA");
		query.setParameter(3, "OMEGA");
		result = query.executeUpdate();
		*/
		return user;
	}
	
	@Transactional
	public User updateUser(User user) {
		return entityManager.merge(user);
	}
}
