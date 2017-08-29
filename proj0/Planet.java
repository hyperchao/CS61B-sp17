
public class Planet {
    
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    
    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }
    
    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }
    
    public double calcDistance(Planet p) {
        return Math.sqrt((xxPos - p.xxPos)*(xxPos - p.xxPos) + (yyPos - p.yyPos)*(yyPos - p.yyPos));
    }
    
    public double calcForceExertedBy(Planet p) {
        return 6.67e-11 * mass * p.mass / Math.pow(calcDistance(p), 2);
    }
    
    public double calcForceExertedByX(Planet p) {
        return calcForceExertedBy(p) * (p.xxPos - xxPos) / calcDistance(p);
    }
    
    public double calcForceExertedByY(Planet p) {
        return calcForceExertedBy(p) * (p.yyPos - yyPos) / calcDistance(p);
    }
    
    public double calcNetForceExertedByX(Planet[] a) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            if (!this.equals(a[i])) {
                sum += calcForceExertedByX(a[i]);
            }
        }
        return sum;
    }
    
    public double calcNetForceExertedByY(Planet[] a) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            if (!this.equals(a[i])) {
                sum += calcForceExertedByY(a[i]);
            }
        }
        return sum;
    }
    
    public void update(double dt, double fX, double fY) {
        double aX = fX/mass, aY = fY/mass;
        xxVel += aX*dt;
        yyVel += aY*dt;
        xxPos += xxVel*dt;
        yyPos += yyVel*dt;
    }
    
    public void draw() {
        String imgPath = "./images/" + imgFileName;
        StdDraw.picture(xxPos, yyPos, imgPath);
    }
}
    