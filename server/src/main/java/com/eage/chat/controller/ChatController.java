package com.eage.chat.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.eage.chat.model.InputString;
import com.eage.chat.model.Message;

@Controller
public class ChatController {

	Map<String,String> data = new ConcurrentHashMap<>();
	Map<String,Integer> checkMap = new ConcurrentHashMap<>();

	@MessageMapping("/hello")
	@SendTo("/topic/message")
	public Message greeting(InputString message, SimpMessageHeaderAccessor  headerAccessor) throws Exception {
		return getMessage(message, headerAccessor.getSessionId());
	}

	public Message getMessage(InputString message, String  sessionId) {
		Set<String> inputSet = new HashSet<String>(Arrays.asList(message.getMessage().toLowerCase().split(" ")));
		for(Map.Entry<String, String> da : data.entrySet()) {
			Set<String> keyString = new HashSet<String>(Arrays.asList(da.getKey().toLowerCase().split(" ")));
			if(keyString.containsAll(inputSet)) {
				if(inputSet.contains("question")) {
					List<Integer> numbers = generateRandomNumbers();
					String result = String.format("%s %d, %d, %d ",da.getValue(), numbers.get(0), numbers.get(1), numbers.get(2));
					int sum = numbers.stream().mapToInt(Integer::intValue).sum();
					checkMap.put(sessionId, sum);
					data.put("Sorry, the answer is " + sum, "That’s great ");
					return Message.builder().content(result).build();
				}
				Optional<String> result = inputSet.stream().filter(value -> StringUtils.isNumeric(value)).findFirst();
				if(result.isPresent() && (checkMap.get(sessionId) == Integer.parseInt(result.get()))) {
					return Message.builder().content("That’s great").build();
				}
				return Message.builder().content(da.getValue()).build();
			}
		}
		return Message.builder().content("i couldn't understand, please enter again").build();
	}

	@PostConstruct
	public void init() {
		data.put("Hey Service, can you provide me a question with numbers to add ? ", "Here you go, solve the question: “Please sum the numbers");
		data.put("Hello Hey Hi", "Welcome user");
		data.put("Great . answer is ",
				"That’s wrong. Please try again.");
		data.put("Sorry, the answer is ",
				"That’s great ");

	}

	public List<Integer> generateRandomNumbers(){
		List<Integer> integerList = IntStream.range(0, 100).boxed().collect(Collectors.toList());
		Collections.shuffle(integerList);
		return integerList.subList(0, 3);
	}
}
