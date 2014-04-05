import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * To change this template use File | Settings | File Templates.
 */


class Street{
    int from;
    int to;
    int length;
    int cost;

    boolean bidirectional;
    Street(int f, int t, int l, int c, boolean bd){
        from=f;to=t;length=l;cost=c;bidirectional=bd;
    }
    Street(){}
}

 class Car{
     int position;
     ArrayList<Integer> path=new ArrayList<Integer>();
     Car(int p){
         position=p;
         path.add(p);
     }

     public void changePosition(int np){
         position=np;
         path.add(np);
     }
 }


class Junction{
    float x;
    float y;
    Junction(float _x, float _y) {
        x=_x;
        y=_y;
    }
    public boolean equals(Object o){
        if (o instanceof Junction){
             return x==((Junction) o).x && y==((Junction) o).y;
        }
        return false;
    }
}



public class StreetView {
    public static List<Junction> getIngoingStreets(Junction j){
        return null;
    }


    public static void proceedOutput(Car[] cars) throws Exception{
        BufferedWriter br=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/Michael/Documents/11OUTPUT.TXT")));
        br.write(cars.length+"\n");
        for (Car c: cars){
            br.write(c.path.size()+"\n");
            for (int p: c.path){
                br.write(p+"\n");
            }

        }
        br.close();
    }

    public static void PlainAlg(Car[] cars, int NJunctions, int NStreets, int NTime, int StartJunctionNumber, ArrayList<ArrayList<Street>> streets){
        Set<Street> visited=new HashSet<Street>();



//
//        int time=0;
//        int iter=0;


        for (Car c : cars) {
            int time = 0;
            int iter = 0;

            while (time<NTime && iter<10000) {
                int maxlength = 0;
                Street maxstreet = null;

                for (Street s : streets.get(c.position)) {
                    if (visited.contains(s)) continue;

                    if (s.length > maxlength && time + s.cost < NTime) {
                        maxlength = s.length;
                        maxstreet = s;

                    }

                }
                if (maxstreet != null) {
                    c.changePosition(maxstreet.to);

                    visited.add(maxstreet);
                    time += maxstreet.cost;
                    iter++;

                }          else {
                    break;
                }

            }

        }


    }

    public static void RandAlg(Car[] cars, int NJunctions, int NStreets, int NTime, int StartJunctionNumber, ArrayList<ArrayList<Street>> streets){
        Set<Street> visited=new HashSet<Street>();



//
//        int time=0;
//        int iter=0;


        for (Car c : cars) {
            int time = 0;
            int iter = 0;

            while (time<NTime && iter<10000) {
                int maxlength = 0;
                Street maxstreet = null;
                ArrayList<Street> connectedStreets=streets.get(c.position);

                int csize=connectedStreets.size();
                Random r=new Random();
                int nextr=0;
                int cnt=0;
                do {
                    nextr = r.nextInt(csize);
                    cnt++;
                } while ((visited.contains(connectedStreets.get(nextr)) || time+connectedStreets.get(nextr).cost>NTime)&&(cnt<10000));


                Street ns=connectedStreets.get(nextr);
                c.changePosition(ns.to);
                time+=ns.cost;
                iter++;









            }

        }


    }


    public static void IterAlg(Car[] cars, int NJunctions, int NStreets, int NTime, int StartJunctionNumber, ArrayList<ArrayList<Street>> streets){
        Set<Street> s=new HashSet<Street>();




        for (Car c : cars) {
            int time = 0;
            int iter = 0;

            while (time<NTime && iter<10000) {
                int maxlength = 0;
                Street maxstreet = null;
                ArrayList<Street> connectedStreets=streets.get(c.position);

                int csize=connectedStreets.size();
                Random r=new Random();
                int nextr=0;
                int cnt=0;
                do {
                    nextr = r.nextInt(csize);
                    cnt++;
                } while ((visited.contains(connectedStreets.get(nextr)) || time+connectedStreets.get(nextr).cost>NTime)&&(cnt<10000));


                Street ns=connectedStreets.get(nextr);
                c.changePosition(ns.to);
                time+=ns.cost;
                iter++;









            }

        }





    }

    public static void main(String[] args) throws Exception {

        int NJunctions=0;
        int NStreets=0;
        int NTime=0;
        int NCars=0;
        int StartJunctionNumber=0;

        InputStream fis=new FileInputStream("/Users/Michael/Documents/00INPUT.txt");
        Scanner sc=new Scanner(fis);

        NJunctions=sc.nextInt();
        NStreets=sc.nextInt();
        NTime=sc.nextInt();
        NCars=sc.nextInt();
        StartJunctionNumber=sc.nextInt();

        Junction[] Junctions=new Junction[NJunctions];

        for (int i=0; i<NJunctions; ++i){
            float x=sc.nextFloat();
            float y=sc.nextFloat();
            Junctions[i]=new Junction(x, y);

        }

        //ArrayList[] streets=new ArrayList[NJunctions];
        ArrayList<ArrayList<Street>> streets=new ArrayList<ArrayList<Street>>();
        for (int i=0; i<NJunctions; ++i){
            streets.add(new ArrayList<Street>());

        }



        for (int i=0; i<NStreets; ++i){

            int from=sc.nextInt();
            int to=sc.nextInt();
            int direction=sc.nextInt();
            int cost=sc.nextInt();
            int length=sc.nextInt();
            Street s=new Street(from, to, cost, length, direction==2);

            if (direction==1){
                streets.get(from).add(s);


            } else {
                Street s2=new Street(to, from, cost, length, direction==2);
                streets.get(from).add(s);
                streets.get(to).add(s2);

            }

        }

        Car[] cars=new Car[NCars];
        for (int i=0; i<NCars; ++i){
            cars[i]=new Car(StartJunctionNumber);
        }


       // PlainAlg(cars,NJunctions,NStreets, NTime, StartJunctionNumber, streets);
       RandAlg(cars,NJunctions,NStreets, NTime, StartJunctionNumber, streets);


        proceedOutput(cars);


    }
}
