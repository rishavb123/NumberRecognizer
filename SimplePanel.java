package io.bhagat.projects.handwrittendigits;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SimplePanel extends JPanel{

	private static final long serialVersionUID = -9177822655558213008L;
	
	private BufferedImage image;
	private String str;

    public SimplePanel(BufferedImage image, String str) {
       setImage(image);
       setStr(str);
    }
    
    public SimplePanel(String filepath, String str) throws IOException {
    	this(ImageIO.read(new File(filepath)), str);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); 
        String[] strs = str.split("\n");
        for(int i = 0; i < strs.length; i++)
	        g.drawString(strs[i], 210, 10 + 20*i);
    }

	/**
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(String filepath) {
		try {
			setImage(ImageIO.read(new File(filepath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param image the image to set
	 */
	public void setImage(BufferedImage image) {
		BufferedImage bi = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		Graphics2D grph = (Graphics2D) bi.getGraphics();
		grph.scale(200.0/image.getWidth(), 200.0/image.getHeight());
		grph.drawImage(image, 0, 0, null);
		grph.dispose();
		this.image = bi;
	}

	/**
	 * @return the str
	 */
	public String getStr() {
		return str;
	}

	/**
	 * @param str the str to set
	 */
	public void setStr(String str) {
		this.str = str;
	}

}