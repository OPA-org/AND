package cse.opa.and;

import java.util.ArrayList;

public class AlgorithmParams {

    private static ArrayList<Node> vertexes = new ArrayList<>();
    private static ArrayList<Edge> edges = new ArrayList<>();

    private static double GRAVITY_const = 200000.0;
    private static double HOOKE_K_const = 0.7;
    private static double DAMPING_const = 0.1;

    public static int MAX_GRAVITY = 1000000;
    public static int MAX_HOOKE_K = 1500;
    public static int MAX_DAMPING = 1000;

    public AlgorithmParams() {
    }

    public static void getInstance() {
        AlgorithmParams s = new AlgorithmParams();
    }

    public static double getGravityConst() {
        return GRAVITY_const;
    }

    public static double getHookConst() {
        return HOOKE_K_const;
    }

    public static double getDampingConst() {
        return DAMPING_const;
    }

    public static void setGravityConst(double val) {
        val = Math.min(val, MAX_GRAVITY);
        GRAVITY_const = val;
    }

    public static void setHookConst(double val) {
        val = Math.min(val, MAX_HOOKE_K);
        HOOKE_K_const = val;
    }

    public static void setDampingConst(double val) {
        val = Math.min(val, MAX_DAMPING);
        DAMPING_const = val;
    }

    public static ArrayList<Node> getNodes() {
        return vertexes;
    }

    public static void setNodes(ArrayList<Node> vertexes) {
        AlgorithmParams.vertexes = vertexes;
    }

    public static ArrayList<Edge> getEdges() {
        return edges;
    }

    public static void setEdges(ArrayList<Edge> edges) {
        AlgorithmParams.edges = edges;
    }

}
