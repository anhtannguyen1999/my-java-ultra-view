import java.awt.AWTException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.ImageIcon;

import DTO.DTO_ArrayLANImageInforObject;
import DTO.DTO_LANImageInforObject;
import GUI.RemoteScreenForm;

public class TestCaptureScreen {
	public static void main(String[] args) throws InterruptedException {
		TestCaptureScreen testCaptureScreen=new TestCaptureScreen();
		RemoteScreenForm remoteScreenForm=new RemoteScreenForm("192.169.1.2", "1999", "");
		while(true) {
			Thread.sleep(40);
			DTO_ArrayLANImageInforObject arrayLANImageInforObject=testCaptureScreen.GetLANScreenCaptureImageArray1();
			remoteScreenForm.ShowImageToPanel(arrayLANImageInforObject);
		}
		
	}
	private BufferedImage screenFullImage;
	private Robot robot;
	private Rectangle screenRect=null;
	//private ImageIcon imgIcon;
	//private static int count=-1;
	//private byte[] byteImage;
	//private int screenWidth;
	//private int screenHeight;
	
	private final int colRowNum=16;
	
	private DTO_LANImageInforObject matrixlanImgInfoObj[][]=new DTO_LANImageInforObject[colRowNum][colRowNum];
	private int timeCountMatrixlanImgInfoObj[][]=new int [colRowNum][colRowNum];
	private final int maxTimCountMatrixlanImgInfoObj=40;
	private DTO_ArrayLANImageInforObject arrlanImgInfoObj;
	private int gridWidth;
	private int gridHeight;
	
	TestCaptureScreen(){
		//screenWidth= (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		//screenHeight= (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		screenFullImage = robot.createScreenCapture(screenRect);
		if(gridWidth==0||gridHeight==0) {
	       	 gridWidth=screenFullImage.getWidth()/colRowNum;
	       	 gridHeight=screenFullImage.getHeight()/colRowNum;
        }
		wStep=gridWidth/colRowNum; hStep=gridHeight/colRowNum;
	}
	//10%-25%CPU, TB 15%
	public DTO_ArrayLANImageInforObject GetLANScreenCaptureImageArray1() {
		
 
         //String fileName = "D://FullScreenshot.jpg";
		if(arrlanImgInfoObj!=null)
			arrlanImgInfoObj.Clear();
		else {
			arrlanImgInfoObj=new DTO_ArrayLANImageInforObject();
		}
	     if(screenFullImage!=null) {
	    	 screenFullImage.flush();
	    	 
	     }
	     java.lang.Runtime.getRuntime().gc();	
         screenFullImage = robot.createScreenCapture(screenRect); //ImageIO.write(screenFullImage, "jpg", new File(fileName));
         
         ImageIcon imgScrop;
         //arrlanImgInfoObj=new DTO_ArrayLANImageInforObject();
         for(int i=0;i<colRowNum;i++)//row |
        	 for(int j=0;j<colRowNum;j++) { //column ->
        		 imgScrop = new ImageIcon(screenFullImage.getSubimage(j*gridWidth, i*gridHeight,gridWidth , gridHeight));
        		 //imgScrop=screenFullImage.getSubimage(j*gridWidth, i*gridHeight,gridWidth , gridHeight);
        		 DTO_LANImageInforObject lanImgInfoObj=new DTO_LANImageInforObject(imgScrop,j,i,gridWidth,gridHeight);
        		 
        		 //Vua moi reset count
        		 if(timeCountMatrixlanImgInfoObj[j][i]==0) {
        			 matrixlanImgInfoObj[j][i]= lanImgInfoObj; //Gan image obj
        			 arrlanImgInfoObj.Add(lanImgInfoObj);	//Add vo array list
        		 }
        		 //Neu khong bi trung
        		 else if(!isEqualToArrElement1(lanImgInfoObj,j,i)) { 
        			 matrixlanImgInfoObj[j][i].reset();
        			 matrixlanImgInfoObj[j][i]= lanImgInfoObj;
        			 arrlanImgInfoObj.Add(lanImgInfoObj);
        		 }
        		 else {
        			 timeCountMatrixlanImgInfoObj[j][i]++;
            		 if(timeCountMatrixlanImgInfoObj[j][i]>maxTimCountMatrixlanImgInfoObj)
            			 timeCountMatrixlanImgInfoObj[j][i]=0;
        		 }
        			 
        	 }
         return arrlanImgInfoObj;
        
    }

	private int wStep;
	private int hStep;
	public boolean isEqualToArrElement1(DTO_LANImageInforObject lanImgInfoObj, int x,int y) {
		BufferedImage img1=(BufferedImage)lanImgInfoObj.image.getImage();
		BufferedImage img2=(BufferedImage)matrixlanImgInfoObj[x][y].image.getImage();
		for(int i=0;i<gridWidth-2;i+=wStep) {
			for(int j=0;j<gridHeight-2;j+=hStep) {
				if(img1.getRGB(i,j)!=img2.getRGB(i,j)) {
					return false;
				}
			}
		}
		return true;
	}
}
