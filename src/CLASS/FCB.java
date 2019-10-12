package CLASS;

import java.util.Random;

public class FCB {
	String path=null;
	int alloc=0;
	public FCB(Disk d,String p) {
		path=p;
		malloc(d);
		System.out.println(p+"  "+d);
	}
	
	public int getalloc() {
		return alloc;
	}
	//为该文件分配磁盘空间
	boolean malloc(Disk d) {
		int rannum;
		if(d.isfull()) {
			System.out.println("ful");
			return false;
		}	
		do {
			rannum=new Random().nextInt(d.disklength)+1;
		}while(!d.block[rannum].isavailable);
		
	
		if(d.block[rannum]!=null)
			d.block[rannum].isavailable=false;
		alloc=rannum;
		System.out.println("    "+path);
		return true;
	}
	
	public void free(Disk d) {
		d.block[alloc].isavailable=true;
	}
}
