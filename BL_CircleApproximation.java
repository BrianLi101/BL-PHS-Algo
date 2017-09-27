/**
 * Brian Li
 * 9/12/16
 * BL_CircleApproximation.java
 * 
 * Required Files: Turtle.java
 * 
 * this code takes a maximum percent change between the area of a polygon of n and n-1 sides
 * to find a close approximation for a circle. it does so by prompting the user for a maximum
 * error value and then running a recursive function to compare the areas of two polygon
 * approximations. when it reaches the n value that satisfies the conditions, it prints out
 * n and draws a comparison.
 */
import java.util.Scanner;
public class BL_CircleApproximation
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("This program finds a polygon circle approximation that has an area within a certain percent error of the previous approximation.");
        System.out.print("Enter the percent change as a decimal: ");
        double errorSize = sc.nextDouble();
        
        int target = recursiveComparison(errorSize, 3, 0);
        System.out.println("A polygon of " + target + " sides is the best circle approximation that fits your criteria.");
        Ngon(target);
    }
    
    public static int recursiveComparison(double errorSize, int n, double previousArea)
    {
        double area = getArea(n);
        System.out.println("The area of a circle approximation with " + n + " sides is " + area + ".");
        double currentError = Math.abs((previousArea - area) / area);
        if(currentError < errorSize && currentError > 0.0)
        {
            System.out.println();
            System.out.println("The percent change as a decimal between a circle approximation of " + n + " sides and one of " + (n-1) + " sides is " + currentError + ".");
            return n;
        }
        else
        {
            return recursiveComparison(errorSize, n + 1, area);
        }
    }
    
    public static double getArea(int numSides)
    {
        double angle = 360.0 / numSides;
        return 0.5 * numSides * Math.sin(Math.toRadians(angle));
    }
    
    public static void Ngon(int numSides)
    {
        int n = numSides;
        double angle = 360.0 / n;
        double step  = Math.sin(Math.toRadians(angle/2.0));   // sin(pi/n)
        Turtle turtle = new Turtle(0.5, 0.0, angle/2.0);
        for (int i = 0; i < n; i++) {
            turtle.goForward(step);
            turtle.turnLeft(angle);
        }
        
        StdDraw.circle(0.5, 0.5, 0.5);
    }
}

// Input and Output
/**
This program finds a polygon circle approximation that has an area within a certain percent error of the previous approximation.
Enter the percent change as a decimal: 0.01
The area of a circle approximation with 3 sides is 1.299038105676658.
The area of a circle approximation with 4 sides is 2.0.
The area of a circle approximation with 5 sides is 2.3776412907378837.
The area of a circle approximation with 6 sides is 2.598076211353316.
The area of a circle approximation with 7 sides is 2.7364101886381045.
The area of a circle approximation with 8 sides is 2.82842712474619.
The area of a circle approximation with 9 sides is 2.892544243589427.
The area of a circle approximation with 10 sides is 2.938926261462366.
The area of a circle approximation with 11 sides is 2.9735244960057865.
The area of a circle approximation with 12 sides is 2.9999999999999996.

The percent change as a decimal between a circle approximation of 12 sides and one of 11 sides is 0.00882516799807102.
A polygon of 12 sides is the best circle approximation that fits your criteria.

 */
