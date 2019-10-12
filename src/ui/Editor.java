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
	private FileDialog openDia, saveDia;// �򿪣������ļ�
	private File file;

	Editor(String filePath) {
		this.path=filePath;
		f = new Frame("����༭��");
		f.setBounds(200, 100, 600, 600);
		m = new Menu("�ļ�");
		submenu = new Menu("�Ӳ˵�");
		mb = new MenuBar();
		ta = new TextArea();
		saveItem = new MenuItem("����");
		closeItem = new MenuItem("�ر�");
		m.add(saveItem);
		m.add(closeItem);
		mb.add(m);
		f.setMenuBar(mb);// ���˵�������
		f.add(ta);
		openDia = new FileDialog(f, "��", FileDialog.LOAD);
		saveDia = new FileDialog(f, "����", FileDialog.SAVE);
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
				if(file.canWrite())	{		//�ļ���д
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(file));
						String text = ta.getText();
						bw.write(text);
						bw.close();
					} catch (IOException e2) {
						throw new RuntimeException("����ʧ��");
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "�ļ�ֻ��","����",JOptionPane.ERROR_MESSAGE);
				}
				
				
			}
		});
		f.addWindowListener(new WindowAdapter() {
 
			public void windowClosing(WindowEvent e) {
				
				f.dispose();
			}
 
		});
		closeItem.addActionListener(new ActionListener() {// ���̻�������
 
					public void actionPerformed(ActionEvent e) {
						f.dispose();
					}
				});
	}