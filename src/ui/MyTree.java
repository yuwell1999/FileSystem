package ui;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import CLASS.Disk;
import CLASS.Disk.frdisk;
import CLASS.FolderRenderer;
import CLASS.NodeData;
import CLASS.FCB;

public class MyTree implements ActionListener
{
	int listsize=0;
	Disk disk=new Disk();
	HashMap<String,FCB> fcbmap = new HashMap<String,FCB>();
	Vector<FCB> scanlist=new Vector<FCB>();
	boolean ismultchoose=false;
	JButton jbDisk =new JButton("���̿ռ�");
	JButton jbSCAN =new JButton("SCAN");
	JButton jbCSCAN =new JButton("CSCAN");
	JButton jbFSCAN =new JButton("FSCAN");
	JButton jbNStepSCAN =new JButton("NStepSCAN");
	JButton jbcompare=new JButton("�Ƚϵ����㷨");
	File copyFile=null;
	TreePath pastePath;
	TreePath copyPath;
	JFrame jf;
	JTree tree;
	JTable table;
	Object[][] list={{}};
	DefaultTableModel tableModel;
	DefaultMutableTreeNode parent;
	DefaultTreeModel model;
	DefaultMutableTreeNode root = new DefaultMutableTreeNode(new NodeData(null,"Ŀ¼�б�"));
	DefaultMutableTreeNode tNode;
	static DefaultMutableTreeNode Node;
	static DefaultMutableTreeNode temp;
	static String path="F:\\�����";
	
	PopupMenu pop = new PopupMenu();
	MenuItem openItem = new MenuItem("�༭");
	MenuItem deleteItem = new MenuItem("ɾ��");
	MenuItem renameItem = new MenuItem("������");
	MenuItem createfolderItem = new MenuItem("�½��ļ���");
	MenuItem newtxtItem = new MenuItem("�½�txt�ļ�");
	MenuItem natrueItem = new MenuItem("����");
	MenuItem copyItem=new MenuItem("����");
	MenuItem pasteItem=new MenuItem("ճ��");
	MenuItem readOnlyItem=new MenuItem("���Ŀɶ�����");
	
	public DefaultMutableTreeNode traverseFolder(String path) {
		//�ݹ�����ļ���
		DefaultMutableTreeNode parent = new DefaultMutableTreeNode(new File(path).getName());
        File file = new File(path);
        
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                if(file.isDirectory()) {
                	//����ǿ��ļ���
                	DefaultMutableTreeNode dn=new DefaultMutableTreeNode(file.getName(), false);
                	return dn;
                }
            }else{
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                    	parent.add(traverseFolder(file2.getAbsolutePath()));
                    }else{
                    	//��δ�������ļ���fcb
                    	initfcb(file2);
                    	temp=new DefaultMutableTreeNode(file2.getName());
                    	parent.add(temp);
                    }
                }
            }
        } else {
            return null;
        }
		return parent;
    }
	public MyTree()
	{
		JFrame jf = new JFrame("�����ļ�ϵͳ");
		jf.setSize(1000, 800);
		DefaultMutableTreeNode parent = new DefaultMutableTreeNode(new File(path).getName());
        File file = new File(path);
        System.out.println(path);

        System.out.println(file.getAbsolutePath());
        File[] roots = file.listFiles(); 
        //����Ŀ¼���������ļ�����fcb���������
        traverseFolder(file.getAbsolutePath());
        
		for(int i = 0;i < roots.length;i++){
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(new NodeData(roots[i],roots[i].getAbsolutePath()));	 
			root.add(node);
		}
		tree = new JTree(root);
		model = (DefaultTreeModel) tree.getModel();
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		tree.setCellRenderer(new FolderRenderer());
		String[] col = { "�ļ���", "����", "��С","�Ƿ�ɶ�"};
		tableModel = new DefaultTableModel(list, col); 
		table = new JTable(tableModel);
		table.setRowHeight(50);
		//table.setEnabled(false);
		 JScrollPane scrollTable = new JScrollPane(table);//��ʾ��Ϊtable���Ŀɹ������
		 scrollTable.setPreferredSize(new Dimension(500, 500));
		 jf.add(BorderLayout.CENTER,scrollTable);//ʹ�ñ߽粼��
		
	     model.addTreeModelListener(new TreeModelListener(){
			public void treeNodesChanged(TreeModelEvent e){
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
				DefaultMutableTreeNode node;
				try {
					int []index = e.getChildIndices();
					node = (DefaultMutableTreeNode)(parent.getChildAt(index[0]));
					((NodeData)tNode.getUserObject()).ChangeString(node.toString());
					model.removeNodeFromParent(node);
					model.insertNodeInto(tNode,parent,index[0]);
				}catch (NullPointerException e1){
				 }

				NodeData data = (NodeData) tNode.getUserObject();
				String tt = data.f.getParent() + "//";
				tt = tt + tNode.toString();
				File newfile = new File(tt);
				data.f.renameTo(newfile);
				data.f = newfile;
				return;
			}
	 
			public void treeStructureChanged(TreeModelEvent e){}
			public void treeNodesRemoved(TreeModelEvent e){} 
            public void treeNodesInserted(TreeModelEvent e){}
		});
	 
		jf.add(pop);
		//Ϊpop��Ӳ˵���
	    pop.add(openItem);
		pop.addSeparator();
		pop.add(copyItem);
		pop.addSeparator();
		pop.add(pasteItem);
		pop.addSeparator();
		pop.add(deleteItem);
		pop.addSeparator();
		pop.add(renameItem);
		pop.addSeparator();	
		
		pop.add(createfolderItem);
		pop.addSeparator();
		pop.add(natrueItem);
		pop.addSeparator();
		pop.add(newtxtItem);
	 
		//�������ı��༭����
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("ok");
				//��ȡѡ�нڵ�
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				TreePath folderPath = tree.getSelectionPath();
				System.out.println(topath(folderPath.toString()));
				File f=new File(topath(folderPath.toString()));
				if(topath(folderPath.toString()).endsWith(".txt")) {
					new Editor(topath(folderPath.toString()));
				}
				else {
					JOptionPane.showMessageDialog(null, "����༭��ֻ�ɴ��ı��ĵ�","����",JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		natrueItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				TreePath filePath = tree.getSelectionPath();
				String path=topath(filePath.toString());
				NatrueWin nw=new NatrueWin(new File(path),fcbmap);
			}
		});
		
		//�˵���deleteItem�Ķ���������
		deleteItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if(selectedNode == null) {
					return;
				}
				NodeData data = (NodeData) selectedNode.getUserObject();
				int n = JOptionPane.showConfirmDialog(tree,"ȷ��ɾ����?","ȷ�϶Ի���",JOptionPane.YES_NO_OPTION);
				if(n == JOptionPane.NO_OPTION)
					return;
				
				if(selectedNode == root){
					JOptionPane.showMessageDialog(tree,"��Ŀ¼���ܱ�ɾ����","����Ի���",JOptionPane.WARNING_MESSAGE);
					return;
				}else if(data.f.isFile()){
					delFile(data.f.getAbsolutePath()); 

				}else if(data.f.isDirectory()){
					if(selectedNode.getParent() != root) {
						delFolder(data.f.getAbsolutePath());//�ݹ�ɾ��
					}else{
						JOptionPane.showMessageDialog(tree,"ɾ��ʧ�ܣ�","����Ի���",JOptionPane.WARNING_MESSAGE);
						return;
					}
				 }
				 model.removeNodeFromParent(selectedNode);
			}
	    });
		
		//�˵���newtxtItem�Ķ��������
		newtxtItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tree.setEditable(true);
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				TreePath folderPath = tree.getSelectionPath();
				if(folderPath==null) {
					return;
				}

				String p1=folderPath.toString();
				p1=topath(p1);
				File fatherfile = new File(p1);
				if(!fatherfile.isDirectory()) {
					JOptionPane.showMessageDialog(null,"����ʧ��","����",JOptionPane.ERROR_MESSAGE);
					return;
				}
				String newTxtName=JOptionPane.showInputDialog("�������½��������ļ���");
				if(newTxtName==null) {
					return;
				}
				File newfile = new File(p1+"\\\\"+newTxtName+".txt");
				if(!newfile.exists()) {
					try {
						newfile.createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new NodeData(newfile,newTxtName+".txt"));
				model.insertNodeInto(newNode,selectedNode,selectedNode.getChildCount());
				initfcb(newfile);
			}
		});
		
		//�˵���createfolderItem�Ķ��������
		createfolderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newFolder=JOptionPane.showInputDialog("�������½����ļ�����");
				if(newFolder==null) {
					return;
				}
				tree.setEditable(true);
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				System.out.println(selectedNode.toString());
				TreePath folderPath = tree.getSelectionPath();
				if(folderPath==null) {
					return;
				}
				
				String p1=folderPath.toString();
				System.out.println(p1);
				p1=topath(p1);
				File newfile= new File(p1+"\\\\"+newFolder);
				if(!newfile.exists()) {
	            	newfile.mkdir();
	            }
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new NodeData(newfile,newFolder));
				model.insertNodeInto(newNode,selectedNode,selectedNode.getChildCount());	            
			}
		});
	
		//�˵���renameItem�Ķ��������
		renameItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				tree.setEditable(true);
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				TreePath editPath = tree.getSelectionPath();
				if(selectedNode == null) {
					return;
				}
				tree.startEditingAtPath(editPath);
				tNode = (DefaultMutableTreeNode) selectedNode.clone();
			}
		});
		 
		//�˵���copyItem�Ķ���������
				copyItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tree.setEditable(true);
						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
						copyPath = tree.getSelectionPath();
						System.out.println("����Դ��ַ��"+topath(copyPath.toString()));
					}
				});
				
				//�˵���pasteItem�Ķ���������
				pasteItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)  {
						//System.out.println(copyPath);
						tree.setEditable(true);
						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
						pastePath = tree.getSelectionPath();
						if(pastePath==null||copyPath==null) {
							return;
						}
						String copyPath1 = topath(copyPath.toString());
						String pastePath1 = topath(pastePath.toString());
						
						System.out.println("����Ŀ�ĵ�ַ��"+topath(pastePath.toString()));
						//System.out.println(copyPath1.lastIndexOf("\\"));
						String pasteFolder=copyPath1.substring(copyPath1.lastIndexOf("\\")+1,copyPath1.length());
						System.out.println("���Ƶ��ļ�������Ϊ��"+pasteFolder);
						
						File srcFile = new File(copyPath1);
						File dstFile = new File(pastePath1);
						try{
							copyFolder(srcFile,dstFile);
						}catch(Exception ioe) {
							
						}
						File newfile=new File(pastePath1+"\\\\"+pasteFolder);
						DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new NodeData(newfile,pasteFolder));
						int count=selectedNode.getChildCount();
						for(int i=0 ;i<count;i++) {
							if(newNode.toString().equals(selectedNode.getChildAt(i).toString())) {
								return;
							}
							if(newNode.toString().contains(selectedNode.toString())) {
								return;
							}
						}
						model.insertNodeInto(newNode,selectedNode,selectedNode.getChildCount());
					}
				});
		
		
//		�ڵ������¼�������
		MouseListener ml = new MouseAdapter(){
			
			
			//***********************************
			public void mousePressed(MouseEvent e){//���Ҽ�����ʱ��Ҳѡ��
				if(e.isControlDown()) {
					TreePath tp = tree.getPathForLocation(e.getX(),e.getY());
					tree.addSelectionPath(tp);
					String rp=topath(tp.toString()).replace("\\\\", "\\");
					
					if(fcbmap.get(rp)!=null) {
						if(!scanlist.contains(fcbmap.get(rp))) {
							listsize++;
							scanlist.add(fcbmap.get(rp));
							System.out.println("1");
						}
					}
				}else {
					TreePath tp = tree.getPathForLocation(e.getX(),e.getY());
					if(tp == null) {
						return;
					} 
					tree.setSelectionPath(tp);*
					scanlist.clear();
					listsize=0;
					String rp=topath(tp.toString()).replace("\\\\", "\\");
					if(fcbmap.get(rp)!=null) {
						if(!scanlist.contains(fcbmap.get(rp))) {
							scanlist.add(fcbmap.get(rp));
						}
						
					}
					if(SwingUtilities.isRightMouseButton(e)) {
						return;
					}
					if(tree.isExpanded(tp)){   
						tree.collapsePath(tp);
						
		            }else{
		            	tree.expandPath(tp); 
		            }	
				}
				
			}
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() >= 2){
					TreePath tp = tree.getPathForLocation(e.getX(),e.getY());
					if(tp != null){
						DefaultMutableTreeNode temp = (DefaultMutableTreeNode) tp.getLastPathComponent();//���ѡ�нڵ�
						if(temp == null) {
							return;
						}
						if(temp.getUserObject() == temp.getUserObject().toString()){
							return;
						}
						NodeData data = (NodeData) temp.getUserObject();
						if(tp != null && data.f.isFile()){ 
							try{
								Runtime ce = Runtime.getRuntime();
								String Temp = new String(data.f.getParent());
								Temp = Temp + "\\";
								Temp = Temp + data.f.getName();
								String cmdarray = "cmd /c start " + Temp;
								ce.exec(cmdarray);
							}catch(Exception ee){ 
								System.out.println(ee);
							}
						}
				    }
				}
			}
	 
			public void mouseReleased(MouseEvent e){
				if(e.isPopupTrigger()){
					TreePath tp = tree.getPathForLocation(e.getX(),e.getY());
					if(tp == null) {
						return;			
					}
					pop.show(tree,e.getX(),e.getY());
				}
			}
	 
		};
	 
		tree.addMouseListener(ml);
		tree.addTreeSelectionListener(new TreeSelectionListener(){
		public void valueChanged(TreeSelectionEvent e){
			TreePath movepath = (TreePath)e.getNewLeadSelectionPath();
			if(movepath == null) {
				return;
			}
			DefaultMutableTreeNode temp = (DefaultMutableTreeNode) movepath.getLastPathComponent();
			if(temp == null){
				return;
			}
			if(temp.getUserObject() == temp.getUserObject().toString()){
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				System.out.println(tNode.toString());
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
				int selectedIndex = parent.getIndex(selectedNode);
				System.out.println("Object of getUserObject() has been changed");
				((NodeData)tNode.getUserObject()).ChangeString(selectedNode.toString());
				model.removeNodeFromParent(selectedNode);
				model.insertNodeInto(tNode,parent,selectedIndex + 1);
				NodeData data = (NodeData) tNode.getUserObject();
				String tt = data.f.getParent() + "//";
				tt = tt +tNode.toString();
				data.f.renameTo(new File(tt));
				tree.setSelectionRow(selectedIndex + 1);
				return;
			 
			}
			NodeData data = (NodeData) temp.getUserObject();
			if(data.f != null){
				if(data.f.isDirectory()){
					File[] RRoots = data.f.listFiles();
					if(temp.isLeaf()) {
						for(int j = 0;j < RRoots.length;j ++){
							DefaultMutableTreeNode NNode = new DefaultMutableTreeNode(new NodeData(RRoots[j],RRoots[j].getName()));
							model.insertNodeInto(NNode,temp,temp.getChildCount());//����½ڵ㲢�Զ�ˢ��
						}
					}
					tableModel.setRowCount(0);
					list = fu(RRoots);
					for (int i = 0; i < RRoots.length; i++) {
						tableModel.addRow(list[i]);
					}
				}else if(data.f.isFile()){
					
				}
			}else{

			}
	 
	    }	    
	 
		});
		 
		JScrollPane scrollTree = new JScrollPane(tree);
		scrollTree.setPreferredSize(new Dimension(200,200));
		jf.add(BorderLayout.WEST,scrollTree);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);

		JMenuBar toba=new JMenuBar();
		//toba.setFloatable(false);
	    Color cl=new Color(255,255,250);
		toba.setBackground(cl);
		
		jf.add(BorderLayout.NORTH,toba);

		toba.add(jbDisk);
		jbDisk.setBorderPainted(false);
		jbDisk.setBackground(cl);
		toba.add(jbSCAN);
		
		jbSCAN.setBackground(cl);
		jbSCAN.setBorderPainted(false);
		
		jbCSCAN.setBackground(cl);
		jbCSCAN.setBorderPainted(false);
		toba.add(jbCSCAN);
		
		jbFSCAN.setBackground(cl);
		jbFSCAN.setBorderPainted(false);
		toba.add(jbFSCAN);
		
		jbNStepSCAN.setBackground(cl);
		jbNStepSCAN.setBorderPainted(false);
		toba.add(jbNStepSCAN);
		jbcompare.setBackground(cl);
		jbcompare.setBorderPainted(false);
		toba.add(jbcompare);
		
		jbSCAN.addActionListener(this);
		jbCSCAN.addActionListener(this);
		jbFSCAN.addActionListener(this);
		jbNStepSCAN.addActionListener(this);
		jbDisk.addActionListener(this);
		jbcompare.addActionListener(this);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jbSCAN) {
			if(!scanlist.isEmpty()) {
				double i=disk.SCAN(scanlist);
				scanlist.clear();
				tree.clearSelection();
				JOptionPane.showMessageDialog(null,"ƽ��Ѱ������Ϊ��"+i);
			}
		}
		if(e.getSource()==jbCSCAN) {
			if(!scanlist.isEmpty()) {
				double i=disk.CSCAN(scanlist);
				scanlist.clear();
				tree.clearSelection();
				JOptionPane.showMessageDialog(null,"ƽ��Ѱ������Ϊ��"+i);
			}
		}
		if(e.getSource()==jbFSCAN) {
			if(!scanlist.isEmpty()) {
				double i=disk.FSCAN(scanlist);
				scanlist.clear();
				tree.clearSelection();
				JOptionPane.showMessageDialog(null,"ƽ��Ѱ������Ϊ��"+i);
			}
			
		}
		if(e.getSource()==jbNStepSCAN) {
			if(!scanlist.isEmpty()) {
				String n=JOptionPane.showInputDialog("������ÿ���Ӷ��еĳ���n");
				double i=disk.NStepSCAN(scanlist,Integer.valueOf(n));
				System.out.println(i);
				scanlist.clear();
				tree.clearSelection();
				if(i==-1) {
					JOptionPane.showMessageDialog(null, "���г��Ȳ���","����",JOptionPane.ERROR_MESSAGE);

				}
				else {
					JOptionPane.showMessageDialog(null,"ƽ��Ѱ������Ϊ��"+i);

				}
			}
			
		}
		if(e.getSource()==jbDisk) {
			disk.showframe();
		}
		if(e.getSource()==jbcompare) {
			if(!scanlist.isEmpty()) {
				String n=JOptionPane.showInputDialog("������ÿ���Ӷ��еĳ���n");
				double scan[]=new double[100];
				scan[0]=disk.SCAN(scanlist);
				scan[1]=disk.CSCAN(scanlist);
				scan[2]=disk.FSCAN(scanlist);
				scan[3]=disk.NStepSCAN(scanlist, Integer.valueOf(n));
				ScanWIn sw=new ScanWIn(scan);
				scanlist.clear();
				tree.clearSelection();
			}
		}
	}
	String topath(String treepath) {
		String path=treepath.substring(6);
		path=path.replaceAll(",", "\\\\");
		path=path.replaceAll(" ", "");
		path=path.replace("]","" );
		path=path.replace("\\","\\\\");
		return path;
	}
	
    void initfcb(File f) {
		if(f.isFile()&&!fcbmap.containsKey(f.getAbsolutePath())) {
			FCB fcb = new FCB(disk,f.getAbsolutePath());
			fcbmap.put(f.getAbsolutePath(), fcb);
			System.out.println(f.getAbsolutePath());
			return;
		}
	}

	public void readfiles(File file, DefaultMutableTreeNode node) {
		File list[] = file.listFiles();
		if (list == null){
			return;
		}
		for (int i = 0; i < list.length; i++) {
		   File file_inlist = list[i];
		   if (file_inlist.isDirectory()) {
			   parent = new DefaultMutableTreeNode(new NodeData(file_inlist));
			   // ��ӿհ��ļ��нڵ� ʹ�ӽڵ���ʾΪ�ļ���
			   File stubadd = null;
			   DefaultMutableTreeNode stub = new DefaultMutableTreeNode(stubadd);
			   parent.add(stub);
			   node.add(parent);
		   }else{
			   DefaultMutableTreeNode son = new DefaultMutableTreeNode(
			   new NodeData(file_inlist));
			   node.add(son);
		    }
		  }
    }
 
	//��ȡ�ļ��Ĵ�С
	 public String size(File file) throws IOException {
		 String sizefile = (file.length()/1024.0) + "KB";
		 return sizefile;
	 }
 
	 public Object[][] fu(File[] file) {
		  Object[][] m = new Object[file.length][4];
		  for (int i = 0; i < file.length; i++) {
			  m[i][0] = file[i].getName();
			  if (file[i].isDirectory()) {
				  m[i][1] = "�ļ���";
			  }else {
				  String fileName = file[i].getName();
				  String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
				  m[i][1] = fileType;
			   }
			   //�ļ���С
			   try {
				   if (file[i].isDirectory()) {
					   m[i][2] = " ";
		           }else {
		        	   m[i][2] = size(file[i]);
		            }
				   if(file[i].canWrite()) {
					   m[i][3]="�ɶ�д";
				   }else {
					   m[i][3]="ֻ��";
				   }
				   }catch (IOException e) {
					    e.printStackTrace();
			   }
		  }
		  return m;
		 
	 }

	public void delFile(String filePathAndName) {  
		//ɾ���ļ�
		try{
			String  filePath  =  filePathAndName; 
			filePath  =  filePath.toString();
			
			java.io.File  myDelFile  =  new  java.io.File(filePath);
			FCB f=fcbmap.get(filePath);
    		f.free(disk);
			myDelFile.delete();
		}catch  (Exception  e){
			System.out.println("ɾ���ļ���������");
		 }  
	}  
 
	public  void  delFolder(String  folderPath)  {
		//ɾ���ļ���
		try{
			delAllFile(folderPath);  //ɾ����������������
			String  filePath  =  folderPath;
			filePath  =  filePath.toString();
			java.io.File  myFilePath  =  new  java.io.File(filePath);
			myFilePath.delete();  //ɾ�����ļ���  
		}catch(Exception e){  
				System.out.println("ɾ���ļ��в�������"); 
			 }  
	}  
 
	public  void  delAllFile(String  path)  {  
		//ɾ���ļ�������������ļ�  
		File  file  =  new  File(path); 
		if(!file.exists()){
			return;
		}  
		if(!file.isDirectory()){  
			return;
		}
		String[] tempList = file.list();  
		File  temp  =  null;  
		for(int i=0;i<tempList.length;i++){
			if  (path.endsWith(File.separator)){
				temp  =  new  File(path  +  tempList[i]); 
			}else{ 
				temp  =  new  File(path  +  File.separator  +  tempList[i]);
			}
			if(temp.isFile()){ 
				temp.delete(); 
			}  
			if(temp.isDirectory()){
				delAllFile(path+"/"+  tempList[i]);//��ɾ���ļ���������ļ�
				delFolder(path+"/"+  tempList[i]);//��ɾ�����ļ���
			}  
		 
		}  
	}
	
    private static void copyFolder(File srcFile, File destFile) throws IOException {
		
		if(srcFile.isDirectory()){
			File newFolder=new File(destFile,srcFile.getName());
			newFolder.mkdirs();
			File[] fileArray=srcFile.listFiles();
			for(File file:fileArray){
				copyFolder(file, newFolder);
			}
			
		}else{
			File newFile=new File(destFile,srcFile.getName());
			copyFile(srcFile,newFile);
		}
		
	}
 
	private static void copyFile(File srcFile, File newFile) throws IOException{
		// TODO Auto-generated method stub
		BufferedInputStream bis=new BufferedInputStream(new FileInputStream(srcFile));
		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(newFile));
		
		byte[] bys=new byte[1024];
		int len=0;
		while((len=bis.read(bys))!=-1){
			bos.write(bys,0,len);
		}
		bos.close();
		bis.close();
	}

	public static void main(String args[]){ 
		new MyTree();
	}
}