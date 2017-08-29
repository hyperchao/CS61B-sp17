public class NBody {
    
    public static String starFieldImg = "./images/starfield.jpg";
    
    public static String backgroundMusic = "./audio/2001.mid";
    
    public static double readRadius(String filePath) {
        In in = new In(filePath);
        in.readInt();
        return in.readDouble();
    }
    
    public static Planet[] readPlanets(String filePath) {
        
        In in = new In(filePath);
        int num = in.readInt();
        Planet[] planets = new Planet[num];
        
        in.readDouble();
        
        for (int i = 0; i < num; i++) {
            double xPos = in.readDouble();
            double yPos = in.readDouble();
            double xVel = in.readDouble();
            double yVel = in.readDouble();
            double mass = in.readDouble();
            String img  = in.readString();
            planets[i] = new Planet(xPos, yPos, xVel, yVel, mass, img);
        }
        
        return planets;
    }
    
    public static void main(String[] args) {
        
        double T = Double.parseDouble(args[0]), dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double universeRadius = readRadius(filename);
        Planet[] planets = readPlanets(filename);
        
        StdDraw.setScale(-universeRadius, universeRadius);
        
        for (double time = 0; time < T; time += dt) {
            int num = planets.length;
            double[] xForces = new double[num];
            double[] yForces = new double[num];
            for (int i = 0; i < num; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for (int i = 0; i < num; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }
            
            StdDraw.clear();
            StdDraw.picture(0, 0, starFieldImg, 2*universeRadius, 2*universeRadius);
            for (Planet p : planets) {
                p.draw();
            }
            StdDraw.show(10);
        }
        
        System.out.println(planets.length);
        System.out.println(universeRadius);
        for (int i = 0; i < planets.length; i++) {
            System.out.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                       planets[i].xxPos, planets[i].yyPos, planets[i].xxVel, planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}
