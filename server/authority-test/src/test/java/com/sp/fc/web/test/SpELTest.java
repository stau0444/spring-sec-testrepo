package com.sp.fc.web.test;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Person{
    private String name;
    private int height;

    public boolean over(int pivot){
        return height >= pivot;
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Dog{
    private String name;
    private int height;

    public boolean over(int pivot){
        return height >= pivot;
    }
}
public class SpELTest {


    ExpressionParser parser = new SpelExpressionParser();

    Person p = Person.builder()
            .name("우고")
            .height(165)
            .build();
    Dog d = Dog.builder()
            .name("gopdan")
            .height(60)
            .build();

    @DisplayName("spELtest1")
    @Test
    void elBasic(){

        assertEquals("우고" , parser.parseExpression("name").getValue(p));

    }

    @DisplayName("spELtest2-값 변경")
    @Test
    void changeValue(){
        //bean에 있는 값을 스크립트로 수정하고있다
        parser.parseExpression("name").setValue(p,"오도이");
        //테스트가 통과된다 .
        assertEquals("오도이" , parser.parseExpression("name").getValue(p));
    }


    @DisplayName("spELtest3-빈의 메소드 사용")
    @Test
    void userMethod(){
        assertTrue(parser.parseExpression("over(160)").getValue(p , Boolean.class));
        assertFalse(parser.parseExpression("over(160)").getValue(d , Boolean.class));

    }


    @DisplayName("bean Test")
    @Test
    void useBeanInApplicationContext(){
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setBeanResolver(new BeanResolver() {
            @Override
            public Object resolve(EvaluationContext context, String beanName) throws AccessException {
                return beanName.equals("person") ? p : d;
            }
        });
        ctx.setRootObject(p);
        ctx.setVariable("dog",d);

        //applicationContext 에 등록된 Bean을 가져와 테스트하고 있다
        //StandardEvaluationContext는 applicationContext에 등록된
        //bean을 가져올 수 있게 한다 .
        //resolve는 가져올 bean에 대해 정의하는 메서드이다.
        //setRootObject(p); 컨텍스트안에서 기본적으로 사용될 빈을 지정할 수 있다.
        //ctx.setVariable("dog",d); 은 특정 변수를 사용하여 El에서 bean을 사용할 수 있게 해준다.

        assertTrue(parser.parseExpression("over(160)").getValue(ctx , Boolean.class));
        assertFalse(parser.parseExpression("#dog.over(160)").getValue(ctx,Boolean.class));
        assertTrue(parser.parseExpression("@person.over(160)").getValue(ctx , Boolean.class));
        assertFalse(parser.parseExpression("@dog.over(160)").getValue(ctx , Boolean.class));

    }

}
