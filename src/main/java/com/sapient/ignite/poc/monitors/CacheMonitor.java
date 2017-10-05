package com.sapient.ignite.poc.monitors;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.persistence.EntityManagerFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManager;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;

@Aspect
public class CacheMonitor {
	private final static Logger LOG = LogManager.getLogger(CacheMonitor.class);
	private final static NumberFormat NF = new DecimalFormat("0.0###");

	//@Autowired
	//private SessionFactory sessionFactory;
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Around("execution(* com.sapient.ignite.poc.beans.User(..))")
	public Object log(ProceedingJoinPoint pjp) throws Throwable {
		if (!LOG.isDebugEnabled()) {
			return pjp.proceed();
		}
		
		
		SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
		Statistics statistics = sessionFactory.getStatistics();
		statistics.setStatisticsEnabled(true);

		long hit0 = statistics.getQueryCacheHitCount();
		long miss0 = statistics.getSecondLevelCacheMissCount();

		Object result = pjp.proceed();

		long hit1 = statistics.getQueryCacheHitCount();
		long miss1 = statistics.getQueryCacheMissCount();

		double ratio = (double) hit1 / (hit1 + miss1);

		if (hit1 > hit0) {
			LOG.debug(String.format("CACHE HIT; Ratio=%s; Signature=%s#%s()", NF.format(ratio),
					pjp.getTarget().getClass().getName(), pjp.getSignature().toShortString()));
		} else if (miss1 > miss0) {
			LOG.debug(String.format("CACHE MISS; Ratio=%s; Signature=%s#%s()", NF.format(ratio),
					pjp.getTarget().getClass().getName(), pjp.getSignature().toShortString()));
		} else {
			LOG.debug("query cache not used");
		}

		return result;
	}

}
