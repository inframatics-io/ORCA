/*
 * Copyright (c) 2013 Open Systems(WWW.OPENSYSTEMS.IO). All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */
/*
 * Created on May 23, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import javax.swing.BorderFactory;
import javax.swing.JSlider;
/**
 * @author Payman Touliat
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Filter {
    
    protected String filterName;
    protected int FilterNumber;
    protected int maxVal, minVal;
    public JSlider mySlider;
    
    public Filter(int fN,String n,int min,int max){
        filterName=n;
        
        if(max>min){
        	maxVal=max;
        	minVal=min;
        	if(fN<1){
        		System.out.println("Filter Number can't be less than 1");
        	}else{
        		FilterNumber=fN;
        	}
        	mySlider=new JSlider(JSlider.HORIZONTAL,min,max,min);
        	mySlider.setName(n);
        	mySlider.setBorder(BorderFactory.createTitledBorder(filterName));
			mySlider.setPaintTicks(true);
			mySlider.setPaintLabels( true );
			mySlider.setPaintTrack( true );
			mySlider.setSnapToTicks(false);
        }else{
        	System.out.println("problem with range. Please use a max > min.");
        }
    }
    public String toString(){
        return filterName;
    }
    public String getName(){
        return filterName;
    }
    public boolean checkValue(int val){
    	return (val>=minVal&&val<=maxVal);
    }

}
