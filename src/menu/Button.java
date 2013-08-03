package menu;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.texture.Texture;
import de.lessvoid.nifty.elements.Element;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import jme3tools.converters.ImageToAwt;

/**
 *
 * @author Inferno
 */
public class Button 
{
    String fileName = "";
    File file = null;
    Element element = null;
    AssetManager assetManager;
    int height = 0;
    int width = 0;
    double perEdX = 0.0; // Percent from edge along the x-axis
    double perEdY = 0.0; // Percent from edge along the y-axis
    double perScrX = 0.0; // Percent width of button
    double perScrY = 0.0; // Percent height of button
    
    Texture texture = null;
    Image image = null;
    BufferedImage buffImage = null;
    
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
    * This is used to make a Button with pathnames
    * @param app Should just be app
    * @param pathname From Asset manager, so assumed to being with assets/ already
    * @param perEdX Percent from Left Edge
    * @param perEdY Percent from Upper Edge
    * @param perScrX Percent of Horizontal Screen taken by Button
    * @param perScrY Percent of Vertical Screen taken by Button
    * @param needsFlip Is it upside down?
    */
   public Button(SimpleApplication app, String pathname, double perEdX, double perEdY, double perScrX, double perScrY, boolean needsFlip)
   {
       TextureKey key = new TextureKey(pathname, needsFlip);
       this.assetManager = app.getAssetManager();
       this.texture = assetManager.loadTexture(key);
       this.makeBufferedImage();
       this.perEdX = perEdX;
       this.perEdY = perEdY;
       this.perScrX = perScrX;
       this.perScrY = perScrY;
       this.refresh();
   }
    
   
   /**
    * This is used to make Buttons with exact pixel locations
    * @param app Should just be app
    * @param pathname From Asset manager, so assumed to being with assets/ already
    * @param element The actual Element the image is located on
    * @param needsFlip Is it upside down (can use createImage() to check)?
    */
    public Button(SimpleApplication app, String pathname, Element element, boolean needsFlip)
    {
        TextureKey key = new TextureKey(pathname, needsFlip);
        this.assetManager = app.getAssetManager();
        this.texture = assetManager.loadTexture(key);
        this.element = element;
        this.makeBufferedImage();
        this.ratioX = 1;
        this.ratioY = 1;
        this.mapX = element.getX();
        this.mapY = element.getY();
    }
    
    /**
     * @deprecated 
     * @return 
     */
    public boolean fileExists()
    {
        return this.file.exists();
    }
    
    public void makeBufferedImage()
    {
        buffImage = ImageToAwt.convert(texture.getImage(), false, true, 0);
    }
    
    public int getButtonHeight()
    {
        return this.height;
    }
    
    public int getButtonWidth()
    {
        return this.width;
    }
    
    /**
     * Given a pixel location, tells if the Button is clickable or not (if the pixel is fully transparent, it is not clickable)
     * @param x
     * @param y
     * @return 
     */
    public boolean isClickable(int x, int y)
    {
        int imageX = (int) ((x - mapX) * ratioX);
        int imageY = (int) ((y - mapY) * ratioY);
        
        if ((imageX < 0) || (imageX >= width) || (imageY < 0) || (imageY >= height))
        {
            return false;
        }
        
        System.out.println("X on image: " + imageX + ", Y on image: " + imageY);
        int pixelColorInt = buffImage.getRGB(imageX, imageY);
        Color pixelColor = new Color(pixelColorInt, true);
        System.out.println(pixelColor.getRed() + ", " + pixelColor.getGreen() + ", " + pixelColor.getBlue() + ", " + pixelColor.getAlpha());
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
    
    /**
     * Refreshes Button data
     */
    public void refresh()
    {
        this.height = this.buffImage.getHeight();
        this.width = this.buffImage.getWidth();
        this.screenWidth = MainMenu.screenWidth;
        this.screenHeight = MainMenu.screenHeight;
        this.imgScrX = perScrX * screenWidth;
        this.imgScrY = perScrY * screenHeight;
        this.ratioX = width/imgScrX;
        this.ratioY = height/imgScrY;
        this.mapX = (int)(MainMenu.screenWidth * perEdX);
        this.mapY = (int) (MainMenu.screenHeight * perEdY);
    }
    
    /**
     * Refreshes mapping on mobile Buttons
     */
    public void mobileRefresh()
    {
        this.height= this.buffImage.getHeight();
        this.width = this.buffImage.getWidth();
        this.mapX = element.getX();
        this.mapY = element.getY();
    }
    
    // This is incredibly useful. Make sure to keep track of this. Just search for "saved" in Windows to find file and look
    /**
     * Creates an actual image to view, used mainly for Debugging
     */
    public void createImage()
    {
        try
        {
            File outputfile = new File("saved.png");
            System.out.println(outputfile.exists());
            ImageIO.write(buffImage, "png", outputfile);
        }
        catch (IOException e)
        {
            System.out.println("Fail");
        }
    }
}
