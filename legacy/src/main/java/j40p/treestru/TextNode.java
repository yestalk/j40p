package j40p.treestru;

public class TextNode extends XNode {
	
	public String data="empty";

	public TextNode() {}
	public TextNode(String text) {
		this.data=text;
	}
	
	public String toString(){
		return this.data;
	}
}
