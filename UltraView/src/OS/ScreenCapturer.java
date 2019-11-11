package OS;

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

public class ScreenCapturer {
	private BufferedImage screenFullImage;
	private Robot robot;
	private Rectangle screenRect;
	private ImageIcon imgIcon;
	//private static int count=-1;
	private byte[] byteImage;
	private int screenWidth;
	private int screenHeight;
	private final int numGridMatrixOIP=16;
	private DTO_LANImageInforObject matrixOIP[][]=new DTO_LANImageInforObject[numGridMatrixOIP][numGridMatrixOIP];
	private int timeCountMatrixOIP[][]=new int [numGridMatrixOIP][numGridMatrixOIP];
	private final int maxTimCountMatrixOIP=40;
	private DTO_ArrayLANImageInforObject arrOIP;
	
	public byte[] CompressImageToByte(BufferedImage image) {
		// The important part: Create in-memory stream
		ByteArrayOutputStream compressed = new ByteArrayOutputStream();
		ImageOutputStream outputStream;
		try {
			//outputStream = ImageIO.createImageOutputStream(compressed);
			outputStream = new MemoryCacheImageOutputStream(compressed);

			// NOTE: The rest of the code is just a cleaned up version of your code
	
			// Obtain writer for JPEG format
			ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
	
			// Configure JPEG compression: 70% quality
			ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
			jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			jpgWriteParam.setCompressionQuality(0.7f);
	
			// Set your in-memory stream as the output
			jpgWriter.setOutput(outputStream);
	
			// Write image as JPEG w/configured settings to the in-memory stream
			// (the IIOImage is just an aggregator object, allowing you to associate
			// thumbnails and metadata to the image, it "does" nothing)
		
			jpgWriter.write(null, new IIOImage((RenderedImage) image, null, null), jpgWriteParam);
			// Dispose the writer to free resources
			jpgWriter.dispose();

			// Get data for further processing...
			byte[] jpegData = compressed.toByteArray();
			return jpegData;
		} catch (Exception e) {}
		return null;
	}

	public DTO_ArrayLANImageInforObject GetLANScreenCaptureImageArray() {
		robot=null;
		screenRect=null;
		//count=0;
		screenWidth= (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		screenHeight= (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int gridWidth=0;
		int gridHeight=0;
        
        	
         if(robot==null)
			try {
				robot = new Robot();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         //String fileName = "D://FullScreenshot.jpg";
         if(screenRect==null)
        	 screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
         if(screenFullImage!=null) {
        	 java.lang.Runtime.getRuntime().gc();
        	 screenFullImage.flush();
         }
        	
         screenFullImage = robot.createScreenCapture(screenRect); //ImageIO.write(screenFullImage, "jpg", new File(fileName));
         if(gridWidth==0||gridHeight==0) {
        	 gridWidth=screenFullImage.getWidth()/16;
        	 gridHeight=screenFullImage.getHeight()/16;
         }
         ImageIcon imgScrop;
         arrOIP=new DTO_ArrayLANImageInforObject();
         for(int i=0;i<16;i++)//row |
        	 for(int j=0;j<16;j++) { //column ->
        		 imgScrop = new ImageIcon(screenFullImage.getSubimage(j*gridWidth, i*gridHeight,gridWidth , gridHeight));
        		 //imgScrop=screenFullImage.getSubimage(j*gridWidth, i*gridHeight,gridWidth , gridHeight);
        		 DTO_LANImageInforObject oip=new DTO_LANImageInforObject(imgScrop,j,i,gridWidth,gridHeight);
        		 if(timeCountMatrixOIP[j][i]==0) {
        			 matrixOIP[j][i]= oip;
        			 arrOIP.Add(oip);
        		 }
        		 else if(!isEqualToArrElement(oip,j,i)) { //Phai sua lai: neu co thay doi thi cap nhat lai matrix va send di 
        			 ((DTO_LANImageInforObject)matrixOIP[j][i]).reset();
        			 matrixOIP[j][i]= oip;
        			 arrOIP.Add(oip);
        		 }	
        		 timeCountMatrixOIP[j][i]++;
        		 if(timeCountMatrixOIP[j][i]>maxTimCountMatrixOIP)
        			 timeCountMatrixOIP[j][i]=0;
        	 }
         return arrOIP;
        
    }

	public boolean isEqualToArrElement(DTO_LANImageInforObject oip, int x,int y) {
		int w=oip.width, h=oip.height;
		int wStep=w/16, hStep=h/16;
		for(int i=0;i<w-1;i+=wStep) {
			for(int j=0;j<h-1;j+=hStep) {
				if(((BufferedImage)oip.image.getImage()).getRGB(i,j)!=((BufferedImage)matrixOIP[x][y].image.getImage()).getRGB(i,j)) {
					return false;
				}
			}
		}
		return true;
	}
}
