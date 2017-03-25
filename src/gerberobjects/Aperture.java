/*
 * Copyright (C) 2017 GY
 *
 * Source code is based on Dave Gonner's free gerber2png work.
 * see gerber2png at <https://github.com/dgonner/gerber2png>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package gerberobjects;



//import java.awt.*;

public class Aperture {

	private int ppi;
	private String type;
	private float[] modarray;
	
	public Aperture(int ppi, String type, float[] modarray) {
		this.ppi = ppi;
		this.type = type;
		this.modarray = modarray;
	}


	public void draw(MyGraphics myg, int x, int y) {
		this.draw(myg, x, y, false);
	}

	public void draw(MyGraphics myg, int x, int y, boolean inverted) {

		if (this.type.equals("C")) { // draw circle
			//System.out.println("Drawing CIRCLE at x,y ["+x+","+y+"]");
			
			int diameter =  (int)Math.round((double)this.modarray[0]*(double)this.ppi);
			//System.out.println("diameter: "+diameter);
			
			int xx = x - (int)Math.round(diameter/2.0);
			int yy = y - (int)Math.round(diameter/2.0);
				
			myg.circle(xx, yy, diameter, inverted);
		}
		
		if (this.type.equals("R")) { // draw rectangle
			//System.out.println("Drawing RECTANGLE at x,y ["+x+","+y+"]");
			
			int width = (int)Math.round((double)this.modarray[0]*(double)this.ppi);
			int height = (int)Math.round((double)this.modarray[1]*(double)this.ppi);
			
			int xx = x - (int)Math.round(width/2.0);
			int yy = y - (int)Math.round(height/2.0);
						
			myg.rect(xx, yy, width, height);
		}

		if (this.type.equals("O")) { // draw oval
			//System.out.println("Drawing OVAL at x,y ["+x+","+y+"]");
			
//			double min = Math.min(this.modarray[0], this.modarray[1]);
//			double max = Math.max(this.modarray[0], this.modarray[1]);
//			
//			int diameter = (int)Math.round((double)min*(double)this.ppi);
//			int width = (int)Math.round((double)max*(double)this.ppi);
			
			double w = (double)this.modarray[0]*(double)this.ppi;
			double h = (double)this.modarray[1]*(double)this.ppi;
			
			boolean upright = true;
			if (w > h) upright = false;
			
			if (upright) {
				int diameter = (int)Math.round(w);
				int half_diameter = (int)Math.round(w/2.0);
				int height = (int)Math.round(h);
				int half_height = (int)Math.round(h/2.0);
				
				myg.circle(x-half_diameter, y-half_height, diameter);
				myg.circle(x-half_diameter, y+half_height-diameter, diameter);
				//myg.circle(x-half_diameter, y-half_diameter, diameter);
                                // GY
                                myg.rect(x - half_diameter, y - half_height + half_diameter, diameter, height - diameter);
			} else {
				int diameter = (int)Math.round(h);
				int half_diameter = (int)Math.round(h/2.0);
				int width = (int)Math.round(w);
				int half_width = (int)Math.round(w/2.0);
			
				myg.circle(x-half_width, y-half_diameter, diameter);
				myg.circle(x+half_width-diameter, y-half_diameter, diameter);
				//myg.circle(x-half_diameter, y-half_diameter, diameter);
                                // GY
                                //myg.rect(x - half_width + half_diameter, y - half_width, width - diameter, diameter);
                                myg.rect(x - half_width + half_diameter, y - half_diameter, width - diameter, diameter);
			}
			
			
			//int xx = x - (int)Math.round(width/2.0);
			//int yy = y - (int)Math.round(height/2.0);
						
			//g2d.fillOval(offsetx+xx, offsety+yy, width, height);
		}
		
		
	}
	
	
}
