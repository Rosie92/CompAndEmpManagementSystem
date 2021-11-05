package jcpmv2.jkcho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
/*
@SpringBootApplication 어노테이션은
@Configuration, @EnableAutoConfiguration, @ComponentScan 의 합이다.
@componentScan을 포함하고 있기에 bean을 만들 시 해당 어노테이션이 존재하는 패키지와
그 하위 패키지들을 scan 하게 되는 것
*/

@SpringBootApplication
public class JkchoApplication {

	public static void main(String[] args) {
/*
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			logic(em);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}

	private static void logic(EntityManager em) {

	}*/
		SpringApplication.run(JkchoApplication.class, args);
	}
}
