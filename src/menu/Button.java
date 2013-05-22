package menu;

import de.lessvoid.nifty.elements.Element;
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
    String fileName = "";
    File file = null;
    Element element = null;
    int height = 0;
    int width = 0;
    double perEdX = 0.0; // Percent from edge along the x-axis
    double perEdY = 0.0; // Percent from edge along the y-axis
    double perScrX = 0.0; // Percent width of button
    double perScrY = 0.0; // Percent height of button
    BufferedImage image = null;
    
    int screenWidth = 0;
    int screenHeight = 0;
    
    double imgScrX = 0.0;
    double imgScrY = 0.0;
    
    double ratioX = 0.0; // The ratio of the button Image's width to the width it has on the screen
    double ratioY = 0.0; // The ratio of the button Image's height to the height it has on the screen
    
    int mapX = 0; // How far the x-component of the cursor's click needs to be shifted to coorespond to the correct pixel on the image
    int mapY = 0; // How far the y-component of the cursor's click needs to be shifted to coorespond to the correct pixel on the image
    
   public Button() // Null Constructor
    {
    }
    
   /**
    * Used to make Buttons with relative (percentage) locations and sizes.
    * @param file
    * @param perEdX
    * @param perEdY
    * @param perScrX
    * @param perScrY 
    */
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
    
    /**
     * Used to make Buttons with exact pixel locations.
     * @param file
     * @param element The actual element
     */
    public Button(File file, Element element)
    {
        this.file = file;
        this.element = element;
        this.makeBufferedImage();
        this.ratioX = 1;
        this.ratioY = 1;
        this.mapX = element.getX();
        this.mapY = element.getY();
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
        this.screenWidth = MainMenu.screenWidth;
        this.screenHeight = MainMenu.screenHeight;
        this.imgScrX = perScrX * screenWidth;
        this.imgScrY = perScrY * screenHeight;
        this.ratioX = width/imgScrX;
        this.ratioY = height/imgScrY;
        this.mapX = (int)(MainMenu.screenWidth * perEdX);
        this.mapY = (int) (MainMenu.screenHeight * perEdY);
    }
    
    public void mobileRefresh()
    {
        this.mapX = element.getX();
        this.mapY = element.getY();
    }
    
    
}
