/**
 * Brian Li
 * 05/23/2017
 * BL_SeamCarver.java
 * 
 * Required Files: StdDraw.java, Picture.java, demo.png
 * 
 * Princeton Univeristy COS 226 Programming Assignment 7: Seam Carving
 * http://www.cs.princeton.edu/courses/archive/spring17/cos226/assignments/seamCarving.html
 */
 
import java.awt.Color;

public class BL_SeamCarver {
    // instance variables of the class
    Picture picture;
    
    public BL_SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        // throw a NullPointerException if the constructor is called with a null argument
        if(picture == null) throw new java.lang.NullPointerException();
        this.picture = picture;
        // set the origin location to be in the upper left corner
        picture.setOriginUpperLeft();
    }
    
    public Picture picture()                          // current picture
    {
        return picture;
    }

    public int width()                            // width of current picture
    {
        return picture.width();
    }

    public int height()                           // height of current picture
    {
        return picture.height();
    }

    public double energy(int x, int y)               // energy of pixel at column x and row y
    {
        // throw an IndexOutOfBoundsException if either the x or the y value are not valid
        if(x < 0 || x > picture.width() - 1) throw new java.lang.IndexOutOfBoundsException();
        if(y < 0 || y > picture.height() - 1) throw new java.lang.IndexOutOfBoundsException();
        Color c = picture.get(x, y);
        
        // energy counter
        double energy = 0;
        
        int left, right, top, bot;
        
        // check if column x is for a border pixel; find energy across row
        if(x == 0)
        {
            left = x + 1;
            right = x + 3;
        }
        else if(x == picture.width() - 1)
        {
            left = x - 3;
            right = x - 1;
        }
        else
        {
            left = x - 1;
            right = x + 1;
        }
        // get x red energy
        energy += Math.pow(picture.get(left, y).getRed() - picture.get(right, y).getRed(), 2);
        // get x green energy
        energy += Math.pow(picture.get(left, y).getGreen() - picture.get(right, y).getGreen(), 2);
        // get x blue energy
        energy += Math.pow(picture.get(left, y).getBlue() - picture.get(right, y).getBlue(), 2);
            
        // check if row y is for a border pixel; find energy in column
        if(y == 0)
        {
            top = y + 1;
            bot = y + 3;
        }
        else if(y == picture.height() - 1)
        {
            top = y - 3;
            bot = y - 1;
        }
        else
        {
            top = y + 1;
            bot = y - 1;
        }
        // get y red energy
        energy += Math.pow(picture.get(x, bot).getRed() - picture.get(x, top).getRed(), 2);
        // get y green energy
        energy += Math.pow(picture.get(x, bot).getGreen() - picture.get(x, top).getGreen(), 2);
        // get y blue energy
        energy += Math.pow(picture.get(x, bot).getBlue() - picture.get(x, top).getBlue(), 2);
        
        // return the energy value
        return Math.sqrt(energy);
    }
    
    public int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        // get the energy matrix
        double[][] energyMatrix = getEnergyMatrix();
        
        // keep a 2D array that stores the energy to reach a point (i, j)
        double[][] energyTo = new double[width()][height()];
        
        // create variable for seam
        int[] seam = new int[width()];
        
        // iterate through energyTo to set all of the first column energies to appropriate values
        for(int i = 0; i < width(); i++)
        {
            for(int j = 0; j < height(); j++)
            {
                if(i == 0)
                {
                    energyTo[i][j] = energyMatrix[i][j];
                }
                else
                {
                    energyTo[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
        
        // iterate through each point in each column to find the lowest energy to each pixel in the last column
        for(int i = 0; i < width() - 1; i++)
        {
            for(int j = 0; j < height(); j++)
            {
                for(int k = j - 1; k <= j + 1; k++)
                {
                    if(k >= 0 && k < height())
                    {
                        if(energyTo[i][j] + energyMatrix[i+1][k] < energyTo[i+1][k])
                        {
                            energyTo[i+1][k] = energyTo[i][j] + energyMatrix[i+1][k];
                        }
                    }
                }
            }
        }
        
        // find the minimum index in the last column
        double min = Double.POSITIVE_INFINITY;
        int minIndex = 0;
        for(int j = 0; j < height(); j++)
        {
            if(energyTo[width() - 1][j] < min)
            {
                min = energyTo[width()-1][j];
                minIndex = j;
            }
        }
        
        System.out.println("The min index is " +  minIndex);
        // back track for the path
        seam[seam.length - 1] = minIndex;
        for(int i = seam.length - 2; i >= 0; i--)
        {
            min = Double.POSITIVE_INFINITY;
            int holder = minIndex;
            for(int k = holder - 1; k <= holder + 1; k++)
            {
                if(k >= 0 && k < height())
                {
                    if(energyMatrix[i][k] < min)
                    {
                        min = energyMatrix[i][k];
                        minIndex = k;
                    }
                }
            }
            seam[i] = minIndex;
        }
        
        verifySeam(seam, false);
        
        return seam;
    }

    public int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        // get the energy matrix
        double[][] energyMatrix = getEnergyMatrix();
        
        // keep a 2D array that stores the energy to reach a point (i, j)
        double[][] energyTo = new double[width()][height()];
        
        // create variable for seam
        int[] seam = new int[height()];
        
        // iterate through energyTo to set all of the first row energies to appropriate values
        for(int i = 0; i < width(); i++)
        {
            for(int j = 0; j < height(); j++)
            {
                if(j == 0)
                {
                    energyTo[i][j] = energyMatrix[i][j];
                }
                else
                {
                    energyTo[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
        
        // iterate through each point in each row to find the lowest energy to each pixel in the last row
        for(int j = 0; j < height() - 1; j++)
        {
            for(int i = 0; i < width(); i++)
            {
                for(int k = i - 1; k <= i + 1; k++)
                {
                    if(k >= 0 && k < width())
                    {
                        if(energyTo[i][j] + energyMatrix[k][j+1] < energyTo[k][j+1])
                        {
                            energyTo[k][j+1] = energyTo[i][j] + energyMatrix[k][j+1];
                        }
                    }
                }
            }
        }
        
        // find the minimum index in the last row
        double min = Double.POSITIVE_INFINITY;
        int minIndex = 0;
        for(int i = 0; i < width(); i++)
        {
            if(energyTo[i][height() - 1] < min)
            {
                min = energyTo[i][height() - 1];
                minIndex = i;
            }
        }
        
        System.out.println("The min index is " +  minIndex);
        // back track for the path
        seam[seam.length - 1] = minIndex;
        for(int j = seam.length - 2; j >= 0; j--)
        {
            min = Double.POSITIVE_INFINITY;
            int holder = minIndex;
            for(int k = holder - 1; k <= holder + 1; k++)
            {
                if(k >= 0 && k < width())
                {
                    if(energyMatrix[k][j] < min)
                    {
                        min = energyMatrix[k][j];
                        minIndex = k;
                    }
                }
            }
            seam[j] = minIndex;
        }
        
        verifySeam(seam, false);
        
        return seam;
    }
    
    /**
     * helper method to verify if an entered seam is valid
     */
    private boolean verifySeam(int[] seam, boolean isVertical)
    {
        if(isVertical)
        {
            if(seam.length != picture.height())
            {
                return false;
            }
            for(int i = 0; i < seam.length; i++)
            {
                if(seam[i] < 0 || seam[i] > picture.width() - 1)
                {
                    return false;
                }
                
                if(i < seam.length - 2)
                {
                    // check distance between pixels
                    if(Math.abs(seam[i] - seam[i+1]) > 1) 
                    {
                        return false;
                    }
                }
            }
        }
        else
        {
            if(seam.length != picture.width())
            {
                return false;
            }
            for(int j = 0; j < seam.length; j++)
            {
                if(seam[j] < 0 || seam[j] > picture.height() - 1)
                {
                    return false;
                }
                
                if(j < seam.length - 2)
                {
                    // check distance between pixels
                    if(Math.abs(seam[j] - seam[j+1]) > 1)
                    {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
        // throw a NullPointerException if called with a null argument
        if(seam == null) throw new java.lang.NullPointerException();
        
        // throw a IllegalArgumentException if the seam is not valid
        if(!verifySeam(seam, false)) throw new java.lang.IllegalArgumentException();
        
        // throw a IllegalArgumentException if the width or height of the picture is 1
        if(picture.width() == 1 || picture.height() == 1) throw new java.lang.IllegalArgumentException();
        
        // create a new picture
        Picture newPic = new Picture(width(), height() - 1);
        
        // copy picture except seam for removal
        for(int i = 0; i < newPic.width(); i++)
        {
            for(int j = 0; j < newPic.height(); j++)
            {
                if(j < seam[i])
                {
                    newPic.set(i, j, picture.get(i, j));
                }
                else
                {
                    newPic.set(i, j, picture.get(i, j + 1));
                }
            }
        }
        
        // update the picture
        picture = newPic;
    }

    public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
        // throw a NullPointerException if called with a null argument
        if(seam == null) throw new java.lang.NullPointerException();
        
        // throw a IllegalArgumentException if the seam is not valid
        if(!verifySeam(seam, true)) throw new java.lang.IllegalArgumentException();
        
        // throw a IllegalArgumentException if the width or height of the picture is 1
        if(picture.width() == 1 || picture.height() == 1) throw new java.lang.IllegalArgumentException();
        
        // create a new picture
        Picture newPic = new Picture(width() - 1, height());
        
        // copy image except seam for removal
        for(int j = 0; j < newPic.height(); j++)
        {
            for(int i = 0; i < newPic.width(); i++)
            {
                if(i < seam[j])
                {
                    newPic.set(i, j, picture.get(i, j));
                }
                else
                {
                    newPic.set(i, j, picture.get(i + 1, j));
                }
            }
        }
        
        // update the picture 
        picture = newPic;
    }
    
    public void show()                          // show the updated picture
    {
        picture.show();
    }
    
    /**
     * private helper method to return the energy of each pixel in matrix form
     */
    private double[][] getEnergyMatrix()
    {
        double[][] energyMatrix = new double[width()][height()];
        
        // calculate the energy of each pixel
        for(int i = 0; i < energyMatrix.length; i++)
        {
            for(int j = 0; j < energyMatrix[0].length; j++)
            {
                energyMatrix[i][j] = energy(i, j);
            }
        }
        
        return energyMatrix;
    }
    
    public static void main(String[] args)            // do unit testing of this class
    {
        // instantiate objects
        Picture pic = new Picture("demo.png");
        BL_SeamCarver sc = new BL_SeamCarver(pic);
        // show the original image
        sc.show();
        
        // number of seams to be removed
        int numSeams = 150;
        for(int n = 0; n < numSeams; n++)
        {
            // diagnostic for progress
            System.out.println("Seam #" + n + " out of " + numSeams);
            
            // remove vertical seams
            int[] seam = sc.findVerticalSeam();
            sc.removeVerticalSeam(seam);
        }
        
        // show the final product
        sc.show();
    }
}
