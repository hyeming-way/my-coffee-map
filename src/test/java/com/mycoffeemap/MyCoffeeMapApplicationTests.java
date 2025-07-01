package com.mycoffeemap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;

@SpringBootTest
class MyCoffeeMapApplicationTests {

	
/*	
	@Autowired
	private QuestionRepository questionRepository;//질문 Question 테이블 정보
												  //조회, 추가, 수정, 삭제 용도의 메소드		
	@Autowired
	private AnswerRepository answerRepository;//답변 Answer 테이블정보 조회, 추가, 수정, 삭제 용도의 메소드	
	
    @Autowired
    private QuestionService questionService;
*/
	
	//페이징 테스트를 위해 질문 엔티티 300 개 생성 해서 DB에 INSERT 
/*    
	@Test
	void testJpa() {
		for(int i=1; i<=300;  i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";
			this.questionService.create(subject, content);
		}
	}
*/	
	
	
/*	
테스트 에러 발생 이유  
-> 왜냐하면 QuestionRepository가 findById 메서드를 통해 Question 객체를 조회하고 나면 
   DB 세션이 끊어지기 때문이다.
   그래서 그 이후에 실행되는 q.getAnswerList() 메서드(Question 객체로부터 answer 리스트를 구하는 메서드)는 
   세션이 종료되어 오류가 발생한다. 
   answerList는 앞서 q 객체를 조회할 때가 아니라 
   q.getAnswerList() 메서드를 호출하는 시점에 가져오기 때문에 이와 같이 오류가 발생한 것이다.
   이렇게 데이터를 필요한 시점에 가져오는 방식을 지연(Lazy) 방식이라고 한다. 
   이와 반대로 q 객체를 조회할 때 미리 answer 리스트를 모두 가져오는 방식은 즉시(Eager) 방식이라고 한다. 
   @OneToMany, @ManyToOne 애너테이션의 옵션으로 
   fetch=FetchType.LAZY 또는 fetch=FetchType.EAGER처럼 가져오는 방식을 설정할 수 있는데, 
   이 책에서는 따로 지정하지 않고 항상 기본값(디폴트값)을 사용한다.
   사실 이 문제는 테스트 코드에서만 발생한다. 실제 서버에서 JPA 프로그램들을 실행할 때는 DB 세션이 종료되지 않아 이와 같은 오류가 발생하지 않는다.
   테스트 코드를 수행할 때 이런 오류를 방지할 수 있는 가장 간단한 방법은 
   다음과 같이 @Transactional 애너테이션을 사용하는 것이다. 
   @Transactional 애너테이션을 사용하면 메서드가 종료될 때까지 DB 세션이 유지된다. 코드를 수정해 보자.
*/   
/* 
//질문을 조회한 후 이 질문에 달린 답변 전체를 조회하는 테스트 코드	
	// JPA 연관관계를 통해 질문에 달린 답변들을 잘 가져올 수 있는지 확인하는 테스트입니다.
    @Transactional
	@Test
    void testJpa() {
    	//1. ID가 2번인 질문을 데이터베이스에서 조회해서 가져옵니다.
        Optional<Question> oq = this.questionRepository.findById(2);
        
        //2. 해당 질문이 실제로 데이터베이스에 존재하는지 확인합니다.
        //   존재하면 (oq.isPresent() -> true)  , 존재하지않으면 (oq.isPresent() -> false)
        //   assertTrue(true); -> 테스트 통과  
        //   assertTrue(false); -> 테스트 실패    조회 안됨 
        assertTrue(oq.isPresent());
        
        //3. Optional에서 실제 조회된 Question 질문 객체를 꺼냅니다.
        Question q = oq.get();

        //4. 해당 질문에 달린 답변 목록을 가져옵니다.
        // Answer는 Question과 @OneToMany 관계로 연결되어 있습니다.
        List<Answer> answerList = q.getAnswerList();

        //5.이 질문에 달린 답변이 정확히 1개인지 확인합니다.
        // 예: "네 자동으로 생성됩니다." 라는 답변이 하나만 존재하는 경우
        assertEquals(1, answerList.size());
        
        //6. 그 하나뿐인 답변의 내용을 검증합니다.
        // 즉, 첫 번째(0번 인덱스)의 답변 내용이 우리가 저장했던 그 문장인지 확인합니다.
        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
    }
*/	
/*
//답변 엔티티 조회 할때 JOIN해서 질문 엔티티도 같이 조회  테스트
	// 이 메서드는 ID가 1번인 답변이 실제로 존재하는지 확인하고,
	// 그 답변이 ID가 2번인 질문(Question)에 연결되어 있는지를 검사합니다.
    @Test
    void testJpa() {
    	//1. ID가 1번인 답변을 데이터베이스에서 조회 해서 가져옵니다.
        Optional<Answer> oa = this.answerRepository.findById(1);
        //2. 조회된 해당 답변이 존재하는지 확인 합니다.
        //  존재하면 테스트 통과 , 존재하지 않으면 테스트 실패
        assertTrue(oa.isPresent());
        
        //3. Optional에서 실제 조회된 Answer 답변 객체를 꺼냅니다.
        Answer a = oa.get();
              
        //4.이 답변이 연결된 질문(Question)의 ID가 2인지 확인합니다.
        // 만약 연결된 질문의 ID가 2가 아니면 테스트는 실패합니다.
        assertEquals(2, a.getQuestion().getId());
    }
*/
/*	
	//답변 엔티티 등록(추가,저장) 테스트
    @Test
    void testJpa() {
    	//1. 먼저 ID가 2인 질문을 데이터베이스에서 조회해 옵니다.
    	//   결과는 Optional<Question> 타입입니다.  (null 일수도 있기 떄문)
        Optional<Question> oq = this.questionRepository.findById(2);
        //2. 해당 질문이 실제로 존재하는지 확인합니다
        //   만약 존재 하지 않으면 테스트는 실패합니다. 존재하면 테스트는 통과.
        assertTrue(oq.isPresent());
        
        //3. Optional에서 실제 Question 객체를 꺼냅니다.
        // 이 객체는 새로 만드는 답변과 연결될 질문입니다.
        Question q = oq.get();

        //4. 새로운 Answer 객체(답변)를 생성합니다.
        Answer a = new Answer();
        a.setContent("네 자동으로 생성됩니다."); //답변 내용 설정 
        
        //어떤 질문에 대한 답변인지 연결해 줍니다.
        // 답변과 질문은 N:1 관계이므로, 답변이 어떤 질문에 속하는지 반드시 알려줘야 합니다.
        a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
        				   // "q"는 위에서 찾은 ID=2인 질문 객체
        
        //답변 작성 시각을 현재 컴퓨터의 시스템 시간으로 설정
        a.setCreateDate(LocalDateTime.now());
        
        //이제 이 답변을 데이터베이스에 저장합니다.
        this.answerRepository.save(a); //INSERT 실행됨
    }
*/	
	
/*	
	//질문 엔티티 삭제 테스트
    @Test
    void testJpa() {
    	//1. 현재 Question테이블에 저장된 질문 수가 2개인지 확인 합니다.
        //만약 개수가 2가 아니라면 테스트는 실패합니다.  조회된 개수가 2와 같으면 테스트는 통과합니다.
    	assertEquals(2, this.questionRepository.count());
        
    	//2. ID가 1번인 질문을 데이터베이스에서 찾아 조회해옵니다.
    	// findById 메소드의 반환타입은 Optional<Question> 타입으로 반환합니다.
        Optional<Question> oq = this.questionRepository.findById(1);
        //ID가 1번인 질문 엔티티가 조회되면 테스트 통과
        assertTrue(oq.isPresent());
       
        //3. Optional에서 실제 삭제할 조회된 Question 객체를 꺼냅니다.
        Question q = oq.get();
        
        //4. 삭제할 조회된 Question객체를 DB에서 삭제 시킵니다.
        this.questionRepository.delete(q); //DELETE 쿼리가 만들어지며 실행됨 
        
        //5. 삭제 후, 질문 레코드의 총 개수가 1개인지 다시 확인 합니다.
        assertEquals(1, this.questionRepository.count());
    }
*/	
	
	
   /* 
	//질문 레코드(엔티티) 수정 테스트 
    @Test
    void testJpa() {
    	//1.수정할 질문 레코드(엔티티)가 DB에 저장되어 있는지 확인하기 위해 아이디로 조회해 오자
        Optional<Question> oq = this.questionRepository.findById(1);
        
        //oq.isPresent() -> Optional 메모리에  조회된 Question엔티티가 저장되어 있으면 true반환 없으면 false반환
        // assertTrue(true); -> 테스트 통과 
        // assertTrue(false); -> 테스트 실패 
        assertTrue(oq.isPresent());
        
        //2. 수정작업
        Question q = oq.get(); // Optional 메모리에서 조회된 Question엔티티 객체 얻기 
        
        q.setSubject("수정된 제목"); // Question엔티티에 subject변수(열)에 "수정된 제목" 넣어 저장 
        
        this.questionRepository.save(q);// Question엔티티를 save()메소드로 전달해 UPDATE 합니다.
    }
	*/
/*	
	 @Test
    void testJpa() {
        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
        
        Question q = qList.get(0);
        
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }
*/	
	
  /*
	//질문의 제목과 내용이 모두 일치하는 질문 레코드 조회 테스
    @Test
    void testJpa() {
        Question q = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", 
        															 "sbb에 대해서 알고 싶습니다.");
        assertEquals(1, q.getId());
    }
  */	
	
	/*

	 //질문의 제목으로 데이터 조회 테스트
	 @Test
	 void testJpa() {
	        Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
	        assertEquals(1, q.getId());
	 }
	*/
	
	/*
	//질문의 id값으로 데이터 조회 테스트
	@Test
    void testJpa() {
		
        Optional<Question> oq = this.questionRepository.findById(1);
            
        if(oq.isPresent()) {
        	
            Question q = oq.get();
            
            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }
    }
	*/
	
	/*
	//질문 조회 테스트
    @Test
    void testJpa() {
        List<Question> all = this.questionRepository.findAll();
        assertEquals(2, all.size());

        Question q = all.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }
    */
	/*
	//질문 생성 테스트 
	@Test
    void testJpa() {        
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 첫번째 질문 저장

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);  // 두번째 질문 저장
    }
	*/

}
