
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hanuman
 */
class Tree
{
    public int key,cnt;
    public ArrayList<Tree>child;
    public Tree parent;
    public Tree(int key,int cnt,Tree parent)
    {
        this.key=key;
        this.cnt=cnt;
        this.parent=parent;
        this.child=new ArrayList<Tree>();
    }
}
public class FPGrowth {
  public static void main(String[] argv)  
  {
      
      int   no_of_items=5,min_sup=2;
      
      
      
      int count[][],order[];
      int num,i,j;
      //first scan of all transaction;
      count=new int[no_of_items][2];
      order=new int[no_of_items];
      for(i=0;i<no_of_items;i++)
      {
          count[i][0]=i+1;
          count[i][1]=0;
      }
      char ch;
      
      try
        {
            FileInputStream fis=new FileInputStream("output.txt");
            DataInputStream fr=new DataInputStream(fis);
            ch='\n';
            
            while((num=fr.readInt())!=-1)
            {
                if(ch!='\n')
                    count[num-1][1]++;
                ch=fr.readChar();
            }   
            fr.close();
        }
        catch(Exception e){}
      
      
      //sort the itemset
      sortByDecOrder(count,no_of_items);
      
      for(i=0;i<no_of_items;i++)
      {
          order[i]=count[i][0];
          System.out.println(order[i]);
      }
      
      
      //root node
      Tree T;
      T=new Tree(0,0,null);
      
      HashMap<Integer,ArrayList<Tree>> hm=null;
      hm=new HashMap<Integer,ArrayList<Tree>>();
      
      for(i=0;i<no_of_items;i++)
          hm.put(i+1,new ArrayList<Tree>());
      
      int cnt=0;
      int arr[];
      arr=new int[no_of_items];
      
      
      try
        {
            FileInputStream fis=new FileInputStream("output.txt");
            DataInputStream fr=new DataInputStream(fis);
            ch='.';
            
            
            num=fr.readInt();
            while(true)
            {
                if(ch=='\n')
                {
                    sortByOrder(arr,cnt,order,no_of_items);
                    System.out.println();
                    insert(T,hm,arr,cnt,1);
                    
                    cnt=0;
                }
                else if(ch!='.')
                {
                    arr[cnt]=num;
                    cnt++;
                }
                if(num==-1)
                    break;
                ch=fr.readChar();
                num=fr.readInt();
                
            }   
            fr.close();
        }
        catch(Exception e){}
        System.out.println("FP Tree : ");
        System.out.println("key,count : Child Nodes");
        levelOrder(T);
        
        
        Tree fpNode[],Z;
        Iterator it;
        fpNode=new Tree[no_of_items];
        for(i=0;i<no_of_items;i++)
        {
            fpNode[i]=new Tree(0,0,null);
            it=(hm.get(i+1)).iterator();
            
            HashMap<Integer,ArrayList<Tree>> nhm=null;
            nhm=new HashMap<Integer,ArrayList<Tree>>();
      
            for(j=0;j<no_of_items;j++)
                nhm.put(j+1,new ArrayList<Tree>());
            
            while(it.hasNext())
            {
                cnt=0;
                Z=(Tree) it.next();
                int freq=Z.cnt;
                Z=Z.parent;
                
                while(Z.parent!=null)
                {
                    arr[cnt]=Z.key;
                    cnt++;
                    Z=Z.parent;
                }
                reverse(arr,cnt);
                insert(fpNode[i],nhm,arr,cnt,freq);
                
            }
            int tmp=i+1;
            System.out.println("FP Tree  for item "+tmp);
            System.out.println("key,count : Child Nodes");
            levelOrder(fpNode[i]);
            System.out.println("Frequent Pattern  : ");
            System.out.println("Pattern : frequency");
            frequentPattern(nhm,i+1,no_of_items,min_sup);
        }
        
        
  }
  public static void sortByDecOrder(int count[][],int no_of_items)
  {
      int i,j,num;
      for(i=0;i<no_of_items;i++)
      {
          for(j=no_of_items-1;j>i;j--)
          {
              if(count[i][1]<count[j][1]||(count[i][1]==count[j][1]&&count[i][0]>count[j][0]))
              {
                  num=count[i][0];
                  count[i][0]=count[j][0];
                  count[j][0]=num;
                  num=count[i][1];
                  count[i][1]=count[j][1];
                  count[j][1]=num;
              }
          }
      }
  }
  
  public static void sortByOrder(int arr[],int cnt,int order[],int no_of_items)
  {
      boolean rst[];
      rst=new boolean[no_of_items];
      int i,j;
      //for(j=0;j<cnt;j++)
        //  System.out.print(arr[j]);
      
      for(i=0;i<no_of_items;i++)
      {
          for(j=0;j<cnt;j++)
              if(arr[j]==i+1)
              {
                  rst[i]=true;
                  break;
              }
          if(j==cnt)
              rst[i]=false;
      }
      j=0;
      for(i=0;i<no_of_items;i++)
          if(rst[order[i]-1]==true)
          {
              arr[j]=order[i];
              j++;
          }
      
  }
  
  
  public static void insert(Tree T,HashMap<Integer,ArrayList<Tree>> hm,int arr[],int cnt,int freq)
  {
      Iterator it;
      ArrayList<Tree> alst=null;
      int i,j;
      i=0;
      Tree Z=T,Y = null,X;
      boolean flag;
      while(i<cnt)
      {
          it=Z.child.iterator();
          flag=false;
          while(it.hasNext())
          {
              Y=(Tree) it.next();
              if(Y.key==arr[i])
              {
                  flag=true;
                  break;
              }
          }
          if(flag==true)
          {
              Y.cnt=Y.cnt+freq;
              Z=Y;
          }
          else
          {
              X=new Tree(arr[i],freq,Z);
              X.parent=Z;
              Z.child.add(X);
              Z=X;
              alst=hm.get(arr[i]);
              alst.add(X);
              hm.put(arr[i], alst);
          }
          i++;
      }
      
  }
  public static void levelOrder(Tree T)
  {
      System.out.print(T.key+ " , "+T.cnt+"  : ");
      Iterator it;
      Tree Z;
      it=T.child.iterator();
      while(it.hasNext())
      {
          Z=(Tree) it.next();
          System.out.print(Z.key);
      }
          
      System.out.println();
      it=T.child.iterator();
      while(it.hasNext())
      {
          Z=(Tree) it.next();
          levelOrder(Z);
      }
  }
  public static void reverse(int arr[],int cnt)
  {
      int i=0,j=cnt-1,tmp;
      while(i<j)
      {
          tmp=arr[i];
          arr[i]=arr[j];
          arr[j]=tmp;
          i++;
          j--;
      }
  }
  public static void frequentPattern(HashMap<Integer,ArrayList<Tree>> nhm,int i,int no_of_items,int min_sup)
  {
       Iterator it;
       int j;
       for(j=0;j<no_of_items;j++)
       {
           it=nhm.get(j+1).iterator();
           Tree Z;  
           int freq;
          while(it.hasNext())
           {
                Z=(Tree) it.next();
                if(Z.cnt>=min_sup)
                {
                    freq=Z.cnt;
                    System.out.println(i+","+Z.key+" : "+freq);
                    String txt="";
                    txt+=(i+",");
                    txt+=(Z.key+",");
                    
                    Z=Z.parent;
                    int flag=0;
                   while(Z.parent!=null)
                   {
                       txt+=(Z.key+",");
                       if(Z.cnt<freq)
                           freq=Z.cnt;
                       Z=Z.parent;
                       flag=1;
                   }
                   if(flag==1)
                   System.out.println(txt+" : "+freq);
               }
            }
       }
    }
}

