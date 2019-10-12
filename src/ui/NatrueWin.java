package ui;

import CLASS.FCB;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class NatrueWin extends JFrame implements ActionListener {
	JButton jbenter =null;
	JButton jbcancel =null;
	JPanel jp1=null;
	JTextField jtfname = null;
	Image img=null;
	Icon icfile=null;
	JLabel lbfile=null;
	JLabel lbtype=null;
	JLabel lbmemory=null;
	JLabel lblocation=null;
	JCheckBox jcbread=null;
	JLabel lbpath=null;
	JLabel lballoc=null;
	File temp=null;
	boolean isrename=false;
	
	public NatrueWin(File file,HashMap<String,FCB> fcbmap){
		temp=file;
		this.setVisible(true);
		this.setTitle("属性");
		this.setLayout(null);
		this.setBounds(300, 200,300,500);
		jp1=new JPanel();
		this.setLayout(null);
		this.add(jp1);
		
		jp1.setLayout(null);
		jp1.setBounds(0, 0, 300, 400);

		jtfname=new JTextField(file.getName(),50);
		jtfname.setBounds(100, 10, 150, 20);
		jtfname.setEditable(false);
		//lbfile
		jbenter =new JButton("确定");
		jbcancel =new JButton("取消");
		lbfile=new JLabel();
		icfile=FileSystemView.getFileSystemView().getSystemIcon(file);
		
		lbfile.setIcon(icfile);
		
		jp1.add(lbfile);
		jp1.add(jtfname);
		lbfile.setBounds(30, 10, 20, 20);

		if(!file.isDirectory()) {
			String suffix = file.getAbsolutePath().split("\\.")[1];
			lbtype=new JLabel("文件类型:                  "+suffix);
			if(fcbmap.containsKey(file.getAbsolutePath())) {
				System.out.println("sus");
				lballoc =new JLabel("分配磁道：               "+fcbmap.get(file.getAbsolutePath()).getalloc());
				lballoc.setBounds(20, 180, 500, 15);
				jp1.add(lballoc);
			}
			if(!file.canWrite()) {
				//若只读的话
				jcbread=new JCheckBox("只读",true);
			}
			else {
				jcbread=new JCheckBox("只读");
			}
			jp1.add(jcbread);
			jcbread.setBounds(20,240,60,20);
		}
		else {
			lbtype=new JLabel("文件类型:                  "+"文件夹");
		}
		
		jp1.add(lbtype);
		lbtype.setBounds(20, 45, 500, 50);
		//lbmemory
		lbmemory=new JLabel("文件大小:                  "+(double)file.length()/1024+" KB");
		jp1.add(lbmemory);
		lbmemory.setBounds(20,100,500,15);
		
		//lbpath
		lbpath=new JLabel("文件路径:  "+file.getAbsolutePath().toString());
		jp1.add(lbpath);
		lbpath.setBounds(20,140,500,15);
		//jbenter
		//jbcancel
		this.add(jbenter);
		this.add(jbcancel);
		jbenter.setBounds(150,400,60,30);
		jbcancel.setBounds(220,400,60,30);
		jbenter.addActionListener(this);
		jbcancel.addActionListener(this);

		this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent arg)
            {
            	if(!file.isDirectory()) {
            		if(jcbread.isSelected()) {
                    	file.setReadOnly();
                    }else {
                    	file.setWritable(true);
                    }
            	}
                
            }
        });
	}
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		if(e.getSource()==jbcancel) {
			this.dispose();
		}
		if(e.getSource()==jbenter) {
			if(!temp.isDirectory()) {
				if(jcbread.isSelected()) {
	             	temp.setReadOnly();
	             }else {
	             	temp.setWritable(true);
	             }
			} 
			 this.dispose();
		}
	}
}
