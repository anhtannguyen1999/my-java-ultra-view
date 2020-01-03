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
import GUI.RemoteScreenForm;

public class ScreenCapturer {
	public static void main(String[] args) throws InterruptedException {
		ScreenCapturer testCaptureScreen=new ScreenCapturer();
		RemoteScreenForm remoteScreenForm=new RemoteScreenForm("192.169.1.2", "1999", "",0);
		while(true) {
			Thread.sleep(40);
			DTO_ArrayLANImageInforObject arrayLANImageInforObject=testCaptureScreen.GetLANScreenCaptureImageArray();
			remoteScreenForm.ShowImageToPanel(arrayLANImageInforObject);
		}
		
	}
	
	
	private BufferedImage screenFullImage;
	private Robot robot;
	private Rectangle screenRect;
	private ImageIcon imgIcon;
	//private static int count=-1;
	//private byte[] byteImage;
	//private int screenWidth;
	//private int screenHeight;
	private final int numGridMatrixOIP=16;
	private DTO_LANImageInforObject matrixOIP[][]=new DTO_LANImageInforObject[numGridMatrixOIP][numGridMatrixOIP];
	private int timeCountMatrixOIP[][]=new int [numGridMatrixOIP][numGridMatrixOIP];
	private final int maxTimCountMatrixOIP=40;
	private DTO_ArrayLANImageInforObject arrOIP;
	private int gridWidth;
	private int gridHeight;
	
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

	public ScreenCapturer(){
		screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        screenFullImage = robot.createScreenCapture(screenRect); //ImageIO.write(screenFullImage, "jpg", new File(fileName));
		gridWidth=screenFullImage.getWidth()/16;
   	 	gridHeight=screenFullImage.getHeight()/16;
   	 	screenFullImage.flush();
	}
	
	public DTO_ArrayLANImageInforObject GetLANScreenCaptureImageArray() {
        if(screenFullImage!=null) {
       	 screenFullImage.flush();
        }
   	 java.lang.Runtime.getRuntime().gc();
        screenFullImage = robot.createScreenCapture(screenRect); //ImageIO.write(screenFullImage, "jpg", new File(fileName));  
        ImageIcon imgScrop;
        arrOIP=new DTO_ArrayLANImageInforObject();
        for(int i=0;i<numGridMatrixOIP;i++)//row |
       	 for(int j=0;j<numGridMatrixOIP;j++) { //column ->
       		 imgScrop = new ImageIcon(screenFullImage.getSubimage(j*gridWidth, i*gridHeight,gridWidth , gridHeight));
       		 //imgScrop=screenFullImage.getSubimage(j*gridWidth, i*gridHeight,gridWidth , gridHeight);
       		 DTO_LANImageInforObject oip=new DTO_LANImageInforObject(imgScrop,j,i,gridWidth,gridHeight);
       		 if(timeCountMatrixOIP[j][i]==0) {
       			 matrixOIP[j][i]= oip;
       			 arrOIP.Add(oip);
       		 }
       		 else if(!isEqualToArrElement(oip,j,i)) { //So sanh voi cai mang hinh cu 
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
		BufferedImage img1=(BufferedImage)oip.image.getImage();
		BufferedImage img2=(BufferedImage)matrixOIP[x][y].image.getImage();
		int w=oip.width, h=oip.height;
		int wStep=w/10, hStep=h/10;
		for(int i=0;i<w-2;i+=wStep) {
			for(int j=0;j<h-2;j+=hStep) {
				if(img1.getRGB(i,j)!=img2.getRGB(i,j)) {
					return false;
				}
			}
		}
		return true;
	}
}
