package cse.opa.and;

import java.util.Date;
import java.util.Iterator;

public class PhisicEngine {

    private final int DELAY = 80;
    private final boolean gravityRising = false;
    private int crossedEdges = -1;
    private boolean crossEdgeResolved = false;
    private boolean firstStep = true;
    private double orgGravity = 0;
    private double orgHook = 0;
    private double lastEnergyChange = 0;

    private int step = 0;
    private int minFrontierX = 20;
    private int minFrontierY = 20;
    private int maxFrontierX = 1500;
    private int maxFrontierY = 1500;

    private double minChange = 0.000001;

    private double SPRING_MINIMAL_LENGTH = 20;

    private double kinetic = 0;

    private double getGravityMultiplayer(){
		return AlgorithmParams.getGravityConst();
	}
	
	private double getHookConst(){
		return AlgorithmParams.getHookConst();
	}
	
	private double getDamping(){
		return AlgorithmParams.getDampingConst();
	}

    private double getSpringMinimalLen() {
        return SPRING_MINIMAL_LENGTH;
    }

    public void initCoords() {
        int vCount = AlgorithmParams.getNodes().size();
        double R = (3.0 * vCount * Node.VERTEX_SIZE) / (2.0 * Math.PI);
        double alfa = 0;
        int num = 0;
        for (Iterator<Node> iter = AlgorithmParams.getNodes().iterator(); iter.hasNext();) {
            Node v = (Node) iter.next();
            alfa = (2.0 * Math.PI / vCount) * (double) num;
            int sgnX = 1;
            int sgnY = -1;
            double x = sgnX * R * Math.cos(alfa) + R + Node.VERTEX_SIZE;
            double y = sgnY * R * Math.sin(alfa) + R + Node.VERTEX_SIZE;

            v.setX((int) x);
            v.setY((int) y);
            num++;
        }
    }

    private void recalcVectors() {
        kinetic = 0;
        for (Iterator<Node> iter = AlgorithmParams.getNodes().iterator(); iter.hasNext();) {
            Node base = (Node) iter.next();
            double sumGravityFx = 0;
            double sumGravityFy = 0;

            for (Iterator<Node> iter2 = AlgorithmParams.getNodes().iterator(); iter2.hasNext();) {
                Node other = (Node) iter2.next();
                if (other == base) {
                    continue;
                }
                double dist = Math.sqrt(Math.pow(base.getX() - other.getX(), 2) + Math.pow(base.getY() - other.getY(), 2));
                if (dist < 2 * Node.VERTEX_SIZE) {
                    dist = 2 * Node.VERTEX_SIZE;
                }
                if (dist == 0) {
                    System.out.println("Distance is 0 !!!");
                    continue;
                }
                double gravityF = (double) 1 / Math.pow(dist, 2) * getGravityMultiplayer();

                kinetic += Math.abs(gravityF);

                double gravityFx = gravityF * (-other.getX() + base.getX()) / dist;
                double gravityFy = gravityF * (-other.getY() + base.getY()) / dist;

                sumGravityFx += gravityFx;
                sumGravityFy += gravityFy;

            }

            double sumHookeFx = 0;
            double sumHookeFy = 0;

            for (Iterator<Edge> iter2 = base.getEdges().iterator(); iter2.hasNext();) {
                Edge e = (Edge) iter2.next();
                Node other = null;
                if (e.getA() == base) {
                    other = e.getB();
                } else if (e.getB() == base) {
                    other = e.getA();
                } else {
                    System.out.println("Error - corrupted edge !!!");
                    continue;
                }

                if (other == base) {
                    System.out.println("Error - base and other are the same vertex !!!");
                    continue;
                }

                double dist = Math.sqrt(Math.pow(base.getX() - other.getX(), 2) + Math.pow(base.getY() - other.getY(), 2));
                if (dist == 0) {
                    System.out.println("Distance is 0 !!!");
                    continue;
                }

                double hookeF = -(double) getHookConst() * (dist - getSpringMinimalLen());

                kinetic += Math.abs(hookeF);

                double hookeFx = hookeF * (-other.getX() + base.getX()) / dist;
                double hookeFy = hookeF * (-other.getY() + base.getY()) / dist;

                sumHookeFx += hookeFx;
                sumHookeFy += hookeFy;
            }
            if ((base.getX() < minFrontierX || base.getX() > maxFrontierX)
                    && (base.getY() < minFrontierY || base.getY() > maxFrontierY)) {
                base.setDx((base.getDx() + sumGravityFx + sumHookeFx) * Math.pow(getDamping(), 12));
                base.setDy((base.getDy() + sumGravityFy + sumHookeFy) * Math.pow(getDamping(), 12));
            } else if (base.getX() < minFrontierX || base.getX() > maxFrontierX) {
                base.setDx((base.getDx() + sumGravityFx + sumHookeFx) * Math.pow(getDamping(), 12));
                base.setDy((base.getDy() + sumGravityFy + sumHookeFy) * getDamping());
            } else if (base.getY() < minFrontierY || base.getY() > maxFrontierY) {
                base.setDx((base.getDx() + sumGravityFx + sumHookeFx) * getDamping());
                base.setDy((base.getDy() + sumGravityFy + sumHookeFy) * Math.pow(getDamping(), 12));
            } else {
                base.setDx((base.getDx() + sumGravityFx + sumHookeFx) * getDamping());
                base.setDy((base.getDy() + sumGravityFy + sumHookeFy) * getDamping());
            }

        }

    }

    private void recalcCoords() {
        for (Iterator<Node> iter = AlgorithmParams.getNodes().iterator(); iter.hasNext();) {
            Node v = (Node) iter.next();
            v.setX(v.getX() + v.getDx());
            v.setY(v.getY() + v.getDy());
        }
    }

    private void runAlgorithmStep() {
        recalcVectors();
        recalcCoords();
        if (step > 0 && firstStep && lastEnergyChange < minChange * 3) {
            orgGravity = AlgorithmParams.getGravityConst();
            orgHook = AlgorithmParams.getHookConst();
            firstStep = false;
        }

        if (!firstStep && !crossEdgeResolved && step % 30 == 0) {
            int c = getCrossedEdgesCount();
            double gravity=AlgorithmParams.getGravityConst();
            if (c > 0 && (crossedEdges == c || crossedEdges == 1)) {
                gravity += orgGravity * 0.3;
                AlgorithmParams.setGravityConst(gravity);
            }
            if (crossedEdges > c || gravity==AlgorithmParams.MAX_GRAVITY) {
                crossEdgeResolved = true;
                AlgorithmParams.setGravityConst(orgGravity);
            }
            crossedEdges = c;
        }
        step = step + 1;
    }

    private long runAlgorithm() {
        kinetic = 0;
        double oldKinetic = 0;

        Date start = new Date();
        while (kinetic == 0 || lastEnergyChange > minChange) {
            if (kinetic != 0) {
                oldKinetic = kinetic;
            } else {
                oldKinetic = Integer.MAX_VALUE;
            }

            runAlgorithmStep();
            lastEnergyChange = Math.abs((double) (oldKinetic - kinetic)) / (double) kinetic;
        }
        long runTime = (new Date()).getTime() - start.getTime();

        return runTime;
    }

    private double distance(double x1, double y1, double x2, double y2){
	    return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
    }

    private boolean linesIntersect(double x1, double y1, double x2, double y2, double x3,
                                         double y3, double x4, double y4) {
        x2 -= x1; // A
        y2 -= y1;
        x3 -= x1; // B
        y3 -= y1;
        x4 -= x1; // C
        y4 -= y1;
        double AvB = x2 * y3 - x3 * y2;
        double AvC = x2 * y4 - x4 * y2;
        // Online
        if (AvB == 0.0 && AvC == 0.0) {
            if (x2 != 0.0) {
                return (x4 * x3 <= 0.0)
                        || ((x3 * x2 >= 0.0) && (x2 > 0.0 ? x3 <= x2 || x4 <= x2 : x3 >= x2
                        || x4 >= x2));
            }
            if (y2 != 0.0) {
                return (y4 * y3 <= 0.0)
                        || ((y3 * y2 >= 0.0) && (y2 > 0.0 ? y3 <= y2 || y4 <= y2 : y3 >= y2
                        || y4 >= y2));
            }
            return false;
        }
        double BvC = x3 * y4 - x4 * y3;
        return (AvB * AvC <= 0.0) && (BvC * (AvB + BvC - AvC) <= 0.0);
    }

    public int getCrossedEdgesCount() {
        int crossedEdgesCount = 0;
        for (Iterator iter = AlgorithmParams.getEdges().iterator(); iter.hasNext();) {
            Edge basic = (Edge) iter.next();
            for (Iterator iter2 = AlgorithmParams.getEdges().iterator(); iter2.hasNext();) {
                Edge guest = (Edge) iter2.next();
                if (basic != guest) {
                    if (linesIntersect(basic.getA().getX(), basic.getA().getY(),
                            basic.getB().getX(), basic.getB().getY(),
                            guest.getA().getX(), guest.getA().getY(),
                            guest.getB().getX(), guest.getB().getY())) {
                        if (distance(basic.getA().getX(), basic.getA().getY(),
                                guest.getA().getX(), guest.getA().getY()) != 0) {
                            if (distance(basic.getA().getX(), basic.getA().getY(),
                                    guest.getB().getX(), guest.getB().getY()) != 0) {
                                if (distance(basic.getB().getX(), basic.getB().getY(),
                                        guest.getB().getX(), guest.getB().getY()) != 0) {
                                    if (distance(basic.getB().getX(), basic.getB().getY(),
                                            guest.getA().getX(), guest.getA().getY()) != 0) {
                                        crossedEdgesCount++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return crossedEdgesCount / 2;
    }


    public PhisicEngine() {
        initCoords();
        runAlgorithm();
    }
    
}
