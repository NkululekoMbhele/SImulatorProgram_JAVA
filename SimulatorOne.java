/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;

// Used to signal violations of preconditions for
// various shortest path algorithms.
class SimulatorOneException extends RuntimeException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimulatorOneException( String name )
    {
        super( name );
    }
}

// Represents an edge in the graph.
class Edge
{
    public Vertex     dest;   // Second vertex in Edge
    public double     cost;   // Edge cost
    
    public Edge( Vertex d, double c )
    {
        dest = d;
        cost = c;
    }
}

// Represents an entry in the priority queue for Dijkstra's algorithm.
class Path implements Comparable<Path>
{
    public Vertex     dest;   // w
    public double     cost;   // d(w)
    
    public Path( Vertex d, double c )
    {
        dest = d;
        cost = c;
    }
    
    public int compareTo( Path rhs )
    {
        double otherCost = rhs.cost;
        
        return cost < otherCost ? -1 : cost > otherCost ? 1 : 0;
    }
}

// Represents a vertex in the graph.
class Vertex
{
    public String     name;   // Vertex name
    public List<Edge> adj;    // Adjacent vertices
    public double     dist;   // Cost
    public Vertex     prev;   // Previous vertex on shortest path
    public int        scratch;// Extra variable used in algorithm

    public Vertex( String nm )
      { name = nm; adj = new LinkedList<Edge>( ); reset( ); }

    public void reset( )
    //  { dist = SimulatorOne.INFINITY; prev = null; pos = null; scratch = 0; }    
    { dist = SimulatorOne.INFINITY; prev = null; scratch = 0; }
      
   // public PairingHeap.Position<Path> pos;  // Used for dijkstra2 (Chapter 23)
}

// SimulatorOne class: evaluate shortest paths.
//
// CONSTRUCTION: with no parameters.
//
// ******************PUBLIC OPERATIONS**********************
// void addEdge( String v, String w, double cvw )
//                              --> Add additional edge
// void printVPath( String w )   --> Print path after alg is run
// void unweighted( String s )  --> Single-source unweighted
// void dijkstra( String s )    --> Single-source weighted
// void negative( String s )    --> Single-source negative weighted
// void acyclic( String s )     --> Single-source acyclic
// ******************ERRORS*********************************
// Some error checking is performed to make sure graph is ok,
// and to make sure graph satisfies properties needed by each
// algorithm.  Exceptions are thrown if errors are detected.

public class SimulatorOne
{
    public static final double INFINITY = Double.MAX_VALUE;
    private Map<String,Vertex> vertexMap = new HashMap<String,Vertex>( );

    /**
     * Add a new edge to the graph.
     */
    public void addEdge( String sourceName, String destName, double cost )
    {
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName );
        v.adj.add( new Edge( w, cost ) );
    }

    /**
     * Driver routine to handle unreachables and print total cost.
     * It calls recursive routine to print shortest path to
     * destNode after a shortest path algorithm has run.
     */
//     public void printVPath(String destName)
//     {
//            Vertex w = vertexMap.get( destName );
//            System.out.println("victim " + destName);
//            System.out.println("hospital " + startName);
//            printVPath(w);
//            System.out.print(" " + startName);
//        
//     }

    /**
     * If vertexName is not present, add it to vertexMap.
     * In either case, return the Vertex.
     */
   public void printPath( String destName)
    {
        Vertex w = vertexMap.get( destName );
        if( w == null )
            throw new NoSuchElementException( "Destination vertex not found" );
        else if( w.dist == INFINITY )
            System.out.println( destName + " is unreachable" );
        else
        {
            // System.out.print( "(Cost is: " + w.dist + ") " );
            printPath( w );
            
        }
    }
     
    private Vertex getVertex( String vertexName )
    {
        Vertex v = vertexMap.get( vertexName );
        if( v == null )
        {
            v = new Vertex( vertexName );
            vertexMap.put( vertexName, v );
        }
        return v;
    }

    /**
     * Recursive routine to print shortest path to dest
     * after running shortest path algorithm. The path
     * is known to exist.
     */
     
   private void printPath( Vertex dest )
    {
        if( dest.prev != null )
        {
            printPath( dest.prev );
            System.out.print( " " );
        }
        System.out.print( dest.name );
    
//     /**
   }
    public void  printPathBack( Vertex dest)
  
        {
         if( dest.prev != null )
        {
            printPath( dest.prev );
            System.out.print( " " );
        }
        System.out.print( dest.name );
        
// //     /**
   }
   public void backPrint( Vertex dest)
      {
         dest = dest;
         ArrayList<String> path = new ArrayList<String>();
         
         while ( dest.prev != null)
         {
              path.add(dest.name);
              dest = dest.prev;               
         }
         for (int i = path.size() - 1; i > 0 ; i--)
         {
         
            System.out.print(path.get(i) + " ");
         }   
      }
    public void clearAll()
    {
        for( Vertex v : vertexMap.values( ) )
            v.reset( );
    }

    /**
     * Single-source unweighted shortest-path algorithm.
     */
   
    public void dijkstra( String startName )
    {
        PriorityQueue<Path> pq = new PriorityQueue<Path>( );

        Vertex start = vertexMap.get( startName );
        if( start == null )
            throw new NoSuchElementException( "Start vertex not found" );

        clearAll( );
        pq.add( new Path( start, 0 ) ); start.dist = 0;
        
        int nodesSeen = 0;
        while( !pq.isEmpty( ) && nodesSeen < vertexMap.size( ) )
        {
            Path vrec = pq.remove( );
            Vertex v = vrec.dest;
            if( v.scratch != 0 )  // already processed v
                continue;
                
            v.scratch = 1;
            nodesSeen++;

            for( Edge e : v.adj )
            {
                Vertex w = e.dest;
                double cvw = e.cost;
                
                if( cvw < 0 )
                    throw new SimulatorOneException( "SimulatorOne has negative edges" );
                    
                if( w.dist > v.dist + cvw )
                {
                    w.dist = v.dist +cvw;
                    w.prev = v;
                    pq.add( new Path( w, w.dist ) );
                }
          }
          }
    }

      
      
      
      public void roundTrip(String hospital, String victim) 
         {
            dijkstra(hospital);
            Vertex go = vertexMap.get( victim );
            

            printPath( go );
            System.out.print(" ");
            
            dijkstra(victim);
            Vertex back = vertexMap.get( hospital );
            backPrint( back);
            System.out.print(hospital);

            // printPathBack( back , go);
            
            
          
     
         }
         public double singleTripCost(String hospital, String victim) 
         {
            dijkstra(hospital);
            Vertex go = vertexMap.get( victim );
            double singleCost = go.dist;
         
            return singleCost;
        
         }

         
         public double roundTripCost(String hospital, String victim) 
         {
            dijkstra(hospital);
            Vertex go = vertexMap.get( victim );
            double goCost = go.dist;
         
            dijkstra(victim);
            Vertex back = vertexMap.get( hospital );
            double backCost = back.dist;
            
            double totalCost = goCost + backCost;
            return totalCost;
        
         }
        
         public void isMultipleCost(String hospital, String victim, String[] edgesStrings, int nodes)
         {
            double[] allcost = new double[5];
            
            double initialCost = roundTripCost(hospital, victim);
            dijkstra(hospital);
            Vertex go = vertexMap.get( victim );
            Vertex x = go.prev;
            String y = x.name;
            // double yes = x.dist;
            // String previousVert = x.name;
            double epsilon = 0.5;
            double newCost = singleTripCost(hospital, victim) + epsilon;
            int i = 0;
            while( i < nodes )
            {
                       String line = edgesStrings[i];
                       StringTokenizer st = new StringTokenizer( line );
                       String source  = st.nextToken();
                       Vertex go2 = vertexMap.get( source );
                       go2.reset();
                       while (st.hasMoreTokens()) 
                         {      
                             String dest    = st.nextToken();
                             int cost    = Integer.parseInt( st.nextToken() );

                       }
                  
               i++;     
                    }
                    while( i < nodes )
            {
                       String line = edgesStrings[i];
                       StringTokenizer st = new StringTokenizer( line );
                       String source  = st.nextToken();
                       while (st.hasMoreTokens()) 
                         {       
                            
                            String dest    = st.nextToken();
                            double cost    = Integer.parseInt( st.nextToken() );
                            if (source == y && dest == victim)
                            {
                                cost = cost + epsilon;
                            }
                            addEdge( source, dest, cost ); 

                       }
                  
               i++;     
              }
            dijkstra(victim);
            

            Vertex back = vertexMap.get( hospital );
            double backC = back.dist;
            dijkstra(hospital);
            Vertex goCost = vertexMap.get( victim );
            double newC = goCost.dist;
            double finalCost = roundTripCost(hospital, victim);
            allcost[0] = initialCost;
            allcost[1] = newCost;
            allcost[2] = finalCost;
            allcost[3] = backC;
            allcost[4] = newC;  

               for (double element: allcost) {
                   System.out.println(element + " ");
                  }
                     for (String element: edgesStrings) {
                   System.out.println(element + " ");
                  }
            
            
            
            // if (initialCost == finalCost)
//             {
//                return true;
//             }
//             else 
//             {
//             return false;
//             }
        }
            
            
         
      
    /**
     * A main routine that:
     * 1. Reads a file containing edges (supplied as a command-line parameter);
     * 2. Forms the graph;
     * 3. Repeatedly prompts for two vertices and
     *    runs the shortest path algorithm.
     * The data file is a sequence of lines of the format
     *    source destination cost

     */

    public static void main( String [] args )
    {
        SimulatorOne g = new SimulatorOne( );
        // try
//         {   	
            //FileReader fin = new FileReader(args[0]);
        	//FileReader fin = new FileReader("SimulatorOne1.txt");
             Scanner input = new Scanner( System.in );            
            // Read the edges and insert
            int i = 0;
            int nodes = Integer.parseInt(input.nextLine());
            String line;
            String[] edgesADD = new String[nodes];
            while( i < nodes )
            {
                       line = input.nextLine();
                        edgesADD[i] = line;
                       StringTokenizer st = new StringTokenizer( line );
                       String source  = st.nextToken();
                       while (st.hasMoreTokens()) 
                         {      
                             String dest    = st.nextToken();
                             int cost    = Integer.parseInt( st.nextToken() );
                             g.addEdge( source, dest, cost ); 

                       }
                  
               i++;     
              }

              
           //Vertex w = vertexMap.get( destName );   
           int numHospitals = Integer.parseInt(input.nextLine());
           String startName = input.nextLine();
                      // number of victims
           int numVictims = Integer.parseInt(input.nextLine());
                     // System.out.print( "Enter destination node:" );
           String destName = (input.nextLine());
                     
           String victims[] = new String[numVictims];
           String hospitals[] = new String[numHospitals];
           int vCount = 0;
           int hCount = 0; 
           StringTokenizer tkHos = new StringTokenizer( startName ); 
                  while (tkHos.hasMoreTokens()) 
                         {     
                              hospitals[hCount] = tkHos.nextToken();
                               hCount++;


                       }
                
                //  for (String element: hospitals) {
//                    System.out.print(element + " ");
//                   }
            StringTokenizer tkVic = new StringTokenizer( destName ); 
                  while (tkVic.hasMoreTokens()) 
                         {     
                              victims[vCount]    = tkVic.nextToken();
                               vCount++;


                       }
            //    for (String element: victims) {
//                    System.out.println(element + " ");
//                   }
               
               int allVisits = numVictims * numHospitals;
               double weight[] = new double[allVisits];
               
               for (int j = 0; j < numHospitals; j++)
               {
                            g.dijkstra( hospitals[j] );

                           Vertex w = g.vertexMap.get( victims[0] );
                           
                          if( w == null )
                              throw new NoSuchElementException( "Destination vertex not found" );
                          else if( w.dist == INFINITY )
                              System.out.println( destName + " is unreachable" );
                          else
                          {
                              // System.out.print( "(Cost is: " + w.dist + ") " );
                              weight[j] = w.dist;                      
                          }

                     
               }
               
                //  for (double element: weight) {
                 //   System.out.print(element +" ");
                 // }
                    for (int j = 0; j < numVictims; j++ )
                    {
                        System.out.println("victim " + victims[j]);
                        for (int k = 0; k < numHospitals; k++)
                        {
                            System.out.println("hospital " + hospitals[k]);

                            g.roundTrip(hospitals[k], victims[j]);
                            System.out.println();
                        }
                    }
                    //  System.out.println("victim " + victims[0]);
                    //  System.out.println("hospital " + hospitals[0]);
                    //  g.roundTrip(hospitals[0], victims[0]);
                    //  System.out.println();
                    //  double x = g.roundTripCost(hospitals[0], victims[0]);
                     
                    //  System.out.println(x);
                     
                      
                    //    g.isMultipleCost(hospitals[0], victims[0], edgesADD, nodes);
                      

                     
                  }   
}