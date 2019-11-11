package DTO;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;

public class DTO_LANImageInforObject extends Object implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ImageIcon image;
	public int column; //Toa do cua buc hinh
	public int row;
	public int width;
	public int height;
	
	public DTO_LANImageInforObject(ImageIcon img, int col,int row, int w, int h) {
		image=img;
		column=col;
		this.row=row;
		width=w;
		height=h;
	}
	
	public DTO_LANImageInforObject() {
		super();
	}
	
	public void reset() {
		image.getImage().flush();
	}
	
	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {      
		image=(ImageIcon) aInputStream.readObject();
        column=aInputStream.readInt();
        row=aInputStream.readInt();
        width=aInputStream.readInt();
        height=aInputStream.readInt();
    }
 
    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {

        aOutputStream.writeObject(image);
        aOutputStream.writeInt(column);
        aOutputStream.writeInt(row);
        aOutputStream.writeInt(width);
        aOutputStream.writeInt(height);
    }
	
}
