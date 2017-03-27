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


import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.geom.Point2D;

// kor alaku apertura
class MyCircle{
	public int x;
	public int y;
	public int diameter;
	public boolean inverted = false;

	public MyCircle(int x, int y, int diameter) {
		this.x = x;
		this.y = y;
		this.diameter = diameter;
	}

}

// negyzetes apertura
class MyRect{
	public int x;
	public int y;
	public int width;
	public int height;

	public MyRect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}


public class MyGraphics {


	private BufferedImage image;
	private Graphics2D g2d;		
	private int imgw = 0;
	private int imgh = 0;


        // cuccok listaja
	private LinkedList operations = new LinkedList();

	public MyGraphics() {
	}

	public void clear(){
		operations.clear();
	}


	public void circle(int x, int y, int diameter){
		this.operations.add(new MyCircle(x,y,diameter));
	}

	public void circle(int x, int y, int diameter, boolean inverted){
		MyCircle mc = new MyCircle(x,y,diameter);
		mc.inverted = inverted;
		this.operations.add(mc);
	}


	public void rect(int x, int y, int width, int height) {
		this.operations.add(new MyRect(x,y,width,height));
	}


	private void newImageFile(int imgw, int imgh) {
		this.image = new BufferedImage(imgw, imgh, BufferedImage.TYPE_INT_RGB);
		this.g2d = image.createGraphics();	
	}

	private void setDPI(IIOMetadata metadata, int DPI) throws IIOInvalidTreeException {
	    // for PMG, it's dots per millimeter
		double INCH_2_MM = 25.4; 
	    double dotsPerMilli = (double)DPI / INCH_2_MM;

	    IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
	    horiz.setAttribute("value", Double.toString(dotsPerMilli));

	    IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
	    vert.setAttribute("value", Double.toString(dotsPerMilli));

	    IIOMetadataNode dim = new IIOMetadataNode("Dimension");
	    dim.appendChild(horiz);
	    dim.appendChild(vert);

	    IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
	    root.appendChild(dim);

	    metadata.mergeTree("javax_imageio_1.0", root);
	}


	private void saveGridImage(BufferedImage gridImage, File output, int ppi) throws IOException {
	    output.delete();

	    final String formatName = "png";

	    for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(formatName); iw.hasNext();) {
	    	ImageWriter writer = iw.next();
	        ImageWriteParam writeParam = writer.getDefaultWriteParam();
	        ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
	        IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
	        if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
	           continue;
	        }

	        setDPI(metadata, ppi);

	        final ImageOutputStream stream = ImageIO.createImageOutputStream(output);
	        try {
	           writer.setOutput(stream);
	           writer.write(metadata, new IIOImage(gridImage, null, metadata), writeParam);
	        } finally {
	           stream.close();
	        }
	        break;
	    }
	}


	private void saveImageFile(String filename, int ppi) {
		// save the buffered image
		try {
		    File outputfile = new File(filename);
		    saveGridImage(this.image, outputfile, ppi);
		} catch (IOException e) {
			System.out.println("Error (8): "+e);
		}
		System.out.println("Output image saved...");
	}


	private void findDimensions(int border) {
		int highX = 0;
		int highY = 0;

		for (Object o: operations) {
			if (o instanceof MyCircle) {
				MyCircle c = (MyCircle)o;
				int xmax = c.x+c.diameter;
				if (xmax > highX) highX = xmax;
				int ymax = c.y+c.diameter;
				if (ymax > highY) highY = ymax;

			} else 
			if (o instanceof MyRect) {
				MyRect r = (MyRect)o;
				int xmax = r.x+r.width;
				if (xmax > highX) highX = xmax;
				int ymax = r.y+r.height;
				if (ymax > highY) highY = ymax;
			}

		}

		if (imgw == 0 && imgh == 0) {
			this.imgw = highX + 2 * border;
			this.imgh = highY + 2 * border;
		}	
	}

	public void drawAndWritePNG(String filename, int imgw, int imgh, int ppi, int border, boolean negative) {
		drawAndWritePNG(filename, ppi, border, negative);
	}


	public void drawAndWritePNG(String filename, int ppi, int border, boolean negative) {
		findDimensions(border);
                
                
		System.out.println("Found dimensions imgw: "+this.imgw+" imgh: "+this.imgh);
		newImageFile(this.imgw, this.imgh);
                /*
                g2d.setColor(Color.blue);
                //g2d.fillRect(0, 0, imgw, imgh);
                g2d.setBackground(Color.yellow);
                g2d.setXORMode(Color.green);
                g2d.setColor(Color.red);
                g2d.fillRect(300, 300, 600, 600);
                
                //g2d.setColor(Color.green);
                g2d.fillOval(850,850,200,200);
*/
		if (negative) {
 			g2d.setColor(Color.white);
			g2d.fillRect(0, 0, imgw, imgh);
 			g2d.setColor(Color.black);
		} else {
 			g2d.setColor(Color.white);
		}

		for (Object o: operations) {
			if (o instanceof MyCircle) {
				MyCircle c = (MyCircle)o;
				if (c.inverted) {
		 			g2d.setColor(Color.black);
				}

				g2d.fillOval(border+c.x, border+c.y, c.diameter, c.diameter);

				if (c.inverted) {
		 			g2d.setColor(Color.white);
				}

			} else 
			if (o instanceof MyRect) {
				MyRect r = (MyRect)o;
                                System.out.println("Rectangle: " + (border+r.x) + ", " + (border+r.y) + ", " + r.width + ", " + r.height);
				g2d.fillRect(border+r.x, border+r.y, r.width, r.height);
			}

		}

                
                
		System.out.println("There were "+operations.size()+" operations in memory");
		operations.clear();

		if (negative) {
//			Functions.floodFill(image, new Point(0, 0), Color.black);
		}	

		saveImageFile(filename, ppi);
	}

}