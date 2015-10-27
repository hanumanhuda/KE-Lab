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
public class MApriori {
   
    
    public static void main(String[] args)
    {
        
        //these data should be  according to the database;
        int no_of_item=5,no_of_trans=100,min_sup=10;;
        
        
        
        
        boolean table[][];
        table=new boolean[no_of_trans][no_of_item];
        
        
        
        int num;
        char ch;
      
        int i,j;
        for(i=0;i<no_of_trans;i++)
            for(j=0;j<no_of_item;j++)
                table[i][j]=false;
        
        
             
        HashMap<HashSet<Integer>,Integer>hm=null;
        hm=new HashMap<HashSet<Integer>,Integer>();
        
        
        try
        {
            FileInputStream fis=new FileInputStream("output.txt");
            DataInputStream fr=new DataInputStream(fis);
            ch='\n';
            
            while((num=fr.readInt())!=-1)
            {
                if(ch=='\n')
                {
                    System.out.print(num+" : ");
                    i=num-1;
                }
                else
                {
                    HashSet<Integer> s=new HashSet<Integer>();
                    table[i][num-1]=true;
                    
                    s.add(num);
                    System.out.print(s);
                    if(hm.containsKey(s))
                    {//System.out.println("tt");
                      int tmp=hm.get(s);
                      hm.put(s, tmp+1);
                    }
                    else
                       hm.put(s,1);
                    //s.clear();
                }
                ch=fr.readChar();
                System.out.print(ch);
            }   
            fr.close();
        }
        catch(Exception e){}
       
        
        
        HashMap<HashSet<Integer>,Integer>nhm=null;
        nhm=new HashMap<HashSet<Integer>,Integer>();
        nhm.putAll(hm);
        hm.clear();
        
        Set<Entry<HashSet<Integer>,Integer>> set=nhm.entrySet();
        for(Entry<HashSet<Integer>,Integer> me:set)
        {
           if(me.getValue()>=min_sup)
               hm.put(me.getKey(),me.getValue());
        }
        nhm.clear();
        print(hm);
        
                
        num=2;
        int c,cnt;
        
        Iterator<Integer> i1;
        while(hm.size()>0)
        {
            hm=apriori_gen(hm);
            if(hm.size()!=0)
            {
                
              System.out.println("\nFrequent "+num+"-ItemSet : ");
              num++;
              System.out.println("After Prunning : ");
              print(hm);
              nhm.clear();
            
                set=hm.entrySet();
                
                
                for(Map.Entry<HashSet<Integer>,Integer> me:set)
                {
                    c=0;
                    for(i=0;i<no_of_trans;i++)
                    {
                        i1=(me.getKey()).iterator();
                        cnt=0;
                        while(i1.hasNext())
                        {
                            
                            int tmp=i1.next()-1;
                            //System.out.println(tmp);
                            if(table[i][tmp]==true)
                             cnt++;
                        }
                     
                        
                        if(cnt==(me.getKey()).size())
                        {//System.out.println("i : "+i);
                            c++;}
                    }
                    
                    
                    if(c>=min_sup)
                    {
                        nhm.put(me.getKey(),c);
                        //System.out.println(me.getKey()+" : "+c);
                    }
                }
                hm.clear();
                hm.putAll(nhm);
                System.out.println("After Minimum Support : ");
                print(hm);
                
            }    
        }
    }
    
    
    
    
    public static void print(HashMap<HashSet<Integer>,Integer>hm)
    {
        Set<Map.Entry<HashSet<Integer>,Integer>> set=hm.entrySet();
        for(Map.Entry<HashSet<Integer>,Integer> me:set)
           System.out.println(me.getKey()+" : "+me.getValue());
    }
    
    
     
    
    
    
    static HashMap<HashSet<Integer>,Integer> apriori_gen(HashMap<HashSet<Integer>,Integer> hm)
    {
        HashMap<HashSet<Integer>,Integer> nhm=new HashMap<HashSet<Integer>,Integer>();
        Set<Map.Entry<HashSet<Integer>,Integer>> set=hm.entrySet();
        
        Iterator<Integer>i1,i2;
        Integer x,y;
        int size,i,j;
        boolean flag;
        System.out.println("Before Prunning : ");
        for(Map.Entry<HashSet<Integer>,Integer> m1:set)
        {
           
            for(Map.Entry<HashSet<Integer>,Integer> m2:set)
            {
                HashSet<Integer>s2=null,s1;
                s1=new HashSet<Integer>();
                
                s1.addAll(m1.getKey());
                s2=m2.getKey();
                size=s2.size();
                //System.out.println(s1);
                //System.out.println(s2);
                i1=s1.iterator();
                i2=s2.iterator();
                for(i=0;i<size-1;i++)   
                    if(i1.next()!=i2.next())
                        break;
                
                x=i1.next();
                y=i2.next();
                
                if(i==size-1&&x<y)
                {
                    s1.add(y);
                    System.out.println(s1);
                    flag=true;
                   
                    for(i=0;i<s1.size();i++)
                    {
                        i1=s1.iterator();
                        Set<Integer>s=new HashSet<Integer>();
                        for(j=0;j<s1.size();j++)
                        {
                            if(i!=j)
                                s.add(i1.next());
                            else 
                                i1.next();
                        }
                        if(hm.get(s)==null)
                        {
                            flag=false;
                            break;
                        }
                        s.clear();
                    }
                    if(flag==true)
                        nhm.put(s1,0);
                }   
            }
        }
        return nhm;
    }
}

