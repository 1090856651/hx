package com.jsonUtil;

import add2.Add;

import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

public class MJBuilderTest {
	public static void main(String[] args) {
        MWNumericArray a = null;   
        MWNumericArray b = null;   
        Object[] result = null;    
        Add myAdd = null;     
        try
        {
           
           /* if (args.length != 2)
            {
                System.out.println("Error: must input 2 numbers!");
                return;
            }*/
          
           
            a = new MWNumericArray(Double.valueOf("215"),MWClassID.DOUBLE);
            //b = new MWNumericArray(Double.valueOf("20"),MWClassID.DOUBLE);
           
            myAdd = new Add();
           
            result = myAdd.add(1, a);
            System.out.print("The sum of " + a.toString() + " and " + " is: ");
            System.out.println(result[0]);
        }
        catch (Exception e)
        {
            System.out.println("Exception: " + e.toString());
        }
      
        finally
        {
           
            MWArray.disposeArray(a);
            MWArray.disposeArray(b);
            MWArray.disposeArray(result);
            if (myAdd != null)
                myAdd.dispose();
        }
	}
}
