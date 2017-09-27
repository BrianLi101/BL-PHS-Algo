/**
 * Brian Li
 * 02/13/17
 * BL_Maze.java
 * 
 * Required Files: StdDraw.java
 * 
 * Constructs a maze by knocking down some of the walls.
 * Starts at the lower level cell (1, 1).
 * Finds a neighbor at random that hasn't been visited.
 * If one is found, moves there, knocking down the wall. Otherwise, goes back to the previous cell.
 * Repeats steps until every cell in the grid has been visited.
 */

import java.util.Scanner;
public class BL_Maze
{
    private static boolean north[][];
    private static boolean south[][];
    private static boolean east[][];
    private static boolean west[][];
    private static int N;
    private static double C = 0.05;
    private static boolean grid[][];
    private static boolean visited[][];
    private static boolean completed = false;
    private static double scale;

    /**
     * method that initiates all of the variables associated with the program
     */
    public static void initiate()
    {
        // initalization of all private instance variables
        north = new boolean[N+2][N+2];
        south = new boolean[N+2][N+2];
        east = new boolean[N+2][N+2];
        west = new boolean[N+2][N+2];
        grid = new boolean[N+2][N+2];
        visited = new boolean[N+2][N+2];

        // store inital values into arrays
        for(int i = 0; i < N + 2; i++)
        {
            for(int j = 0; j < N + 2; j++)
            {
                north[i][j] = true;
                south[i][j] = true;
                east[i][j] = true;
                west[i][j] = true;
                grid[i][j] = false;
                visited[i][j] = false;
            }
        }
    }

    /**
     * draw an initially empty grid on the screen
     */
    public static void drawBoard()
    {
        StdDraw.setXscale(0.0, 1.0);
        StdDraw.setYscale(0.0, 1.0);

        StdDraw.setPenColor(StdDraw.BLACK);

        scale = (1.0 - 2*C)/(N);

        for(int i = 0; i < N + 1; i++)
        {
            for(int j = 0; j < N + 1; j++)
            {
                if(i < N)
                {
                    // horizontal line from point
                    StdDraw.line(C + i*scale, C + j*scale, C + (i+1)*scale, C + j*scale);
                }

                if(j < N)
                {
                    // vertical line from point
                    StdDraw.line(C + i*scale, C + j*scale, C + i*scale, C + (j+1)*scale);
                }
            }
        }
    }

    /**
     * recursive method for knocking down walls based on the algorithm
     */
    public static void knockWalls(int x, int y)
    {
        grid[x][y] = true;
        
        boolean possibilities[] = {true, true, true, true};

        while(possibilities[0] || possibilities[1] || possibilities[2] || possibilities[3])
        {
            int next = (int) (Math.random() * 4);
            
            if(x - 1 >= 1)
            {
                if(grid[x - 1][y])
                {
                    possibilities[0] = false;
                }
            }
            else
            {
                possibilities[0] = false;
            }

            if(y + 1 <= N)
            {
                if(grid[x][y + 1])
                {
                    possibilities[1] = false;
                }
            }
            else
            {
                possibilities[1] = false;
            }

            if(x + 1 <= N)
            {
                if(grid[x + 1][y])
                {
                    possibilities[2] = false;
                }
            }
            else
            {
                possibilities[2] = false;
            }

            if(y - 1 >= 1)
            {
                if(grid[x][y - 1])
                {
                    possibilities[3] = false;
                }
            }
            else
            {
                possibilities[3] = false;
            }

            if(next == 0 && possibilities[next] && !grid[x-1][y])
            {
                // knock wall
                StdDraw.line(C + (x-1)*scale, C + (y-1)*scale, C + (x-1)*scale, C + (y)*scale);
                west[x][y] = false;
                east[x-1][y] = false;
                possibilities[next] = false;
                knockWalls(x - 1, y);
            }
            else if(next == 1 && possibilities[next] && !grid[x][y+1])
            {
                // knock wall
                StdDraw.line(C + (x-1)*scale, C + (y)*scale, C + (x)*scale, C + (y)*scale);
                north[x][y] = false;
                south[x][y+1] = false;
                possibilities[next] = false;
                knockWalls(x, y + 1);
            }
            else if(next == 2 && possibilities[next] && !grid[x+1][y])
            {
                // knock wall
                StdDraw.line(C + (x)*scale, C + (y-1)*scale, C + (x)*scale, C + (y)*scale);
                east[x][y] = false;
                west[x+1][y] = false;
                possibilities[next] = false;
                knockWalls(x + 1, y);
            }
            else if(next == 3 && possibilities[next] && !grid[x][y-1])
            {
                // knock wall
                StdDraw.line(C + (x-1)*scale, C + (y-1)*scale, C + (x)*scale, C + (y-1)*scale);
                south[x][y] = false;
                north[x][y-1] = false;
                possibilities[next] = false;
                knockWalls(x, y - 1);
            }
        }
    }
    
    /**
     * method to explore the maze
     */
    public static void explore(int x, int y)
    {
        visited[x][y] = true;
        StdDraw.filledSquare(C + (x-0.5)*scale, C + (y-0.5)*scale, scale/3);
        
        if(x == N && y == N)
        {
            completed = true;
        }
        /**
        try {
            Thread.sleep(50); //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
        }
        **/
        while(!completed)
        {
            if(!north[x][y] && !visited[x][y+1] && y+1 <= N)
            {
                explore(x, y+1);
            }
            else if(!east[x][y] && !visited[x+1][y] && x+1 <= N)
            {
                explore(x+1, y);
            }
            else if(!south[x][y] && !visited[x][y-1] && y-1 >= 1)
            {
                explore(x, y-1);
            }
            else if(!west[x][y] && !visited[x-1][y] && x-1 >= 1)
            {
                explore(x-1, y);
            }
            else
            {
                return;
            }
        }
        
        if(x == 1 && y == 1 && completed)
        {
            System.out.println("done");
        }
    }
    
    public static void check()
    {
        for(int i = 0; i < N+2; i++)
        {
            for(int j = 0; j < N+2; j++)
            {
                System.out.print(north[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args)
    {
        // prompt the user
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter an N value for the maze: ");
        N = sc.nextInt();
        
        // start program
        initiate();
        drawBoard();

        // reset pen color to draw over black lines when knocking down walls
        StdDraw.setPenColor(StdDraw.WHITE);
        
        // knock down the walls to build a maze
        knockWalls(1, 1);
        
        // reset pen color to draw path
        StdDraw.setPenColor(StdDraw.RED);
        
        // run explore
        explore(1, 1);
    }
}
