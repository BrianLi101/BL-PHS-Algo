/**
 * Brian Li
 * 9/12/16
 * BL_KochArea.java
 * 
 * Required Files: Turtle.java
 *
 * this program demonstrates the effect of increasing order on the area between a Koch
 * Curve and the x-axis. it prompts the user for a certain percent change as a decimal
 * between two consecutive Koch curves of different magnitude as a bench mark for the
 * minimum error. then it recursively finds the area and compares it to that of the 
 * previous Koch Curve. upon reaching a target value for the order, it draws the Koch
 * curve.
 */
import java.util.Scanner;
public class BL_KochArea
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("This program demonstrates the effect of increasing order on the area between a Koch Curve and the x-axis.");
        System.out.println("It will find an order and calculate the area based on a percent change of area from the Koch Curve that was one lower in order.");
        System.out.print("Enter the percent change as a decimal: ");
        double errorSize = sc.nextDouble();
        
        int target = recursiveComparison(errorSize, 1, 0);
        
        int n = target;
        double step = 1.0 / Math.pow(3.0, n); 
        Turtle turtle = new Turtle(0.0, 0.0, 0.0); 
        koch(n, step, turtle); 
    }
    
    public static int recursiveComparison(double errorSize, int n, double previousArea)
    {
        double area = getArea(n, 1);
        System.out.println("The area of a Koch Curve of order " + n + " is " + area + ".");
        double currentError = Math.abs((previousArea - area) / area);
        if(currentError < errorSize && currentError > 0.0)
        {
            System.out.println();
            System.out.println("The percent change as a decimal between a Koch Curve of order " + n + " and one of order " + (n-1) + " is " + currentError + ".");
            return n;
        }
        else
        {
            return recursiveComparison(errorSize, n + 1, area);
        }
    }
    
    public static double getArea(int order, double baseArea)
    {
        if(order == 1)
        {
            return 1 * baseArea;
        }
        else
        {
            double orderArea = Math.pow(4, order - 1) * Math.pow(1.0/9.0, order - 1);
            return orderArea + getArea(order - 1, baseArea);
        }
    }
    
    // plot Koch curve of order n, with given step size
    public static void koch(int n, double step, Turtle turtle)
    {
        if (n == 0) {
            turtle.goForward(step);
            return;
        }
        koch(n-1, step, turtle);
        turtle.turnLeft(60.0);
        koch(n-1, step, turtle);
        turtle.turnLeft(-120.0);
        koch(n-1, step, turtle);
        turtle.turnLeft(60.0);
        koch(n-1, step, turtle);
    }
}

// Input and Output
/**
This program demonstrates the effect of increasing order on the area between a Koch Curve and the x-axis.
It will find an order and calculate the area based on a percent change of area from the Koch Curve that was one lower in order.
Enter the percent change as a decimal: 0.01
The area of a Koch Curve of order 1 is 1.0.
The area of a Koch Curve of order 2 is 1.4444444444444444.
The area of a Koch Curve of order 3 is 1.6419753086419753.
The area of a Koch Curve of order 4 is 1.7297668038408778.
The area of a Koch Curve of order 5 is 1.7687852461515012.
The area of a Koch Curve of order 6 is 1.7861267760673338.

The percent change as a decimal between a Koch Curve of order 6 and one of order 5 is 0.009709014023077863.

 */
