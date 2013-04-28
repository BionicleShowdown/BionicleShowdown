package menu;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Inferno
 */
public class Button 
{
    String fileName;
    File file;
    int height;
    int width;
    double perEdX; // Percent from edge along the x-axis
    double perEdY; // Percent from edge along the y-axis
    double perScrX; // Percent width of button
    double perScrY; // Percent height of button
    BufferedImage image = null;
    
    int screenWidth;
    int screenHeight;
    
    double imgScrX;
    double imgScrY;
    
    double ratioX; // The ratio of the button Image's width to the width it has on the screen
    double ratioY; // The ratio of the button Image's height to the height it has on the screen
    
    int mapX; // How far the x-component of the cursor's click needs to be shifted to coorespond to the correct pixel on the image
    int mapY; // How far the y-component of the cursor's click needs to be shifted to coorespond to the correct pixel on the image
    
   public Button() // Null Constructor
    {
    }
    
    public Button(File file, double perEdX, double perEdY, double perScrX, double perScrY)
    {
        this.file = file;
        this.makeBufferedImage();
        this.perEdX = perEdX;
        this.perEdY = perEdY;
        this.perScrX = perScrX;
        this.perScrY = perScrY;
        this.refresh(); // Make sures all necessary values dependent on the input values are initialized correctly 
        
    }
    
    public boolean fileExists()
    {
        return this.file.exists();
    }
    
    public void makeBufferedImage() 
    {
       
       try
       {
           image = ImageIO.read(this.file);
       }
       //File file = new File(this.fileName);
       
       catch (IOException e)
       {
           System.out.println("Error");
       }
      
    }
    
    public int getButtonHeight()
    {
        return this.height;
    }
    
    public int getButtonWidth()
    {
        return this.width;
    }
    
    public boolean isClickable(int x, int y)
    {
        int imageX = (int) ((x - mapX) * ratioX);
        int imageY = (int) ((y - mapY) * ratioY);
        int pixelColorInt = image.getRGB(imageX, imageY);
        Color pixelColor = new Color(pixelColorInt, true);
        boolean notAlpha = false;
        
        if (pixelColor.getAlpha() != 0)
        {
            notAlpha = true;
        }
        
        return notAlpha;
    }
    
    public void printAll()
    {
        System.out.println("Height: " + height);
        System.out.println("Width: " + width);
        System.out.println("Percent x from Edge: " + perEdX);
        System.out.println("Percent y from Edge: " + perEdY);
        System.out.println("Percent x on Screen: " + perScrX);
        System.out.println("Percent y on Screen: " + perScrY);
        System.out.println("Screen Width: " + screenWidth);
        System.out.println("Screen Height: " + screenHeight);
        System.out.println("X size of image on screen: " + imgScrX);
        System.out.println("Y size of image on screen: " + imgScrY);
        System.out.println("Ratio of x: " + ratioX);
        System.out.println("Ratio of y: " + ratioY);
        System.out.println("Map of x: " + mapX);
        System.out.println("Map of y: " + mapY);
    }
    
    public void refresh()
    {
        this.height = this.image.getHeight();
        this.width = this.image.getWidth();
        this.screenWidth = menu.screenWidth;
        this.screenHeight = menu.screenHeight;
        this.imgScrX = perScrX * screenWidth;
        this.imgScrY = perScrY * screenHeight;
        this.ratioX = width/imgScrX;
        this.ratioY = height/imgScrY;
        this.mapX = (int)(menu.screenWidth * perEdX);
        this.mapY = (int) (menu.screenHeight * perEdY);
    }
    
    
    
    
}
