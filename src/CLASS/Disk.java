package CLASS;
import CLASS.Disk.Block;

import java.awt.Color;
import java.awt.Dimension;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.*;

public class Disk {
	int disklength;
	int begin;
	Block block[]=null;		//磁盘块数组表示该磁道是否被占用
	public Disk() {
		begin=100;
		disklength=200;
		block=new Block [disklength+1];	
		init_block();		//分配有两百个磁道的磁盘
		
	}
	public class frdisk extends JFrame implements Runnable{
		
		JPanel jp=new JPanel();
		JLabel jlb[]=new JLabel[201];
		int x=0;
		int y=0;
		public frdisk() {
			this.setTitle("磁盘空间");
			this.setVisible(true);
			this.setBounds(200, 300, 670, 400);
			jp.setLayout(new BoxLayout(jp,BoxLayout.Y_AXIS));
			this.add(jp);
			jp.setSize(500,400);
			for(y=0;y<10;y++) {
				JPanel jp1=new JPanel();
				jp1.setLayout(new BoxLayout(jp1,BoxLayout.X_AXIS));
				for(x=1;x<=20;x++) {
					int loc=20*y+x;
					if(loc<10) {
						jlb[20*y+x]=new JLabel("        "+Integer.toString(loc));
					}
					else if(loc<100) {
						jlb[20*y+x]=new JLabel("     "+Integer.toString(loc));
					}
					else {
						jlb[20*y+x]=new JLabel("   "+Integer.toString(loc));

					}
					jlb[20*y+x].setPreferredSize(new Dimension(100,50));
					jlb[20*y+x].setOpaque(true);
					//System.out.println("   "+20*y+x);
//					System.out.println(block[20*y+x].bid+"      "+block[20*y+x].isavailable);
					if(block[20*y+x].isavailable) {
						jlb[20*y+x].setBackground(Color.gray);
					}
					else {
						jlb[20*y+x].setBackground(Color.red);
					}
					
					
					
					jlb[20*y+x].setBorder(BorderFactory.createLineBorder(Color.BLUE));
					jp.add(jp1);
					jp1.add(jlb[20*y+x]);
				}
			}
		}

		@Override
		public void run() {

		}
	}
	public void showframe() {
		frdisk d=new frdisk();
	}
	
	//初始化磁盘
	void init_block() {
		for(int j=0;j<201;j++) {
			
			block[j]=new Block();
			block[j].bid=j;
			//System.out.println(block[j].bid);
		}
	}
	//判断磁盘是否为满
	boolean isfull() {
		for(int i=1;i<=disklength;i++) {
			if(block[i].isavailable) 
				return false;
		}
		return true;
	}
	
	//磁盘调度算法SCAN
	public double SCAN(Vector<FCB> request) {
//		int direction=1;			//1表示方向自小到大
		int begin=this.begin;
		double Average_seek=0;
		double seeklength=0;
		TreeSet<Integer> ts1=new TreeSet<Integer>();
		TreeSet<Integer> ts2=new TreeSet<Integer>();
		if(request!=null) {
			for(FCB f:request) {
				if(f!=null) {
					if(f.alloc>=begin) {
						ts1.add(f.alloc);
					}
					else {
						ts2.add(f.alloc);
					}
				}
				
			}
			
			for(int temp:ts1) {
				seeklength+=Math.abs(begin-temp);
				
				for(int i=begin;i<=temp;i++) {
				}
				begin=temp;
			}
			
			for(int temp:ts2.descendingSet()) {
				
				seeklength+=Math.abs(begin-temp);
				
				
				
				for(int i=begin;i>=temp;i--) {
				}
				begin=temp;
			}
			
			Average_seek=(seeklength/(double)request.size());
			return Average_seek;
		}
		return 0;
	}
	
	//磁盘调度算法CSCAN
	public double CSCAN(Vector<FCB> request) {
		int begin=this.begin;
		System.out.println("  "+request.size());
		if(request.size()<=1) {
			
			double result=(double)Math.abs(begin-request.get(0).alloc);
			begin=request.get(0).alloc;
			return result;
		}
		
		double Average_seek=0;
		double seeklength=0;
		//begin=block[3].bid;
		TreeSet<Integer> ts1=new TreeSet<Integer>();
		TreeSet<Integer> ts2=new TreeSet<Integer>();
		
		for(FCB f:request) {
			if(f.alloc>=begin) {
				ts1.add(f.alloc);
			}
			else {
				ts2.add(f.alloc);
			}
		}
		
		//遍历找到ts1中的最大值和ts2中的最小值
		for(int temp:ts1) {
			seeklength+=Math.abs(begin-temp);
			for(int i=begin;i<=temp;i++) {
				
			}
			begin=temp;
		}
		
		seeklength+=(begin-ts2.descendingSet().last());
		begin=ts2.descendingSet().last();
		
		for(int temp:ts2) {
			seeklength+=Math.abs(begin-temp);
			for(int i=begin;i<=temp;i++) {
			
			}
			begin=temp;
		}
		
		Average_seek=(seeklength/(double)request.size());
		return Average_seek;
	}
	

	//磁盘调度算法NStepSCAN
	public double NStepSCAN(Vector<FCB> request,int n) {
		int begin=this.begin;
		Vector<FCB> temp=new Vector<FCB>();
		double Average_seek=0;
		double seeklength=0;
		
		//将申请队列分成长度为n的子队列
		if(request.size()==n) {
			return SCAN(request);
		}
		
		else if(request.size()!=n&&request.size()%n==0) {		
			//可整除n
			for(int i=0;i<request.size()/n;i++) {
				if(i*n+n<=request.size()) {
					for(int j=i*n;j<i*n+n;j++) {
						temp.add(request.get(j));
					}
				}
				else if(i*n+n>request.size()) {
					for(int j=i*n;j<request.size();j++) {
						temp.add(request.get(j));
					}
				}
				else if(request.size()<n) {
					for(int j=0;j<n;i++) {
						temp.add(request.get(j));
					}
					
				}
				seeklength+=SCAN(temp)*temp.size();
				temp=new Vector<FCB>();
			}
			
		}
		else if(request.size()%n!=0) {
			for(int i=0;i<request.size()/n;i++) {
				if(i*n+n<request.size()) {
					for(int j=i*n;j<i*n+n;j++) {
						if(request.get(j)!=null)
							temp.add(request.get(j));
					}
				}
				else if(i*n+n>request.size()) {
					for(int j=i*n;j<request.size();j++) {
						temp.add(request.get(j));
					}
				}
				else if(request.size()<n) {
					for(int j=0;j<n;i++) {
						temp.add(request.get(j));
					}
					
				}
				seeklength+=SCAN(temp)*temp.size();
				temp=new Vector<FCB>();
				
			}
		}
		Average_seek=(seeklength/(double)request.size());
		return Average_seek;
	}
	
	//磁盘调度算法FSCAN算法
	public double FSCAN(Vector<FCB> request) {
	
		return NStepSCAN(request,2);
	}
	
	public class Block {
		int bid=0;		//磁道号
		boolean isavailable=true;
		public Block(){
			bid=0;
			isavailable = true;
		}
	}

}


