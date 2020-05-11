package com.automation.helper;

/**
 * Final utils class wth static methods<br>
 * @author Govardhan
 *
 */
import java.util.Random;

public final class Utils {

	private Utils() {

	}

	/*
	 * 
	 */
	public static String getRandomString(int lentgh) {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		
		return new Random().ints(leftLimit, rightLimit + 1).limit(lentgh)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

	}
}
