package ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;


public class Editor {
	private Menu m, submenu;
	private MenuBar mb;
	private Frame f;
	private MenuItem closeItem, saveItem, openItem;
	private TextArea ta;
	String path;
	private FileDialog openDia, saveDia;// 打开，保存文件
	private File file;

	Editor(String filePath) {
		this.path=filePath;
		f = new Frame("虚拟编辑器");
		f.setBounds(200, 100, 600, 600);
		m = new Menu("文件");
		submenu = new Menu("子菜单");
		mb = new MenuBar();
		ta = new TextArea();
		saveItem = new MenuItem("保存");
		closeItem = new MenuItem("关闭");
		m.add(saveItem);
		m.add(closeItem);
		mb.add(m);
		f.setMenuBar(mb);// 将菜单加入表格
		f.add(ta);
		openDia = new FileDialog(f, "打开", FileDialog.LOAD);
		saveDia = new FileDialog(f, "保存", FileDialog.SAVE);
		try {
			file=new File(path);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				ta.append(line + "\r\n");
			}
			br.close();
		}catch(Exception e) {}
		Event();
		f.setVisible(true);
	}

	private void Event() {
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(file.canWrite())	{		//文件可写
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(file));
						String text = ta.getText();
						bw.write(text);
						bw.close();
					} catch (IOException e2) {
						throw new RuntimeException("保存失败");
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "文件只读","警告",JOptionPane.ERROR_MESSAGE);
				}
				
				
			}
		});
		f.addWindowListener(new WindowAdapter() {
 
			public void windowClosing(WindowEvent e) {
				
				f.dispose();
			}
 
		});
		closeItem.addActionListener(new ActionListener() {// 键盘或鼠标监听
 
					public void actionPerformed(ActionEvent e) {
						f.dispose();
					}
				});
	}