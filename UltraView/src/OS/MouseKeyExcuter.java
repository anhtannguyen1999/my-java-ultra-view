package OS;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;

public class MouseKeyExcuter {
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//Lay size goc
	public static float dpi = Toolkit.getDefaultToolkit().getScreenResolution();
	private Robot robot;
	private int screenWidth;
	private int screenHeight;
	private int leftMark=InputEvent.BUTTON1_DOWN_MASK;
	private int midMark=InputEvent.BUTTON2_DOWN_MASK;
	private int rightMark=InputEvent.BUTTON3_DOWN_MASK;
	public MouseKeyExcuter() {
		// TODO Auto-generated constructor stub
		try {
			robot=new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		screenWidth=(int) screenSize.getWidth();
		screenHeight=(int)screenSize.getHeight();
	}
	public void ExcuteByMessage(String message) {
		String []arr=message.split("-");
		//Mouse move vs drag
		if(arr[0].equals("MMOVE")) {
			int x= (int) (Float.parseFloat(arr[1])*screenWidth) ;//xRatio*width
			int y= (int) (Float.parseFloat(arr[2])*screenHeight) ;//xRatio*width
			robot.mouseMove(x, y);
			//System.out.println(screenWidth+","+screenHeight+";"+x+":"+y+","+dpi);
		}
		//Mouse press vs release
		else if(arr[0].equals("CLK")) {
			if(arr[1].equals("LEFT")) {
				if(arr[2].equals("DOWN")) {
					robot.mousePress(leftMark);
				}else if(arr[2].equals("UP")) {
					robot.mouseRelease(leftMark);
				}else {}
			}else if(arr[1].equals("MID")) {
				if(arr[2].equals("DOWN")) {
					robot.mousePress(midMark);
				}else if(arr[2].equals("UP")) {
					robot.mouseRelease(midMark);
				}else {}
			}else if(arr[1].equals("RIGHT")) {
				if(arr[2].equals("DOWN")) {
					robot.mousePress(rightMark);
				}else if(arr[2].equals("UP")) {
					robot.mouseRelease(rightMark);
				}else {}
			}
		}
		//Wheel
		else if(arr[0].equals("MWHEEL")) {
			int amount=Integer.parseInt(arr[2]);
			
			if(arr[1].equals("DOWN")) {
				robot.mouseWheel(amount);
			}
			else {// if(arr[1].equals("UP")) {
				robot.mouseWheel(-amount);
			}
		}
		//Key
		else if(arr[0].equals("KEY")) {
			int keycode=Integer.parseInt(arr[2]);
			if(arr[1].equals("DOWN")) {
				robot.keyPress(keycode);
			}else{ //if(arr[1].equals("UP")) {
				robot.keyRelease(keycode);
			}
		}
	}
}
