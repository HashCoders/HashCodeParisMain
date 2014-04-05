package com.hashcoders.noexception;

public class FixedDepthTreeExplorationStrategy implements Strategy {
  class Vehicle {
    int i;
    float t;
    Intersection intersection;
    Intersection oldIntersection;
    boolean over = false;
    List<Road> availableRoads = null;
    List<Road> availableNotBrowsedRoads = null;

    Vehicle(int i, float t, Intersection intersection) {
      this.t = t;
      this.i = i;
      this.intersection = intersection;
    }

    void takeRoad(Road road) {
      assert(intersection == road.from);
      t += road.cost;
      oldIntersection = intersection;
      intersection = road.to;
      availableRoads = null;
      availableNotBrowsedRoads = null;
    }

    List<Road> availableRoads(Data d){
      if(availableRoads == null){
        List<Road> av = new List<Road>();
        for(Road r : d.roads)
          if(r.from == intersection)
            av.add(r);
        return av;
      }
      return availableRoads;
    }

    List<Road> availableNotBrowsedRoads(Data d, List<Road> used){
      if(availableNotBrowsedRoads == null){
        List<Road> av = availableRoads(d);
        List<Road> avnb = new List<Road>();
        for(Road r : av){
          if(!used.contains(r)
              avnb.add(r);
        }
        availableNotBrowsedRoads = avnb;
      }
      return availableNotBrowsedRoads;

    }
  }

  Data data;
  List<Intersection> solution;
  int time;
  int depth;
  int currentScore = 0;
  double[] weight_time; //the weight of steps in time
  List<Road> used = new ArrayList<Road>();
  List<Vehicle> vh = new List<Vehicle>();

  Vehicle next() {
    Vehicle best = null;
    for (Vehicle vehicle : vh) {
      if (!vehicle.over)
        if (best == null || vehicle.t < best.t)
          best = vehicle;
    }
    return best;
  }

  FixedDepthTreeExplorationStrategy(Data d, int depth, double decreaseTime){
    this.data = d;
    this.depth = depth;
    this.weight_time = new double[depth];
    double weight = 1;
    for(int i = 0;i < depth; i++){
      this.weight_time[i] = weight;
      weight = weight * decreaseTime;
    }
  }

  //this function rates the current position according to:
  //Number of available roads
  //Number of not browsed roads
  //The current score
  double rate(){
    int availableRoads = 0;
    int notBrowsedRoads = 0;
    for(Vehicle v : vh){
      availableRoads += v.availableRoads(data).size;
      notBrowsedRoads += v.availableNotBrowsedRoads(data, used).size;
    }
    return availableRoads + notBrowsedRoads;
  }

  @Override
  public Solution process(Data d) {
    int numVeh = data.C;
    Solution s = new Solution(numVeh);

    List<Vehicle> vehicles = new ArrayList<Vehicle>();
    for (int i = 0; i < data.C; i++)
    {
      vehicles.add(new Vehicle(i, 0, data.startingIntersection));
      solution.paths.get(i).intersections.add(data.startingIntersection);
    }

    return s;
  }
}
