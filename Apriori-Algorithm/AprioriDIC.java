
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package randomtransaction;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author hanuman
 */
class Value
{
    public int state;
    public int freq;
    public Value(int state,int freq)
    {
        this.state=state;
        this.freq=freq;
    }
}
public class AprioriDIC {
   
    public static void main(String argv[])
    {
        //these data should be  according to the database;
        //no_of_state : number of starting states in DIC
        int no_of_transaction=100,no_of_item=5,min_sup=10,no_of_state=4;
        
        
        
        int index=0,range_size=no_of_transaction/no_of_state;;
        boolean table[][];
        table=new boolean[no_of_transaction][no_of_item];
        
        int i,j,t,k;
        for(i=0;i<no_of_transaction;i++)
            for(j=0;j<no_of_item;j++)
                table[i][j]=false;
        try
        {
            FileInputStream fis=new FileInputStream("output.txt");
            DataInputStream fr=new DataInputStream(fis);
            char ch='\n';
            int num;
            while((num=fr.readInt())!=-1)
            {
                if(ch=='\n')                   
                    i=num-1;
                else
                    table[i][num-1]=true;
                
                ch=fr.readChar();
            }   
        }
        catch(Exception e){}
        
        
        
        HashMap<HashSet<Integer>,Value>hm=null;
        hm=new HashMap<HashSet<Integer>,Value>();
        for(t=0;t<no_of_transaction/4;t++)
            for(j=0;j<no_of_item;j++)
            {
                HashSet<Integer> s=new HashSet<Integer>();
                s.add(j+1);
                if(table[t][j]==true)
                {
                    if(hm.get(s)==null)
                    {
                     hm.put(s, new Value(0,1));
                    }
                    else
                    {
                        int tmp=hm.get(s).freq;
                        hm.put(s,new Value(0,tmp+1));
                    }
                }
            }
        
       Iterator i1,i2;
       HashSet<Integer> s=new HashSet<Integer>();
       HashSet<Integer> s1=new HashSet<Integer>();
       
       HashMap<HashSet<Integer>,Value>nhm=null;
       nhm=new HashMap<HashSet<Integer>,Value>();
       
       HashMap<HashSet<Integer>,Value>thm=null;
       thm=new HashMap<HashSet<Integer>,Value>();
       
       Integer x=0,y=0; 
       index=1;
       boolean flag=true;
       while(flag || hm.size()>0)
       {
           //System.out.println("start");
           //print(hm);
           flag=false;
           Set<Entry<HashSet<Integer>,Value>> set=hm.entrySet();
           for(Map.Entry<HashSet<Integer>,Value> me:set)
           {
               Value ob=me.getValue();
               s=null;
               s=me.getKey();
               ob.state++;
               //System.out.println(s+" : "+ob.freq);
               if(ob.state<no_of_state)
               {
                    thm.put(s, ob);
                    flag=true;
               }
               else
                   nhm.put(s, ob);
               if(ob.freq>=min_sup)
               {
                   
                   for(Map.Entry<HashSet<Integer>,Value> m2:set)
                   {
                       s=null;
                       s=new HashSet<Integer>();
                       s.addAll(me.getKey());
                      //System.out.println(s+" : "+m2.getKey());
                     if(s.size()==m2.getKey().size()&&m2.getValue().freq>=min_sup)  
                     {
                         int size=s.size();
                         i1=s.iterator();
                         s1=null;
                         s1=m2.getKey();
                         i2=s1.iterator();
                         for(i=0;i<size-1;i++)
                             if(i1.next()!=i2.next())
                                 break;
                         if(i1.hasNext())
                          x=(Integer) i1.next();
                         if(i2.hasNext())
                          y=(Integer) i2.next();
                         //System.out.println(s+" : "+s1);
                         if(i==size-1&&x<y)
                         {
                             s.add(y);
                             if(hm.get(s)==null)
                             {
                                thm.put(s,new Value(0,0));
                                //System.out.println(s);
                             }
                         }
                     }
                   }
               }
           }
           hm.clear();
           //System.out.println("middle : ");
           //print(thm);
           hm.putAll(thm);
           //System.out.println("mid : ");
           //print(hm);
           
           for(i=0;i<no_of_transaction/4;i++)
           {
                s=null;
                s=new HashSet<Integer>();
                for(j=0;j<no_of_item;j++)
                {
                    if(table[index*range_size+i][j]==true)
                    {
                       s.add(j+1);
                       s1=new HashSet<Integer>();
                       s1.add(j+1);
                       if(thm.get(s1)==null&&nhm.get(s1)==null)
                       {
                           //System.out.println("new entry");
                           Value ob=new Value(0,1);
                           thm.put(s1, ob);
                           hm.put(s1, ob);
                       }
                       s1.clear();
                    }
                }
                //System.out.println(" set  : "+s);
                set=thm.entrySet();
                for(Map.Entry<HashSet<Integer>,Value> me:set)
                {
                    
                    if(s.containsAll(me.getKey())==true)
                   {
                        Value ob=hm.get(me.getKey());
                        hm.put(me.getKey(),new Value(ob.state,ob.freq+1));
                   }
                }
            
           }
           index=(index+1)%no_of_state;
           //System.out.println("END : ");
           //print(hm);
           thm.clear();
           
       }
       hm.clear();
       Set<Entry<HashSet<Integer>,Value>> set=nhm.entrySet();
       for(Map.Entry<HashSet<Integer>,Value> me:set)
       {
           Value ob=me.getValue();
           if(ob.freq>=min_sup)
               hm.put(me.getKey(), ob);
       }
       System.out.println("All Frequent Itemsets : ");
       print(hm);
    }
    public static void print(HashMap<HashSet<Integer>,Value>hm)
    {
        Set<Map.Entry<HashSet<Integer>,Value>> set=hm.entrySet();
        for(Map.Entry<HashSet<Integer>,Value> me:set)
           System.out.println(me.getKey()+" : "+me.getValue().freq);
    }
}
