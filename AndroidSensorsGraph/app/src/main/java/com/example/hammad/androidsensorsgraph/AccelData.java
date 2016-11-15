package com.example.hammad.myapplication;

/**
 * Created by hammad on 10/23/16.
 */

public class AccelData {
    private long timestamp;
    private double x;
    private double y;
    private double z;
    private double xG,yG,zG;
    double xM,yM,zM;

    public AccelData(long timestamp, double x, double y, double z,double xG, double yG,double zG,double xM, double yM,double zM ) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xG = xG;
        this.yG = yG;
        this.zG = zG;
        this.xM = xM;
        this.yM = yM;
        this.zM = zM;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getZ() {
        return z;
    }
    public void setZ(double z) {
        this.z = z;
    }
    public double getxG() {
        return xG;
    }
    public void setxG(double xG) {
        this.xG = xG;
    }


    public double getyG() {
        return yG;
    }
    public void setyG(double yG) {
        this.yG = yG;
    }

    public double getzG() {
        return zG;
    }
    public void setzG(double zG) {
        this.zG = zG;
    }
    public double getxM() {
        return xM;
    }
    public void setxN(double xM) {
        this.xM = xM;
    }
    public double getyM() {
        return yM;
    }

    public void setxyM(double yM) {
        this.xM = yM;
    }

    public double getzM() {
        return zM;
    }
    public void setzM(double zM) {
        this.zM = zM;
    }


    public String toString()
    {
        return "t="+timestamp+", x="+x+", y="+y+", z="+z;
    }
}