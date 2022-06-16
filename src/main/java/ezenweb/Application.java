package ezenweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing  // JPA 매핑된 엔티티(테이블)의 변화 감지  [스프링 시작부터]
@SpringBootApplication  // 스프링부트의 자동 설정, 스프링 빈(클래스 메모리) 읽기와 생성 모두 자동 설정
public class Application {
    public static void main(String[] args) {    // 메인 스레드 ( 코드를 읽어주는 역할)
        SpringApplication.run(Application.class);   // 내장 서버(톰캣) 스프링 시작
    }
}
