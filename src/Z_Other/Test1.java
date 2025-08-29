package Z_Other;
public class Test1 {

    public static int q1() {
        int x, a = 4, b = 5;
        x = a---b++;
        return x;
    }

    public static int q2() {
        int x = 5; int y=7; int z=(x>4)?(y>2)?x-1:x-2:y-2;
//        int x = 5; <?> y = (x > 3)?7:1;
        // остальное понятно
        return z;
    }

    public static int q3() {
//        friendly String str; - не сущ.
//        transient double x = 41; - внутри класса
//        public final static native int metod() - все ок
//        abstract double desk; - неверно
        return 0;
    }

    public static int q4() {
//        package lesson;
//        abstract public class Jumper {
//            ??? int length;
//
//            public Jumper(int i) {
//                length = i;
//            }
//
//            abstract public int jump(int k);
//        }
//        public class Deer extends Jumper {
//            public Deer(int i) {super(i);}
//            public int jump(int k) {return k;}
//
//            public static void main(String[] args) {
//                Deer q = new Deer(5);
//                q.jump(2);
//                System.out.println("jump " + q.length);
//            }                                         внутри класса -> в одном пакета -> пакет и производные классы -> везде
//        } Нескомппилится только при private, поскольку классы одного пакета (private -> <без м.> -> protected -> public)
        return 0;
    }

    public static int q5() {
//        Animal anim = new Animal();
//        Cat cat = new Cat("Coocky");
//        Rat rat = new Rat("Worm");
//        Object o = new Object();
//        a. cat = (Cat) anim;
//        b. rat = (Rat) anim;
//        c. anim = rat;
//        d. cat = (Cat) rat;
//        e. o = rat;                   ошибки компиляции a, b, e
        return 0;
    }

    public static int q6() {
//        byte b = 5; char c='5'; short s=55; int i=555; float f=5.f; boolean bool=true;
//        a. b = (byte) s;                  присваивание норм
//        b. if (f > b);                    сравнивание приводится к float
//        c. i = c;                         char само приводится к int
//        d. s = f;                         нельзя f > s
//        e. f = i;                         int само приводится к float
//        f. float pi = 3.1415;             нельзя d > f
//        g. c = (char) b;                  присваивание норм
//        h. int x=0; bool=(boolean) x;     нельзя, несравнимые типы
//        ошибки компиляции d, f, h
        return 0;
    }

    public static int q7() {
        outer: for (int i = 0; i < 2; i++)
                    for (int j = 0; j < 3; j++) {
                        if (i == j) {continue outer;}
                        System.out.println("i="+i+" j="+j);
                    }
        return 0;
    }

    public static int q8() {
//        piblic class MyException extends Exception{
//            public MyException(String msg) {super(msg);}
//        }
//        1. int a = 10, h = 10, c;
//        2. try {
//            3. if (a*h/2>10) {MyException e = new MyException("E1"); throw e;}
//        4. } catch (Exception e) {System.out.println(e);}
//        5.   catch (MyException e) {System.out.println(e)};
//        6.   finally {System.out.println("123");}
//        7.   c = (int) (a*h/2); System.out.println(c);
        return 0;
    }

    public static int q9() {
//        int[] z = new int[5]; int x = 0; int y = 10;
//        try {
//            System.out.println("z["+y+"]="+z[y]+"y/x"+y/x);
//        } catch (ArrayIndexOutOfBoundsException e) {
//            System.out.println(e);
//        } catch (ArithmeticException e) {
//            System.out.println(e);
//        } catch (Exception e) {
//            System.out.println(e);
//        } finally {
//            System.out.println("x="+x+" y="+y);
//        }
//        System.out.println("Other");
        return 0;
    }

    public static int q10() {
//        public float method(float a, float b) {...};
//        a.  public float method(float a, float b) {...};
//        b.  public int method(int a, int b) {...};
//        c.  public static void method(float a, float b) {...};
//        d.  public double method(int a) {...};
//        e.  public float method(float a) throws Exception {...};
//        f.  public double method(float a, float b) {...};
//        g.  public float method(float a, float b, float c) {...};
//        h.  public int method(int a, int b, int c) {...};
        return 0;
    }

    public static int q11() {
//        class outer {
//            public static int a = 10;
//            public int b = 5;
//
//            static class inner {
//                private int c = 7;
//                public static void inner() {
//                    System.out.println("a="+a);
//                    System.out.println("b="+b);
//                    System.out.println("c="+c);
//                }
//            }
//
//            public void outer() {
//                System.out.println("a="+a);
//                System.out.println("a="+b);
//                System.out.println("a="+c);
//            }             из статического доступ к статическому снаружи, из обычного inner к неизменяемым снаружи
//        }                 убрать коммент и всё видно
        return 0;
    }

    public static int q12() {
        class outer {
            public final int a = 10;
            public void metod(int x, int y) {
                int b = x*y; final int c = x;
                class inner {
                    public void method() {
                        System.out.println("x=" + x);
                        System.out.println("y=" + y);
                        System.out.println("a=" + a);
                        System.out.println("b=" + b);
                        System.out.println("c=" + c);
                    }
                }
                inner i = new inner(); i.method();
            }
        }
        outer o = new outer();
        o.metod(2, 3);
        return 0;               // Все идеально компилит
    }

    public static int q13() {
//        public class EndlessLoop extends Thread{
//            public EndlessLoop() { setPriority(10);}
//            public void run() { while (true); }
//
//            public static void main(String[] args) {
//                EndlessLoop e1 = new EndlessLoop();
//                EndlessLoop e2 = new EndlessLoop();
//                EndlessLoop e3 = new EndlessLoop();
//                e1.start();
//                e2.start();
//                e3.start();
//            }
//        }
        return 0;
    }

    public static int q14() {
//        public class t extends Thread {
//            public void run() {
//                System.out.println("Begin");
//                suspend();
//                resume();
//                System.out.println("End");
//            }
//
//            public static void main(String[] args) {
//                t th = new t();
//                t.start();
//            }
//        }
        return 0;
    }


    public static void print(int x) {System.out.println(x);}

    public static void main(String[] args) {

    }

}
