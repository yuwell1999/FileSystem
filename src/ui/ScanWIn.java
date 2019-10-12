package ui;
import javax.swing.*;
public class ScanWIn extends JFrame{
	JLabel jlbscan=null;
	JLabel jlbcscan=null;
	JLabel jlbfscan=null;
	JLabel jlbnscan=null;
	JPanel jp=null;
	public ScanWIn(double num[]) {
		this.setTitle("各类磁盘调度算法");
		this.setVisible(true);
		this.setLayout(null);
		this.setBounds(200,300,300,200);
		
		jp=new JPanel();
		jp.setLayout(null);
		jp.setBounds(0,0,200,300);
		this.add(jp);
		
		jlbscan=new JLabel("SCAN:  "+num[0]);
		jlbcscan=new JLabel("CSCAN:  "+num[1]);
		jlbfscan=new JLabel("FSCAN:  "+num[2]);
		jlbnscan=new JLabel("NStepSCAN:  "+num[3]);
		
		jlbscan.setBounds(10,10,200,20);
		jlbcscan.setBounds(10,40,200,20);
		jlbfscan.setBounds(10,70,200,20);
		jlbnscan.setBounds(10,100,200,20);
		
		jp.add(jlbscan);
		jp.add(jlbcscan);
		jp.add(jlbfscan);
		jp.add(jlbnscan);
	}
}
