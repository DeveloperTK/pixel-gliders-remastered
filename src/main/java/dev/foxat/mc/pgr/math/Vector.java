package dev.foxat.mc.pgr.math;

public class Vector {

    private final double[] coords;

    /**
     * Create vector in R^n
     *
     * @param coords vector coordinates
     */
    public Vector(double... coords) {
        this.coords = coords;
    }

    /**
     * @param index coordinate index
     * @return coordinate value at index
     */
    public double get(int index) {
        return this.coords[index];
    }

    /**
     * @param index index to change value at
     * @param value new value
     * @return old value at given index
     */
    public double set(int index, double value) {
        double oldValue = get(index);
        this.coords[index] = value;
        return oldValue;
    }

    /**
     * @return number of vector coordinates
     */
    public double dimension() {
        return this.coords.length;
    }

    /**
     * @return euclidean vector length
     */
    public double length() {
        double sumOfSquares = 0.0;
        for (double coord : coords) {
            sumOfSquares += Math.pow(coord, 2);
        }

        return Math.sqrt(sumOfSquares);
    }

    /**
     * @return normalized copy of vector
     */
    public Vector normalize() {
        int dimension = this.coords.length;
        double length = length();

        double[] newCoords = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            newCoords[i] = coords[i] / length;
        }

        return new Vector(newCoords);
    }

    /**
     * @param scalar parameter to scale vector with
     * @return scaled vector
     */
    public Vector scale(double scalar) {
        int dimension = this.coords.length;

        double[] newCoords = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            newCoords[i] = coords[i] * scalar;
        }

        return new Vector(newCoords);
    }

    public Vector plus(Vector other) {
        if (other.dimension() != this.dimension()) {
            throw new IllegalArgumentException("Second vector is of different dimension!");
        }

        int dimension = this.coords.length;
        double[] newCoords = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            newCoords[i] = this.get(i) + other.get(i);
        }

        return new Vector(newCoords);
    }

    /**
     * @param other second vector to build dot product with
     * @return dot product of both vectors
     */
    public double dot(Vector other) {
        if (other.dimension() != this.dimension()) {
            throw new IllegalArgumentException("Second vector is of different dimension!");
        }

        double result = 0.0d;
        for (int i = 0; i < dimension(); i++) {
            result += this.get(i) * other.get(i);
        }

        return result;
    }

}
