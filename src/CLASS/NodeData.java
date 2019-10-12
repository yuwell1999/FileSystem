package CLASS;
import java.io.File;

public class NodeData
{
	public File f;
	public String Name;
	public NodeData(File f,String Name){
		this.f = f;
		this.Name = Name;
	}
	
	public NodeData(File file) {
		this.f = file;
	}
	 
	public String toString(){
		return Name;
	}
	
	public void ChangeString(String s){
		Name = s;
	}
}
