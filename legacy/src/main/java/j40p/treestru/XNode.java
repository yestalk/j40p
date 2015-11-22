package j40p.treestru;

import java.util.LinkedList;


public class XNode  {

	public static final byte BLACK=(byte)254;
	public static final byte RED=0;
	
	public static final int left=0;public static final int predecessor=left;
	public static final int right=1;public static final int successor=right;
	public static final int parent=2;
	
	private XNode[] lnk=new XNode[3];
	private byte color;
	private int size=0;
	
	private XNode nil;
	private XNode rootChild;
	private XNode upper;
	
	
	private int data;
	private static int gdata;
	
	public XNode(){
		XNode lnil =new XNode(false);
		this.nil=lnil;		
		this.rootChild=lnil;
		this.upper=lnil;
		
		//this.color=XNode.RED;
		//this.size=1;
		this.data=XNode.gdata++;
	}
	
	private XNode(boolean nil){
		for(int i=0;i<=2;i++){
			this.lnk[i]=this;
		}
		this.nil=this;		
		this.rootChild=this;
		this.upper=this;
		
		this.color=XNode.BLACK;
		this.size=0;
		this.data=-100;
	}

	public void addFirst(XNode nd){
		if(nd.upper!=nd.nil)
			throw new RuntimeException("add in node must delete  from other list first.");

		this.initWithUpper(nd,this);
		
		
		if(this.rootChild==this.nil){
			this.rootChild=nd;
			nd.color=XNode.BLACK;
			return;
		}
		
		
		XNode first =this.rootChild;
		while(first.lnk[XNode.left]!=this.nil)
			first=first.lnk[XNode.left];
		first.lnk[XNode.left]=nd;
		nd.lnk[XNode.parent]=first;
		
		this.resizePath(nd, true);
		this.fixBRInsertion(nd);
//		System.out.println("\r\n***\r\nrootchild left size:___"+this.rootChild.lnk[left].size);
//		System.out.println("rootchild right size:___"+this.rootChild.lnk[right].size);
//		System.out.println("rootchildsz:___"+this.rootChild.size);
//		System.out.println("rootchild data:___"+this.rootChild.data);
//		
//		System.out.println("\r\n--------------");
	}

	public void addLast(XNode nd){
		if(nd.upper!=nd.nil)
			throw new RuntimeException("add in node must delete  from other list first.");
		
		this.initWithUpper(nd,this);
		
		if(this.rootChild==this.nil){
			this.rootChild=nd;
			nd.color=XNode.BLACK;
			return;
		}
		
		XNode last =this.rootChild;
		while(last.lnk[right]!=this.nil)
			last=last.lnk[right];
		last.lnk[right]=nd;
		nd.lnk[parent]=last;
		
		this.resizePath(nd, true);
		this.fixBRInsertion(nd);
//		System.out.println("\r\n***\r\nrootchild left size:___"+this.rootChild.lnk[left].size);
//		System.out.println("rootchild right size:___"+this.rootChild.lnk[right].size);
//		System.out.println("rootchildsz:___"+this.rootChild.size);
//		System.out.println("rootchild data:___"+this.rootChild.data);
//		
//		System.out.println("\r\n--------------");
	}

	public void addBefore(XNode axis, XNode nd){
		if(nd.upper!=nd.nil)
			throw new RuntimeException("add in node must delete  from other list first.");
		if(axis.upper!=this){
			//System.out.println("\r\naxis.data:____"+axis.data);
			throw new RuntimeException("axis must be a member of list");
		}
		this.initWithUpper(nd,this);
		
		if(this.rootChild==this.nil){
			this.rootChild=nd;
			nd.color=XNode.BLACK;
			return;
		}
		
		
		if(axis.lnk[left]==this.nil){
			axis.lnk[left]=nd;
			nd.lnk[parent]=axis;
		}else{
			XNode predecessor = this.getAdjacency(axis, XNode.predecessor);
			predecessor.lnk[right]=nd;
			nd.lnk[parent]=predecessor;
		}
		

		
		this.resizePath(nd, true);
		this.fixBRInsertion(nd);
	}
	public void addAfter(XNode axis, XNode nd){
		if(nd.upper!=nd.nil)
			throw new RuntimeException("must delete  from other list first.");
		if(axis.upper!=this){
			//System.out.println("\r\naxis.data:____"+axis.data);
			throw new RuntimeException("\r\naxis must be a member of list");
		}
		this.initWithUpper(nd,this);
		
		if(this.rootChild==this.nil){
			this.rootChild=nd;
			nd.color=XNode.BLACK;
			return;
		}
		
		
		if(axis.lnk[right]==this.nil){
			axis.lnk[right]=nd;
			nd.lnk[parent]=axis;
		}else{
			XNode successor = this.getAdjacency(axis, XNode.successor);
			successor.lnk[left]=nd;
			nd.lnk[parent]=successor;
		}

		
		this.resizePath(nd, true);
		this.fixBRInsertion(nd);
	}

	public void delete(XNode nd){
		
		if(nd.upper!=this){
			System.out.println("delete target must belong to this list");
			return;
			//throw new RuntimeException();
		}
		
		if(this.rootChild==this.nil)
			return;
		XNode eventsource = this.innerDeletion(nd); // nd identity is switched with its filler, and removed from tree, 
		//System.out.println("eventsourse:___"+eventsource.data);
		//System.out.println("eventsoursetype:___"+(eventsource==XNode.nil));
		
		if(nd.color==XNode.BLACK){//16 if color[y] = BLACK // the color nd (now)carried(switched with filler) also been removed form tree, hence might cause black-red rule violation.
			this.fixBRDeletion(eventsource);//then RB-DELETE-FIXUP(T, x) // x is residue, the eventsource.
		}
		nd.upper=nd.nil;
		//this.fixNil();
		//System.out.println("!!!!!!!!!!size after deletion:___"+this.getChildrenSize());
	}
	
	public XNode nextSibling(){ 
		if(this.upper==this.nil)
			return null;
		XNode rt = this.getAdjacency(this, XNode.successor);
		if(rt==this.upper.nil)
			return null;
		else
			return rt;
	}
	public XNode previousSibling(){
		if(this.upper==this.nil)
			return null;
		XNode rt = this.getAdjacency(this, XNode.predecessor);
		if(rt==this.upper.nil)
			return null;
		else
			return rt;
	}
	public int getIndex(XNode subject){
		//System.out.println("inital root child size:___"+this.rootChild.size);
		//int debugct = 0;
		if(subject.upper!=this){
			//System.out.println("subject must be member of this list.");
			throw new RuntimeException("subject must be member of this list.");
		}
		int tempdx = subject.lnk[left].size;//1  r ← size[left[x]] + 1
		//XNode subject=nd;//2  y ← x
		while(subject!=this.rootChild){//3  while y ≠ root[T]
//			if(debugct>1000){
//				System.out.println("root child:___"+this.rootChild.data);
//				System.out.println("root child size:___"+this.rootChild.size);
//				throw new RuntimeException("............");
//			}
			if(subject==subject.lnk[XNode.parent].lnk[XNode.right]){//4      do if y = right[p[y]]
				tempdx=tempdx+subject.lnk[XNode.parent].lnk[XNode.left].size+1;//5           then r ← r + size[left[p[y]]] + 1
			}
			subject=subject.lnk[parent];//6        y ← p[y]
			//debugct++;
		}
		return tempdx;//7  return r

	}
	public XNode getNodeByIndex(int index){
		
		return this.iSelect(this.rootChild, index);
	}

	public int getChildrenSize(){
		return this.rootChild.size;
	}
	
	
	private void yieldIdentityTo(XNode source,XNode replacement){
		if(source==this.nil)
			throw new RuntimeException("can't switch nil identity.");

		XNode lparent=source.lnk[XNode.parent];
		XNode lleft=source.lnk[XNode.left];
		XNode lright =source.lnk[XNode.right] ;
		byte lcolor = source.color;
		int lsize = source.size;

		if(source==this.rootChild){
			this.rootChild=replacement;
		}else if(lparent.lnk[XNode.left]==source)
			lparent.lnk[XNode.left]=replacement;
		else
			lparent.lnk[XNode.right]=replacement;
		
//		source.lnk[XNode2.left].lnk[XNode2.parent]=replacement;
//		source.lnk[XNode2.right].lnk[XNode2.parent]=replacement;
		if(source.lnk[XNode.left]!=this.nil)
			source.lnk[XNode.left].lnk[XNode.parent]=replacement;
		if(source.lnk[XNode.right]!=this.nil)
			source.lnk[XNode.right].lnk[XNode.parent]=replacement;

		source.color=replacement.color;
		source.size=replacement.size;


		replacement.lnk[XNode.parent]=lparent;
		replacement.lnk[XNode.left]=lleft;
		replacement.lnk[XNode.right]=lright;
		replacement.color=lcolor;
		replacement.size=lsize;
		//nd.sublist=lsublist;
		//nd.data=ldata;
//		source.lnk[XNode.parent]=source.nil;//nd.parent;
//		source.lnk[XNode.left]=source.nil;//nd.left;
//		source.lnk[XNode.right]=source.nil;//nd.right;
	}



	private void initWithUpper(XNode subject,XNode upper){
		subject.upper=upper;
		subject.size=1;
		subject.color=XNode.RED;
		for(int i=0;i<=2;i++){
			subject.lnk[i]=upper.nil;
		}
	}



	private void resizePath(XNode event,boolean isIncrease){
//		int debugsize=0;
//		if(!isIncrease){
//			System.out.println("-resize - - - - ");
//			debugsize = this.getChildrenSize();
//		}
		if(event!=this.rootChild){
			while(event.lnk[parent]!=this.nil){

				event=event.lnk[parent];
				if(isIncrease)
					event.size++;
				else
					event.size--;
			}
		}
		
		
//		if(!isIncrease){
//			int postdsize = this.getChildrenSize();
//			boolean bg=postdsize!=(debugsize-1);
//			System.out.println("resize before:"+debugsize);
//			System.out.println("resize after:"+postdsize);
//			System.out.println("-resize before!=after-1_____"+bg);
//			if(bg){
//				throw new RuntimeException("resize bug here.");
//			}
//		}

		//System.out.println("prolog data:___"+event.data);
//		if(event.lnk[parent].data==-100){
//			XNode thisnil = this.nil;
//			System.out.println("event data:___"+event.data);
//			throw new RuntimeException();
//		}
//		System.out.println("event ==this.nil:____"+(event==this.nil));
//		System.out.println("resize session:____"+event.size);
//		System.out.println("resize session data:____"+event.data);
//		System.out.println("\r\n----------\r\n");
	}
	
	private XNode getAdjacency(XNode axis,int orientation){ // getSuccessor
		int vleft,vright;
		if(orientation==XNode.successor){
			 vleft=XNode.left;vright=XNode.right;
		}else{
			 vleft=XNode.right;vright=XNode.left;
		}
		XNode dest = axis.lnk[vright];
		if(axis.upper!=axis.nil){
			if(dest==axis.upper.nil){
				if(axis.lnk[parent].lnk[vleft]==axis){//this.isLeftSide(
					if( axis!=this.rootChild)
						return axis.lnk[parent];
					else
						return axis.upper.nil;
				}else{ //right side of partent
					do{
						if(axis.lnk[parent]!=this.rootChild)
							axis=axis.lnk[parent];
						else
							return axis.upper.nil;
					}while(axis.lnk[parent].lnk[vright]==axis);
					return axis.lnk[parent];
				}
			}else{
				while(dest.lnk[vleft]!=axis.upper.nil){
					dest=dest.lnk[vleft];
				}
				
			}
			return dest;
		}
		return this.nil;
		
	}

	private XNode iSelect(XNode axis ,int index){
		int tempdx = axis.lnk[left].size;// r ← size[left[x]]+1
		if(index==tempdx){ // 2  if i = r
			return axis;// 3     then return x
		}else if(index<tempdx){ // 4  elseif i< r
			if(axis.lnk[left]==this.nil)
				return null;
			return this.iSelect(axis.lnk[left],index);// 5     then return OS-SELECT(left[x], i)
		}else{
			if(axis.lnk[right]==this.nil)
				return null;
			return this.iSelect(axis.lnk[right], index-tempdx-1);// 6  else return OS-SELECT(right[x], i - r)
		}
	}
	private XNode innerDeletion(XNode nd){
//		System.out.println("deletion target:___"+nd.data);
//		int debugsize = this.getChildrenSize();
//		XNode debgrootbef = this.rootChild;
		
		XNode filler,residue; 
		if(nd.lnk[XNode.left] == this.nil || nd.lnk[XNode.right] == this.nil){	//1 if left[z] = nil[T] or right[z] = nil[T]
			filler = nd; // 2    then y ← z
		}else{	//nd has two data(not nil) children
			filler=this.getAdjacency(nd, XNode.successor); // 3    else y ← TREE-SUCCESSOR(z)
		}	
		if(filler.lnk[XNode.left]!=this.nil){	// 4 if left[y] ≠ nil[T]
			residue=filler.lnk[XNode.left]; // 5    then x ← left[y]
		}else{	// 6    else x ← right[y]
			residue = filler.lnk[XNode.right];
		}

		residue.lnk[XNode.parent]=filler.lnk[parent];// 7 p[x] ← p[y]

		if(filler==this.rootChild){	// 8 if p[y] = nil[T]
			this.rootChild=residue; // 9    then root[T] ← x
			
		}else if(filler==filler.lnk[XNode.parent].lnk[XNode.left]){//10    else if y = left[p[y]]
			filler.lnk[XNode.parent].lnk[XNode.left]=residue; //11            then left[p[y]] ← x //remove filler form its old parent.
		}else{
			filler.lnk[XNode.parent].lnk[XNode.right]=residue; //12            else right[p[y]] ← x//remove filler form its old parent.
		}

		//System.out.println("filler type:___"+(filler.data));
		this.resizePath(filler, false); // order statistics info maintainance code;
		if(filler!=nd){ //13 if y ≠ z
			this.yieldIdentityTo(nd,filler);//14    then key[z] ← key[y]
											//15         copy y's satellite data into z
//			int postdsize = this.getChildrenSize();
//			XNode debgrootaf = this.rootChild;
//			if(postdsize!=(debugsize-1)){
//				System.out.println("root change:___"+(debgrootbef!=debgrootaf));
//				System.out.println("root data:___"+this.rootChild.data);
//				System.out.println("size before:"+debugsize);
//				System.out.println("size after:"+postdsize);
//				throw new RuntimeException("deletion bug here.");
//			}
		}
		

		nd.lnk[XNode.parent]=nd.nil;
		nd.lnk[XNode.left]=nd.nil;
		nd.lnk[XNode.right]=nd.nil;
		
		return residue;//18 return y
//		if(nd.color==TwoLogNList.BLACK){//16 if color[y] = BLACK
//			then RB-DELETE-FIXUP(T, x) // x is residue, the event source.
//		}
		//System.out.println("deleted:___"+nd.data);
		//System.out.println("-------");

	}
	private void rotateTreeNode(XNode axis ,int orientation ){

		
		int vleft,vright;
		if(orientation==XNode.left){
			 vleft=XNode.left;vright=XNode.right;
		}else{
			 vleft=XNode.right;vright=XNode.left;
		}
		
		
		
		XNode handle = axis.lnk[vright];		// 1  y ← right[x]           // ▹ Set y.
		if(axis==this.nil)
			throw new RuntimeException("axis can not be nil.");
		if(handle==this.nil){
			
			//System.out.println("orientation:___"+((orientation==XNode.left)?"left":"right"));
			//System.out.println("re_orientation:___"+((vright==XNode.right)?"left":"right"));
			//System.out.println("tri_orientation:___"+((vleft==XNode.left)?"left":"right"));
			throw new RuntimeException("handle can not be nil.");
		}

		axis.lnk[vright]=handle.lnk[vleft];			// 2  right[x] ← left[y]     // ▹ Turn y's left subtree into x's right subtree.

		if(handle.lnk[vleft]!=this.nil)
			handle.lnk[vleft].lnk[parent]=axis;			// 3  p[left[y]] ← x
		handle.lnk[parent]=axis.lnk[parent];		// 4  p[y] ← p[x]            // ▹ Link x's parent to y.
		
		if(axis==this.rootChild){// 5  if p[x] = nil[T] // is axis root?
			this.rootChild=handle;			// 6     then root[T] ← y
		}else if(axis.lnk[parent].lnk[XNode.left]==axis){// 7     else if x = left[p[x]]
			axis.lnk[parent].lnk[XNode.left]=handle;	// 8             then left[p[x]] ← y
		}else{
			axis.lnk[parent].lnk[XNode.right]=handle;	// 9             else right[p[x]] ← y
		}

		handle.lnk[vleft]=axis;		//10  left[y] ← x            // ▹ Put x on y's left.
		axis.lnk[parent]=handle;				//11  p[x] ← y
		
		handle.size=axis.size;	//12 size[y] ← size[x]
		axis.size=axis.lnk[XNode.left].size+axis.lnk[XNode.right].size+1;	//13 size[x] ← size[left[x]] + size[right[x]] + 1
		
		//if(vleft==XNode.left && handle==this.nil) throw new RuntimeException("3");
		//if(vleft==XNode.left && handle==this.nil) throw new RuntimeException("2");
		//if(handle==this.nil) throw new RuntimeException("0");
		//if(vright==XNode.left && axis==this.nil) throw new RuntimeException("1");
		//System.out.println("hsz:___"+handle.size);
		//System.out.println("---axis left size:___"+axis.lnk[left].size);
		//System.out.println("---axis right size:___"+axis.lnk[right].size);
		//System.out.println("axsz:___"+axis.size+"\r\n");
	}

	private void fixBRDeletion(XNode event){
		XNode sibling;
		int vleft,vright;
		while(event!=this.rootChild && event.color==XNode.BLACK){//1 while x ≠ root[T] and color[x] = BLACK
			if(event==event.lnk[XNode.parent].lnk[XNode.left]){//2 do if x = left[p[x]]
				 vleft=XNode.left;vright=XNode.right;
			}else{
				 vleft=XNode.right;vright=XNode.left;
			}
			
			sibling=event.lnk[XNode.parent].lnk[vright];//3 then w ← right[p[x]]
			//if(sibling==this.nil) throw new RuntimeException("-2");
			if(sibling.color==XNode.RED){//4 if color[w] = RED
				sibling.color=XNode.BLACK;// 5 then color[w] ← BLACK	Case 1
				event.lnk[XNode.parent].color=XNode.RED;//6 color[p[x]] ← RED	Case 1
				this.rotateTreeNode(event.lnk[XNode.parent], vleft);	//7 LEFT-ROTATE(T, p[x])	Case 1
				sibling=event.lnk[XNode.parent].lnk[vright];		//8 w ← right[p[x]]			Case 1
			}		
			//if(sibling==this.nil) throw new RuntimeException("-3");
			if(sibling.lnk[XNode.left].color==XNode.BLACK && sibling.lnk[XNode.right].color==XNode.BLACK){//9 if color[left[w]] = BLACK and color[right[w]] = BLACK
				if(sibling!=this.nil)
					sibling.color=XNode.RED;//10	then color[w] ← RED Case 2
				event=event.lnk[XNode.parent];//11	x p[x]   Case 2
				continue;

			}else if(sibling.lnk[vright].color==XNode.BLACK){//12 else if color[right[w]] = BLACK
				if(sibling.lnk[vleft]==this.nil && sibling.lnk[vleft].color==XNode.RED){
					System.out.println();
					throw new RuntimeException("nil color is red!!!");
				}
				sibling.lnk[vleft].color=XNode.BLACK	;	//13 	then color[left[w]] ← BLACK Case 3
				sibling.color=XNode.RED; //14  	color[w] ← RED Case 3

				
				this.rotateTreeNode(sibling, vright); //15 	RIGHT-ROTATE(T, w)	Case 3
				sibling=event.lnk[XNode.parent].lnk[vright]; //16 	w ← right[p[x]]	Case 3
			}
			sibling.color=event.lnk[XNode.parent].color; //17    color[w] ← color[p[x]]	Case 4
			event.lnk[XNode.parent].color=XNode.BLACK; //18    color[p[x]] ← BLACK	Case 4
			sibling.lnk[vright].color=XNode.BLACK; //19	color[right[w]] ← BLACK	Case 4
			this.rotateTreeNode(event.lnk[XNode.parent],vleft); //20	LEFT-ROTATE(T, p[x])	Case 4
			event=this.rootChild; //21	x ← root[T]		Case 4		
		}
		event.color=XNode.BLACK;	//			23 color[x] ← BLACK
		//this.fixNil();
	}
	
	private void fixBRInsertion(XNode event){
		XNode uncle;
		int vleft,vright;
		while(event.lnk[XNode.parent].color==XNode.RED){//1 while color[p[z]] = RED
			if(event.lnk[XNode.parent]==event.lnk[XNode.parent].lnk[XNode.parent].lnk[XNode.left]){//2 do if p[z] = left[p[p[z]]]
				 vleft=XNode.left;vright=XNode.right;
			}else{
				 vleft=XNode.right;vright=XNode.left;
			}
			
			uncle=event.lnk[XNode.parent].lnk[XNode.parent].lnk[vright];//3 then y ← right[p[p[z]]]
			if(uncle.color==XNode.RED){//4 if color[y] = RED
				event.lnk[XNode.parent].color=XNode.BLACK;//5 then color[p[z]] ← BLACK	Case 1
				uncle.color=(XNode.BLACK);//6 color[y] ← BLACK	Case 1
				event.lnk[XNode.parent].lnk[XNode.parent].color=XNode.RED;//7 color[p[p[z]]] ← RED	Case 1
				event = event.lnk[XNode.parent].lnk[XNode.parent];//8 z ← p[p[z]]	Case 1
				continue;
			}else if(event==event.lnk[XNode.parent].lnk[vright]){//9 else if z = right[p[z]]
				event=event.lnk[XNode.parent];			//10 then z ← p[z]	 Case 2
				this.rotateTreeNode(event, vleft);		//11 LEFT-ROTATE(T, z)	 Case 2
			}
			event.lnk[XNode.parent].color=XNode.BLACK;//12 color[p[z]] ← BLACK	Case 3
			event.lnk[XNode.parent].lnk[XNode.parent].color=XNode.RED;	//13 color[p[p[z]]] ← RED	Case 3
			this.rotateTreeNode(event.lnk[XNode.parent].lnk[XNode.parent],vright);		//14 RIGHT-ROTATE(T, p[p[z]])	Case 3
		}
		this.rootChild.color=(XNode.BLACK);//16 color[root[T]] ← BLACK
		//this.fixNil();
		
	}

	private void fixNil(){
		if((this.nil.color==XNode.RED))
			System.out.println("nil is red!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		if(this.nil.lnk[parent]!=this.nil)
			System.out.println("nil parent changed!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		if(this.nil.lnk[left]!=this.nil)
			System.out.println("nil left changed!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		if(this.nil.lnk[right]!=this.nil)
			System.out.println("nil right changed!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		//System.out.println((this.nil.color==XNode.BLACK)?"black":"red");
//		this.nil.lnk[parent]=this.nil;
//		this.nil.lnk[left]=this.nil;
//		this.nil.lnk[right]=this.nil;
//		this.nil.color=XNode.BLACK;
	}
	
	
	public Object[] getPath4Event(){

		return this.getPath().toArray();
	}
	public LinkedList<XNode> getPath(){
		//if(this.path2rootid==null)
			//return new TreePath(this);

		LinkedList<XNode> path = new LinkedList<XNode>();
		path.addFirst(this);
		if(this.upper==this.nil)
			return path;
		XNode lparent = this.upper;
		do{	
			path.addFirst(lparent);
			lparent=lparent.upper;	
		}while(lparent!=lparent.nil);
		return path;
	}

}
