package BTree;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class is used for writing the current BTree onto disk and reading data from disk to create 
 * a BTree in memory, since BTree is serializable.
 * @author May + Bill
 *
 */
public class BTreeHandler {
	/**
	 * Method writes a given Btree into disk that's serializable
	 * @param tree
	 * @param filename
	 * @return succesfully written to file?
	 */
	public static synchronized boolean writeTree(BTree tree, String filename){
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try
		{
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(tree);
			out.close();
			fos.close();
		}
		catch(IOException ex)
		{
			System.err.println("Deflate problem");
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * Method reads the BTree from disk into memory
	 * @param filename
	 * @return the BTree that's created
	 */
	public static synchronized BTree readTree(String filename){
		BTree tree;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try
		{
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			tree = (BTree)in.readObject();
			in.close();
			fis.close();
		}
		catch(IOException ex)
		{
			System.err.println("Inflate problem");
			ex.printStackTrace();
			return null;
		}
		catch(ClassNotFoundException ex)
		{
			System.err.println("Class not found");
			ex.printStackTrace();
			return null;
		}
		return tree;
	}

}
