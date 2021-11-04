package jcpmv2.jkcho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
@SpringBootApplication 어노테이션은
@Configuration, @EnableAutoConfiguration, @ComponentScan 의 합이다.
@componentScan을 포함하고 있기에 bean을 만들 시 해당 어노테이션이 존재하는 패키지와
그 하위 패키지들을 scan 하게 되는 것
*/

@SpringBootApplication
public class JkchoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JkchoApplication.class, args);
	}

}
