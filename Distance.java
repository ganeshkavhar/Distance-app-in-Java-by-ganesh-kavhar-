//1:newfndistrans.java<br />
//2:A bmp monochrome image(preferably 512-512)<br />
//make the image from mspaint or any other source</p>
<p>import java.io.*;<br />
import javax.swing.*;<br />
import java.awt.*;<br />
import java.awt.event.*;<br />
public class newfndistrans extends JFrame{<br />
	frame frm=null;<br />
	File f;<br />
	FileInputStream ff;<br />
	Color cl;<br />
    int count;<br />
    int  a[];</p>
<p>    int rnum,gnum;<br />
   int xsize;<br />
   int ysize;<br />
  final int sample=5;<br />
  String str=null;</p>
<p>newfndistrans(String str)<br />
{<br />
    super(str);<br />
    frm=new frame();</p>
<p>    count=0;<br />
    a=new int[8];<br />
    rnum=0;gnum=0;<br />
}</p>
<p>	 public void paint(Graphics g)<br />
   {<br />
   	int x,y,i=0,j=0,temp;</p>
<p>   	    try{</p>
<p>		System.out.println(“Starting”);<br />
		System.out.println(“filename=”+frm.getfilename());<br />
		try{f=frm.getfilename();<br />
            ff=new FileInputStream(f);<br />
           }catch(Exception e){<br />
           	System.out.println(“error in reading file”);<br />
           	}<br />
     ff.skip(18);<br />
     i=ff.read();<br />
     i=((ff.read()<<8)|i);<br />
     i=((ff.read()<<16)|i);<br />
     i=((ff.read()<<24)|i);<br />
     xsize=i;<br />
     System.out.println(“width=”+xsize);<br />
     i=0;<br />
     i=ff.read();<br />
     i=((ff.read()<<8)|i);<br />
     i=((ff.read()<<16)|i);<br />
     i=((ff.read()<<24)|i);<br />
     ysize=i;<br />
     System.out.println(“Height=”+ysize);</p>
<p>     ff.skip(38);//62-(2+16+4+4=26)=36, actually total=62 Bytes header<br />
     //note:make it 38 for nrectbit1.bmp and nrectbit2.bmp<br />
     x=0;y=ysize;<br />
     int pix[][],mat1[][];<br />
     pix=new int[xsize][ysize];<br />
     mat1=new int[xsize][ysize];<br />
     System.out.println(“here”);<br />
   	while(true)<br />
		{</p>
<p>			rnum=ff.read();<br />
			//System.out.println(“rnum=”+rnum);<br />
			if(rnum==-1)break;<br />
			count=7;<br />
	//image has 0 for black ,1 for white,but my convention is opposite<br />
		while(rnum>0)<br />
			{<br />
				gnum=rnum%2;<br />
				if(gnum==0)a[count]=1;<br />
				 else a[count]=0;<br />
				count–;<br />
				rnum=rnum/2;<br />
			}<br />
			while(count>=0)<br />
			{a[count]=1;count–;<br />
			}<br />
			for(i=0;i<=7;i++)<br />
			{if(a[i]==0)cl=new Color(255,255,255);<br />
			   else cl=new Color(0,0,0);<br />
			  pix[x][y-1]=a[i];<br />
			  mat1[x][y-1]=a[i];<br />
			   g.setColor(cl);<br />
				g.drawRect(x,y,1,1);<br />
				x++;<br />
				if(x==xsize){x=0;y–;}<br />
			 }<br />
			}//end while</p>
<p>System.out.println(“File read successfully”);<br />
//sleep for 4 seconds(4000 msec)<br />
try{Thread.sleep(4000);<br />
}catch(Exception e){}<br />
//clear the region where the image was drawn,area near the boundaries<br />
are<br />
also cleaned<br />
for(j=0;j<=ysize+2;j++)<br />
for(i=0;i<=xsize+2;i++)<br />
  g.clearRect(i,j,1,1);<br />
//cleaning done above</p>
<p>//0 means white,1 means black<br />
//perform the distance transform<br />
for(j=0;j<ysize;j++)<br />
for(i=0;i<xsize;i++)<br />
 {<br />
  if(mat1[i][j]==0)continue;<br />
  if((j==0)||(j==ysize-1)){mat1[i][j]=1;continue;}<br />
   if((i==0)||(i==xsize-1)){mat1[i][j]=1;continue;}</p>
<p>temp=findmin(mat1[i-1][j],mat1[i-1][j-1],mat1[i][j-1],mat1[i+1][j-1]);<br />
      mat1[i][j]=temp+1;<br />
 }</p>
<p>//perform the distance transform from bottom<br />
for(j=ysize-1;j>=0;j–)<br />
for(i=xsize-1;i>=0;i–)<br />
 {if(mat1[i][j]==0)continue;<br />
  if((j==0)||(j==ysize-1)){mat1[i][j]=1;continue;}<br />
  if((i==0)||(i==xsize-1)){mat1[i][j]=1;continue;}</p>
<p>temp=findmin(mat1[i-1][j+1],mat1[i][j+1],mat1[i+1][j+1],mat1[i+1][j]);<br />
      if(temp>(mat1[i][j]-1))temp=mat1[i][j]-1;<br />
      mat1[i][j]=temp+1;<br />
 }</p>
<p>int max=-9999,colornum,i1,j1,globmax=-9999;<br />
x=0;y=0;<br />
for(j=0;j<ysize-(ysize%sample);j+=sample)<br />
for(i=0;i<xsize-(xsize%sample);i+=sample)<br />
 {<br />
 	max=-9999;<br />
 	for(j1=j;j1<j+sample;j1++)<br />
 	for(i1=i;i1<i+sample;i1++)<br />
 	if(max<mat1[i1][j1])<br />
 	{<br />
 		max=mat1[i1][j1];<br />
 		//xcord=i1;ycord=j1;<br />
 	}<br />
 	//out of both above for loops<br />
 	if(globmax<max)globmax=max;</p>
<p> 	if(max!=0)<br />
 	{<br />
 	System.out.println(“max=”+max);<br />
 	for(j1=j;j1<j+sample;j1++)<br />
 	for(i1=i;i1<i+sample;i1++)<br />
 		mat1[i1][j1]=max;<br />
 	}</p>
<p> }<br />
System.out.println(“globmax=”+globmax);<br />
 for(j=0;j<ysize;j++)<br />
 for(i=0;i<xsize;i++)<br />
  {colornum=(mat1[i][j]*255)/globmax;//contrast enhancement<br />
   colornum=255-colornum;<br />
   cl=new Color(colornum,colornum,colornum);<br />
   g.setColor(cl);<br />
   g.drawRect(i,j,1,1);<br />
  }<br />
ff.close();<br />
System.out.println(“program ends”);<br />
		}//end try block<br />
	catch(Exception e)<br />
		{System.out.println(“Error”+e.getMessage());<br />
		}</p>
<p>   	}<br />
 int findmin(int num1,int num2,int num3,int num4)<br />
 {<br />
 	int min=4000;<br />
 	if(min>num1)min=num1;<br />
 	if(min>num2)min=num2;<br />
 	if(min>num3)min=num3;<br />
 	if(min>num4)min=num4;<br />
 	return(min);<br />
 }</p>
<p>	public static void main(String args[])<br />
	{</p>
<p>		JFrame frm=new newfndistrans(“Distance Transform”);<br />
		frm.setSize(700,700);<br />
		frm.setVisible(true);</p>
<p>	}//end main()</p>
<p>}// end class fndistrans<br />
class frame extends JFrame{<br />
	JButton but;<br />
	JFileChooser fch;<br />
    String filename;<br />
    File file=null;<br />
	//Container con;<br />
	boolean temp;<br />
	frame()<br />
	{ super(“FileReading”);<br />
	  setSize(700,500);<br />
	  setLayout(new FlowLayout());</p>
<p>	  temp=true;<br />
	  but=new JButton(“Open the binary file”);<br />
	  add(but);<br />
	  fch=new JFileChooser();<br />
	  but.addMouseListener(new MouseAdapter()<br />
	  { public void mousePressed(MouseEvent me)<br />
	  	{<br />
	  		int retval=fch.showOpenDialog(but);<br />
	  		if(retval==JFileChooser.APPROVE_OPTION)<br />
	  		{<br />
	  			file=fch.getSelectedFile();<br />
	  			filename=file.getName();<br />
	  			System.out.println(“1filename=”+filename);<br />
	  			temp=false;<br />
	  		}</p>
<p>	  	}</p>
<p>	  });<br />
	  setVisible(true);<br />
	while(temp);<br />
	}//end constructor<br />
File getfilename()<br />
{System.out.println(“filename=”+filename);<br />
	return file;<br />
}</p>
<p>}<br />
