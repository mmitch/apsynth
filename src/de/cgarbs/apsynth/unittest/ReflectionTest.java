package de.cgarbs.apsynth.unittest;

public class ReflectionTest {

    static void getChildren(Class c, int depth) {
        System.out.println(depth + " <-> " + c.getName());
        Class[] cl = c.getDeclaredClasses();
        for (int i=0; i<cl.length; i++) {
            getChildren(cl[i], depth+1);
        }
    }
    
    public static void main(String[] argv) {
        getChildren(de.cgarbs.apsynth.signal.SignalClass.class, 0);
    }
    
}
