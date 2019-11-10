import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Test {
	public static void main(String[] args) throws InterruptedException {
		TestKey();
	}
	public static void TestMouse() throws InterruptedException {
		Robot bot = null;
		  try {
		   bot = new Robot();
		  } catch (Exception failed) {
		   System.err.println("Failed instantiating Robot: " + failed);
		  }
		  int mask = InputEvent.BUTTON1_DOWN_MASK;
		  Thread.sleep(1000);
		  bot.mouseMove(100, 100);
		  Thread.sleep(1000);
		  bot.mousePress(mask);
		  Thread.sleep(1000);
		  bot.mouseMove(200, 100);
		  Thread.sleep(100);
		  bot.mouseMove(300, 100);
		  Thread.sleep(100);
		  bot.mouseMove(400, 100);
		  Thread.sleep(100);
		  bot.mouseMove(500, 100);
		  Thread.sleep(100);
		  bot.mouseMove(600, 100);
		  Thread.sleep(100);
		  bot.mouseMove(700, 100);
		  Thread.sleep(100);
		  bot.mouseRelease(mask);
	}
	public static void TestKey() throws InterruptedException {
		Thread.sleep(3000);
		Robot bot = null;
		  try {
		   bot = new Robot();
		  } catch (Exception failed) {
		   System.err.println("Failed instantiating Robot: " + failed);
		  }
		  bot.keyPress(KeyEvent.VK_T);
		  bot.keyPress(KeyEvent.VK_A);
		  bot.keyPress(KeyEvent.VK_N);
	}
}
