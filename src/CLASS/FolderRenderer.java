package CLASS;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FolderRenderer extends DefaultTreeCellRenderer { 
	private static FileSystemView fsView;
	private static final long serialVersionUID = 1L;
	public Component getTreeCellRendererComponent(JTree tree, Object value,boolean sel, boolean expanded, boolean leaf, int row,boolean hasFocus) {
		fsView = FileSystemView.getFileSystemView();
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) value;
		NodeData data = (NodeData) selectedNode.getUserObject();
		Icon icon = fsView.getSystemIcon(data.f);
		setLeafIcon(icon);
		setOpenIcon(icon);
		setClosedIcon(icon);
		return super.getTreeCellRendererComponent(tree, value, sel, expanded,leaf, row, hasFocus);
	}
}