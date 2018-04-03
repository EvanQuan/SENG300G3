package test;

import java.io.IOException;

public class MainTest {

	public class Inner {
	}

	public static void main(String[] args) throws IOException {
		String s = "bar.Other.Foo[]";
		s = s.substring(0, s.length() - 2);
		System.out.println(s);
	}

}
