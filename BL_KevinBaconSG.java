/**
 * Brian Li
 * 02/21/17
 * BL_KevinBaconSG.java
 * 
 * Required Files: movies.txt, BreadthFirstPaths.java, SymbolGraph.java, StdDraw.java
 *
 * Generates a visual Symbol Graph from the movies.txt file.
 * Prompts the user for how many degrees of separation from a vertex, in this case the actor Kevin Bacon,
 * and draws a graph with the names of the performers and the movies that are connected to him by that many links.
 */
import java.util.*;
import java.awt.Font;

public class BL_KevinBaconSG
{
    private static int N;
    private static double scale;
    private static Graph G;
    private static SymbolGraph sg;
    private static int fontSize = 14;

    public static void designScaling()
    {
        StdDraw.setXscale(0.0, 1.0);
        StdDraw.setYscale(0.0, 1.0);

        scale = 1.0 / (N*4 + 3);

        StdDraw.setPenColor(StdDraw.BLACK);
    }

    public static void recurseLayer(int counter, int vertex, double x, double y, double newScale)
    {
        if(counter < N)
        {
            BreadthFirstPaths bfs = new BreadthFirstPaths(G, vertex);

            ArrayList<Integer> subPaths = new ArrayList<>();

            for(int i = 0; i < G.V(); i++)
            {
                if(bfs.hasPathTo(i) && bfs.distTo(i) == 1)
                {
                    subPaths.add(i);
                }
            }

            //System.out.println(vertex + " " + subPaths.size());
            double angleScale = 360.0/(subPaths.size());

            int j = 0;
            for(int a: subPaths)
            {
                if(a != vertex)
                {
                    double xNew = x + 2*Math.cos(Math.toRadians((j*angleScale)))*newScale;
                    double yNew = y + 2*Math.sin(Math.toRadians((j*angleScale)))*newScale;

                    if(counter < N)
                    {
                        recurseLayer(counter + 1, a, xNew, yNew, newScale/2);
                    }
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.line(x, y, xNew, yNew);
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.filledSquare(xNew, yNew, newScale/4);
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.square(xNew, yNew, newScale/4);

                    Font f = new Font("ITALIC", Font.BOLD, (int)(fontSize*scale));
                    StdDraw.setFont(f);
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.text(xNew, yNew, sg.name(a));
            
                    j += 1;
                }
            }
        }
        
        if(counter == 0)
        {
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.filledSquare(x, y, scale/2);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.square(x, y, scale/2);
            
            Font f = new Font("ITALIC", Font.BOLD, (int)(fontSize*scale*4));
            StdDraw.setFont(f);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.text(x, y, sg.name(vertex));
        }
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        String start = "Bacon, Kevin";//"Gregg, Clark";//"Bale, Christian";
        System.out.print("Enter an N value: ");
        N = sc.nextInt();
        sg = new SymbolGraph("movies.txt", "/");
        G = sg.G();
        
        int s = sg.index(start);
        System.out.println("Start Index: " + s);
        BreadthFirstPaths bfs = new BreadthFirstPaths(G, s);

        ArrayList<Integer> hasPath = new ArrayList<>();

        for(int i = 0; i < G.V(); i++)
        {
            if(bfs.hasPathTo(i) && bfs.distTo(i) == N)
            {
                hasPath.add(i);
            }
        }

        designScaling();

        recurseLayer(0, s, 0.5, 0.5, scale);
        /**
         * Prompt the user for how many degrees of separation from a vertex, 
         * in this case Kevin Bacon and draw a graph with the name of the performers 
         * and the movies they are connected with. 
         */
    }
}
