package Filters;

import java.awt.*;
import java.awt.image.*;
import java.applet.*;
import java.net.*;
import java.io.*;
import java.lang.Math;
import java.util.*;
 
 // additional stuff for swing
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JApplet;
import javax.imageio.*;

public class laplacian {

		int[] input;
		int[] output;
		float[] template={0,-1,0,-1,4,-1,0,-1,0};
		int[] processedValues;
		int[] MHValues;
		int progress;
		int sigma;
		int templateSize=3;
		int width;
		int height;

		public void laplacian() {

		}
		
		public void init(int[] original, int widthIn, int heightIn) {
			width=widthIn;
			height=heightIn;
			input = new int[width*height];
			output = new int[width*height];
			input=original;
		}
		public int[] process() {
			int sum;
			int max=0;
			int min=0;
			progress=0;
			processedValues = new int[width*height];

			for(int x=1; x<width-2;x++) {
				progress++;
				for(int y=1; y<height-2;y++) {
					sum=0;
					for(int x1=0;x1<templateSize;x1++) {
						for(int y1=0;y1<templateSize;y1++) {
							int x2 = (x-(templateSize-1)/2+x1);
							int y2 = (y-(templateSize-1)/2+y1);
							float value = (input[y2*width+x2] & 0xff) * (template[y1*templateSize+x1]);
							sum += value;
						}
					}
					if(sum>max)
						max=sum;
					if(sum<min)
						min=sum;
					processedValues[y*width+x] = sum;
				}
			}
			float ratio;
			ratio=(float)255/(float)(max+min);
			System.out.println("ratio="+ratio);
			System.out.println("max="+max);
			for(int x=1; x<width-2;x++) {
				for(int y=1; y<height-2;y++) {
					processedValues[y*width+x]=(int)((double)(processedValues[y*width+x]-min)*ratio);
					int val=processedValues[y*width+x];
/*					if(val>0)
						output[y*width+x] = 0xffffffff;
					if(val<0)
						output[y*width+x] = 0xff000000;
*/					if(val>=0)
						output[y*width+x] = 0xff000000 | (val << 16 );
					if(val<0)
						output[y*width+x] = 0xff000000 | ((-val&0xff) << 8);
/*				if(val>=0)
						output[y*width+x] = 0xff000000 | (val << 16 | val << 8 | val  );
					if(val<0)
						output[y*width+x] = 0xff000000 | ((-val&0xff) << 16 | (-val&0xff) << 8 | (-val&0xff)  );
*/				}
			}
			return output;
		}

		public int[] getMH() {
			progress=0;
			MHValues = new int[width*height];
			int max=0;
			for(int x=1; x<width-2;x++) {
				progress++;
				for(int y=1; y<height-2;y++) {
					int val=get_change(x,y);
//					MHValues[y*width+x]=0xff000000 | (val << 16 | val << 8 | val  );
					MHValues[y*width+x]=val;
					
					if(val>max)
						max=val;
				}
			}
			//double ratio=255/max;
			for(int x=1; x<width-2;x++) {
				for(int y=1; y<height-2;y++) {
					
					int val=(int)((double)MHValues[y*width+x]*255)/max;
			//		System.out.print(" "+val);
					MHValues[y*width+x]=0xff000000 | (val << 16 | val << 8 | val  );
				}
			//		System.out.println();
			}
			return MHValues;
		}
		private int get_change(int x, int y){
			int value=0;
			int total=0;
			

			if(processedValues[(y-1)*width+(x-1)]*processedValues[(y+1)*width+(x+1)]<0){
				total+=Math.abs(processedValues[(y-1)*width+(x-1)]-processedValues[(y+1)*width+(x+1)]);
			}
			if(processedValues[(y+1)*width+(x-1)]*processedValues[(y-1)*width+(x+1)]<0){
				total+=Math.abs(processedValues[(y+1)*width+(x-1)]-processedValues[(y-1)*width+(x+1)]);
			}
			if(processedValues[(y+1)*width+(x)]*processedValues[(y-1)*width+(x)]<0){
				total+=Math.abs(processedValues[(y+1)*width+(x)]-processedValues[(y-1)*width+(x)]);
			}
			if(processedValues[(y)*width+(x+1)]*processedValues[(y)*width+(x-1)]<0){
				total+=Math.abs(processedValues[(y)*width+(x+1)]-processedValues[(y)*width+(x-1)]);
			}
			
			return total;
				
		}

		public int getProgress() {
			return progress;
		}


	}