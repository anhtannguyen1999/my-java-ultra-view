package OtherComponent;

import java.util.Random;

public class ReusableFunctions {
	public static String RandomPassword(int length) {
		String kq="";
		Random r = new Random();
		for(int i=0;i<length;i++) {
			char c = (char)(r.nextInt(26) + 'a');
			kq+=c;
		}
		return kq;
	}
}
