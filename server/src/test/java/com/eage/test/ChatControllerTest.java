/**
 * 
 */
package com.eage.test;


import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.eage.chat.controller.ChatController;
import com.eage.chat.model.InputString;
import com.eage.chat.model.Message;


/**
 * @author Bhaskara S
 *
 */
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ChatControllerTest {
	
	@InjectMocks
	ChatController controller;
	
	@Before
	public void prepare() {
	    MockitoAnnotations.initMocks(this);
	    this.controller.init();
	}
	
	@Test
	public void getMessageTest() {
		InputString message = InputString.builder().message("Hi").build();
		String sessionId = "test360";
		Message result = controller.getMessage(message, "test360");
		assertTrue(result.getContent().equals("Welcome user"));
	}
	
	@Test
	public void getMessageTestCheckQuestion() {
		InputString message = InputString.builder().message("Provide me a question").build();
		String sessionId = "test360";
		Message result = controller.getMessage(message, "test360");
		String checkString = "Here you go, solve the question: “Please sum the numbers";
		assertTrue(result.getContent().contains(checkString));
	}
	
	

}
