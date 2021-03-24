/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package J1;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

/**
 *
 * @author DELL
 */
public class LineArrow {

    private static final Polygon ARROW_HEAD = new Polygon();

    static {
        ARROW_HEAD.addPoint(0, 0);
        ARROW_HEAD.addPoint(-5, -10);
        ARROW_HEAD.addPoint(5, -10);
    }
    
    private final int x;
    private final int y;
    private final int endX;
    private final int endY;
    private final int thickness;

    public LineArrow(int x, int y, int x2, int y2, int thickness) {
        this.x = x;
        this.y = y;
        this.endX = x2;
        this.endY = y2;
        this.thickness = thickness;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // 
        double angle = Math.atan2(endY - y, endX - x);

        g2.setStroke(new BasicStroke(thickness));

        // 
        g2.drawLine(x, y, (int) (endX - 10 * Math.cos(angle)), (int) (endY - 10 * Math.sin(angle)));

        // 
        AffineTransform tx1 = g2.getTransform();

        // 
        AffineTransform tx2 = (AffineTransform) tx1.clone();

        // 
        tx2.translate(endX, endY);
        tx2.rotate(angle - Math.PI / 2);

        // 
        g2.setTransform(tx2);
        g2.fill(ARROW_HEAD);

        // 
        g2.setTransform(tx1);
    }
}
