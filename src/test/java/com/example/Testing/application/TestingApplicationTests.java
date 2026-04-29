package com.example.Testing.application;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest
class TestingApplicationTests {

	@Test
	void testNumberOne() {

        int a=5;
        int b=10;
        int result=addTwoNumber(a,b);
        //   Assertions.assertEquals(13,result);

       assertThat(result).isEqualTo(15)
                .isCloseTo(14, Offset.offset(1));

        assertThat("Apple").isEqualTo("Apple").startsWith("Ap").endsWith("le");

	}


    int addTwoNumber(int a,int b){
        return a+b;
    }

}
