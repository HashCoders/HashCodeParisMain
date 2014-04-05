package com.hashcoders.noexception;

import java.util.ArrayList;
import java.util.List;

public class HeuristicByVehicle implements Strategy {
  Data data;
  List<Intersection> solution;
  int time;
  int maxDepth;
  List<Road> used;

  Heuristic(Data d, int dep) {
    data = d;
    used = new ArrayList<Road>();
    maxDepth = dep;
  }

  int randomInt(int min, int max) {
    return (int) (min + Math.random() * (max - min + 1));
  }

  public List<Road> notUsed(List<Road> av) {
    List<Road> res = new ArrayList<Road>();
    for (Road r : av) {
      if (!used.contains(r))
        res.add(r);
    }

    return res;
  }

  public int lengthUnused(List<Intersection> path, Intersection i, int depth,
      List<Road> potUsed) {
    int res = 0;
    if (depth == 0) {
      return res;
    }
    for (Road pot : i.outgoing) {
      if (!path.contains(pot) && !used.contains(pot)
          && !potUsed.contains(pot)) {
        potUsed.add(pot);
        res = pot.loot.score
          + Math.max(res,
              lengthUnused(path, pot.to, depth - 1, potUsed));
      } else {
        res += Math.max(res,
            lengthUnused(path, pot.to, depth - 1, potUsed));
      }
    }
    return res;
  }

  public Intersection nextIntersection(Intersection from,
      List<Intersection> path) {
    List<Road> available = from.outgoing;
    List<Road> availableNotUsed = notUsed(available);

    if (!available.isEmpty()) {
      if (!availableNotUsed.isEmpty()) {
        int bestLength = -1;
        Road bestRoad = null;
        for (Road next : availableNotUsed) {
          int cost = next.cost;
          if (cost <= time) {
            int score = lengthUnused(path, from, maxDepth,
                new ArrayList<Road>());
            if (score > bestLength) {
              bestRoad = next;
              bestLength = score;
            }
          }
        }
        if (bestLength != -1) {
          used.add(bestRoad);
          time -= bestRoad.cost;
          Intersection to = bestRoad.to;
          path.add(to);
          return to;
        } else {
          bestLength = -1;
          bestRoad = null;
          for (Road next : available) {
            int cost = next.cost;
            if (cost <= time) {
              int score = lengthUnused(path, from, maxDepth,
                  new ArrayList<Road>());
              if (score > bestLength) {
                bestRoad = next;
                bestLength = score;
              }
            }
          }
          if (bestLength != -1) {
            used.add(bestRoad);
            time -= bestRoad.cost;
            Intersection to = bestRoad.to;
            path.add(to);
            return to;
          } else {
            time = 0;
            return from;
          }
        }
      } else {
        int bestLength = -1;
        Road bestRoad = null;
        for (Road next : available) {
          int cost = next.cost;
          if (cost <= time) {
            int score = lengthUnused(path, from, maxDepth,
                new ArrayList<Road>());
            if (score > bestLength) {
              bestRoad = next;
              bestLength = score;
            }
          }
        }
        if (bestLength != -1) {
          used.add(bestRoad);
          time -= bestRoad.cost;
          Intersection to = bestRoad.to;
          path.add(to);
          return to;
        } else {
          time = 0;
          return from;
        }
      }
    }
    time = 0;
    return from;
  }

  public List<Road> createPath(Intersection start, List<Intersection> path) {
    List<Road> usedHere = new ArrayList<Road>();
    Intersection temp = start;
    Intersection temp2 = temp;
    path.add(start);
    while (time != 0) {
      temp2 = temp;
      temp = nextIntersection(temp, path);
      if (temp.equals(temp2))
        return new ArrayList<Road>();
    }
    return usedHere;
  }

  public Solution process(Data d) {
    int numVeh = data.C;
    Solution s = new Solution(numVeh);
    for (int i = 0; i < numVeh; i++) {
      time = d.maxT;
      createPath(data.startingIntersection, s.paths.get(i).intersections);
    }
    return s;
  }
}
