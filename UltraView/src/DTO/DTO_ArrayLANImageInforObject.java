package DTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class DTO_ArrayLANImageInforObject implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public ArrayList<DTO_LANImageInforObject> arr;
	
	public DTO_ArrayLANImageInforObject() {
		arr=new ArrayList<DTO_LANImageInforObject>();
	}
	public void Add(DTO_LANImageInforObject oip) {
		arr.add(oip);
	}

	public void Clear() {
		arr.clear();
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {      
        arr= (ArrayList<DTO_LANImageInforObject>) aInputStream.readObject();
        
    }
 
    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
        aOutputStream.writeObject(arr);
    }
}
