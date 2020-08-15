/* Zach Barnhart */
/* Towers of Hanoi */

import java.util.*;

public class TowersHanoi {

    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        String format = "%-35s%-15s%-15s%-15s";

        System.out.println("Towers of Hanoi Solver.");
        System.out.print("Input number of disks (max 6): ");
        int n = keyboard.nextInt();
        while (n>6 || n<=0) {
            System.out.print("Not a valid number of disks! Please enter a valid number of disks (max 6): ");
            n = keyboard.nextInt();
        }

        System.out.format(format, " ", "Tower A", "Tower B", "Tower C");
        System.out.println();

        int[] A = new int[n+1];
        for (int i = 0; i<A.length-1; i++) {
            A[i] = i+1;
        }
        int[] B = new int[n+1];
        int[] C = new int[n+1];
        fillzero(B);
        fillzero(C);
        A[n] = n+1;
        B[n] = n+2;
        C[n] = n+3;
        System.out.format(format, "Initial state:", ArraytoString(A), ArraytoString(B), ArraytoString(C));
        System.out.println();

        Hanoistate initial = new Hanoistate(0,A,B,C);

        solveHanoi(n,initial,A,B);

    }

    public static void solveHanoi(int diskstemp, Hanoistate temp, int[] temp1, int[] temp2) {

        int m = diskstemp;
        if (tester(temp1,temp2).equals("AB")) {
            if (m == 1) {
                MoveOne2(temp,temp.getTowerA(),temp.getTowerB());
            } else {
                solveHanoi(m - 1,temp, temp.getTowerA(),temp.getTowerC());
                MoveOne2(temp,temp.getTowerA(),temp.getTowerB());
                solveHanoi(m - 1,temp,temp.getTowerC(),temp.getTowerB());
            }
        }
        if (tester(temp1,temp2).equals("AC")) {
            if (m == 1) {
                MoveOne2(temp,temp.getTowerA(),temp.getTowerC());
            } else {
                solveHanoi(m - 1,temp,temp.getTowerA(),temp.getTowerB());
                MoveOne2(temp,temp.getTowerA(),temp.getTowerC());
                solveHanoi(m - 1,temp,temp.getTowerB(),temp.getTowerC());
            }
        }
        if (tester(temp1,temp2).equals("BC")) {
            if (m == 1) {
                MoveOne2(temp,temp.getTowerB(),temp.getTowerC());
            } else {
                solveHanoi(m - 1,temp,temp.getTowerB(),temp.getTowerA());
                MoveOne2(temp,temp.getTowerB(),temp.getTowerC());
                solveHanoi(m - 1,temp,temp.getTowerA(),temp.getTowerC());
            }
        }
        if (tester(temp1,temp2).equals("BA")) {
            if (m == 1) {
                MoveOne2(temp,temp.getTowerB(),temp.getTowerA());
            } else {
                solveHanoi(m - 1,temp,temp.getTowerB(),temp.getTowerC());
                MoveOne2(temp,temp.getTowerB(),temp.getTowerA());
                solveHanoi(m - 1,temp,temp.getTowerC(),temp.getTowerA());
            }
        }
        if (tester(temp1,temp2).equals("CA")) {
            if (m == 1) {
                MoveOne2(temp,temp.getTowerC(),temp.getTowerA());
            } else {
                solveHanoi(m - 1,temp,temp.getTowerC(),temp.getTowerB());
                MoveOne2(temp,temp.getTowerC(),temp.getTowerA());
                solveHanoi(m - 1,temp,temp.getTowerB(),temp.getTowerA());
            }
        }
        if (tester(temp1,temp2).equals("CB")) {
            if (m == 1) {
                MoveOne2(temp,temp.getTowerC(),temp.getTowerB());
            } else {
                solveHanoi(m - 1,temp,temp.getTowerC(),temp.getTowerA());
                MoveOne2(temp,temp.getTowerC(),temp.getTowerB());
                solveHanoi(m - 1,temp,temp.getTowerA(),temp.getTowerB());
            }
        }

    }

    public static int[] MoveOne(int[] temp1, int[] temp2) {

        temp2[firstnonzerol(temp2)-1] = firstnonzero(temp1);
        temp1[firstnonzerol(temp1)] = 0;
        return temp2;

    }

    public static int firstnonzero(int[] temp) {

        int firstnonzero = 0;
        for(int i = 0; i<temp.length-1; i++) {
            if (temp[i]!=0) {
                firstnonzero = temp[i];
                break;
            }
        }
        return firstnonzero;
    }

    public static int firstnonzerol(int[] temp) {

        int firstnonzerol = 0;
        int[] Z = new int[temp.length-1];
        fillzero(Z);
        int[] test = new int[temp.length-1];
        for(int i = 0; i<test.length; i++) {
            test[i]=temp[i];
        }
        if (Arrays.equals(test,Z)) {
            firstnonzerol = temp.length-1;
        }
        for(int i = 0; i<temp.length-1;i++) {
            if (temp[i]!=0) {
                firstnonzerol = i;
                break;
            }
        }
        return firstnonzerol;

    }


    public static void MoveOne2(Hanoistate temp, int[] temp1, int[] temp2) {

        int disk=0;
        String test = tester(temp1,temp2);
        if (test.equals("AB")) {
            int[] tempA = temp.getTowerA();
            int[] tempB = temp.getTowerB();
            disk = firstnonzero(tempA);
            MoveOne(tempA, tempB);
            temp.setTowerA(tempA);
            temp.setTowerB(tempB);

        }
        if (test.equals("AC")) {
            int[] tempA = temp.getTowerA();
            int[] tempC = temp.getTowerC();
            disk = firstnonzero(tempA);
            MoveOne(tempA, tempC);
            temp.setTowerA(tempA);
            temp.setTowerC(tempC);
        }
        if (test.equals("BC")) {
            int[] tempB = temp.getTowerB();
            int[] tempC = temp.getTowerC();
            disk = firstnonzero(tempB);
            MoveOne(tempB, tempC);
            temp.setTowerB(tempB);
            temp.setTowerC(tempC);
        }
        if (test.equals("BA")) {
            int[] tempB = temp.getTowerB();
            int[] tempA = temp.getTowerA();
            disk = firstnonzero(tempB);
            MoveOne(tempB, tempA);
            temp.setTowerB(tempB);
            temp.setTowerA(tempA);
        }
        if (test.equals("CA")) {
            int[] tempC = temp.getTowerC();
            int[] tempA = temp.getTowerA();
            disk = firstnonzero(tempC);
            MoveOne(tempC, tempA);
            temp.setTowerC(tempC);
            temp.setTowerA(tempA);
        }
        if (test.equals("CB")) {
            int[] tempC = temp.getTowerC();
            int[] tempB = temp.getTowerB();
            disk = firstnonzero(tempC);
            MoveOne(tempC, tempB);
            temp.setTowerC(tempC);
            temp.setTowerB(tempB);
        }

        int step = temp.getStep();
        step++;
        temp.setStep(step);
        String format = "%-35s%-15s%-15s%-15s";
        printRow(format, temp.getStep(),disk,test.charAt(0),test.charAt(1),temp.getTowerA(),temp.getTowerB(),temp.getTowerC());
        System.out.println();
    }

    public static void printRow(String format, int steptemp, int disk, char r, char s, int[] temp1, int[] temp2, int[] temp3) {

        String Move="";
        if (steptemp<10) {
            Move = steptemp + "   (Move disk " + disk + " from " + r + " to " + s + "):";
        }
        if (steptemp >= 10 && steptemp<100) {
            Move = steptemp + "  (Move disk " + disk + " from " + r + " to " + s + "):";
        }
        if (steptemp>=100) {
            Move = steptemp + " (Move disk " + disk + " from " + r + " to " + s + "):";
        }

        System.out.format(format, Move, ArraytoString(temp1), ArraytoString(temp2), ArraytoString(temp3));
        if (steptemp==0) {System.out.println(" ");}
    }

    public static String ArraytoString(int[] temp) {
        String r = "";
        for(int i = 0; i<temp.length-1; i++) {
            if (temp[i]!= 0) {
                r = r + temp[i] + " ";
            }
        }
        return r;
    }

    public static void fillzero(int[] temp) {
        for (int i = 0; i<temp.length; i++) {
            temp[i]=0;
        }

    }

    public static String tester(int[] temp1, int[]temp2) {

        int l = temp1.length-1;
        String s="";
        if (temp1[l] + temp2[l] == 2*l+3) {
            if(temp1[l]<temp2[l]) {
                s = "AB";
            } else {s = "BA";}
        }
        if (temp1[l] + temp2[l] == 2*l+4) {
            if(temp1[l]<temp2[l]) {
                s = "AC";
            } else {s = "CA";}
        }
        if (temp1[l] + temp2[l] == 2*l+5) {
            if(temp1[l]<temp2[l]) {
                s = "BC";
            } else {s = "CB";}
        }
        return s;
    }
}

class Hanoistate {

    private int step;
    private int[] towerA;
    private int[] towerB;
    private int[] towerC;

    public Hanoistate(int step, int[] towerA, int[] towerB, int[] towerC) {
        this.step = step;
        this.towerA = towerA;
        this.towerB = towerB;
        this.towerC = towerC;
    }

    public void setStep(int temp) {
        this.step = temp;
    }

    public int getStep() {
        return this.step;
    }

    public void setTowerA(int[] temp) {
        this.towerA=temp;
    }

    public void setTowerB(int[] temp) {
        this.towerB=temp;
    }

    public void setTowerC(int[] temp) {
        this.towerC=temp;
    }

    public int[] getTowerA() {
        return this.towerA;
    }

    public int[] getTowerB() {
        return this.towerB;
    }

    public int[] getTowerC() {
        return this.towerC;
    }

}
