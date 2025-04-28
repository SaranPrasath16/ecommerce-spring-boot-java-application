package com.ecommerce.util;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {
	
	public static String generateRandomNumber() {
	    Random random = new Random();
	    long number = 1000000000L + (long)(random.nextDouble() * 9000000000L);
	    return String.valueOf(number);
	}

}
