/* Zach Barnhart
AirportQueue */

/* DIRECTIONS: JUST RUN AND FOLLOW INSTRUCTIONS */

import java.util.Iterator;
import java.util.Random;
import java.io.*;

public class AirportQueue {

    public static void main(String[] args) throws IOException {


        /* Wait between passenger arrivals is best modeled by exponential distribution random variables. This type of
        random variable typically calls its parameter lambda, hence the name of each's parameter constant
        We have also chosen service times to be exponential RVs, but it is possible service times might be better modeled
        by another type of distribution
        Other decisions we have made include a) not including service time in wait time, b) using seconds to keep track
        of time, but reporting time in minutes
         */

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        /* CONSTANTS */
        int CUT_OFF = 2400;//Cut off time for check-out, 40 minutes in this simulation
        /* INPUTS */
        System.out.println("We will simulate a check-in operation at an airport terminal.");
        System.out.print("Please input the total duration of the check in: ");
        int OPEN_TIME = 60 * Integer.parseInt(keyboard.readLine()); //When the queue for the flight opens. Here we convert to seconds
        System.out.print("Please input the average coach passenger arrival rate: ");
        double LAMBDA_CA = 1.0/(60.0*Integer.parseInt(keyboard.readLine()));//parameter for first-class arrival RV. Chosen so that mean arrival time is user input minutes
        System.out.print("Please input the average coach passenger service rate: ");
        double LAMBDA_CS = 1.0/(60.0*Integer.parseInt(keyboard.readLine()));//parameter for coach service time. Chosen so that means service time is user input minutes on average
        System.out.print("Please input the average first-class passenger arrival rate: ");
        double LAMBDA_FCA = 1.0/(60.0*Integer.parseInt(keyboard.readLine()));//parameter for coach arrival RV. Chosen so that mean arrival time is user input minutes
        System.out.print("Please input the average first-class passenger service rate: ");
        double LAMBDA_FCS = 1.0/(60.0*Integer.parseInt(keyboard.readLine()));//parameter for first-class service time. Chosen so that mean service time is user input minutes

        System.out.println();
        System.out.println("SIMULATION BEGINS");
        System.out.println("--------------------");

        RQueue<Integer> fc = new RQueue<>();//first-class queue
        RQueue<Integer> c = new RQueue<>();//coach queue
        int[] fca = new int[255];//array to keep track of customer arrival times. Needed for average waiting time calculation
        int[] ca = new int[255];//array to keep track of customer arrival times. Needed for average waiting time calculation
        int[] fcmax = new int[2];//maximum length of first-class queue. An array so we can keep track of time it occurs.
        int[] cmax = new int[2];//maximum length of coach queue
        fcmax[0] = 0;
        cmax[0] = 0;
        int fcna = OPEN_TIME - getExp(LAMBDA_FCA);//next arrival time for first class customer
        int cna = OPEN_TIME - getExp(LAMBDA_CA);//next arrival time for coach customer
        int[] fcwaitmax = new int[2];//keep track of longest wait time for first class queue
        int[] cwaitmax = new int[2];//keep track of longest wait time for first class queue
        fcwaitmax[0] = 0;
        cwaitmax[0] = 0;
        boolean fcfree = true; //boolean to keep track of busy time
        boolean cfree = true; //boolean to keep track of busy time
        int fcbusyt = 0;
        int cbusyt = 0;
        int fccount = 1;// first class customer count
        int ccount = 1;//coach customer count
        int fcready = 0;//to keep track of when the first-class service desk will be ready again
        int cready = 0;//to keep track of when the coach service desk will be ready again

        for(int i = OPEN_TIME; i > CUT_OFF; i--) {
            if (i%60==0) {//Mark the minutes so the graphical readout is better
                System.out.println((int) i / 60 + " minutes until take off");
            }

            if(i == cready) { //check if coach queue is ready again
                cfree = true; //if so, it is free now
            }
            if(i == fcready) { //check if first class queue is ready again
                fcfree = true; //if so, it is free now
            }
            if (i == fcna) { //if it is time for a first-class customer to arrive
                fc.enqueue(fccount); //put them in the queue
                fca[fccount-1] = i;//mark their arrival time for later
                System.out.println("First class customer " + fccount + " has arrived.");
                fcna = fcna - getExp(LAMBDA_FCA); //get the next first-class arrival time
                fccount++; //add one to passenger number
            }
            if (i == cna) { //if it is time for a new coach customer to arrive
                c.enqueue(ccount); //put them in the queue
                ca[ccount-1] = i;//mark their arrival time for later
                System.out.println("Coach customer " + ccount + " has arrived.");
                cna = cna - getExp(LAMBDA_CA); //get the next coach arrival time
                ccount++; //add one to passenger number
            }
            if (cfree && !c.empty()){ //if coach desk is free and coach queue is not empty
                int custhelp = (int)c.dequeue(); //take customer from coach queue
                int waittime = ca[custhelp - 1] - i;// calculate wait time
                if (waittime > cwaitmax[0]) { //see if max wait time
                    cwaitmax[0] = waittime; //store if is
                    cwaitmax[1] = custhelp;
                }
                ca[custhelp-1] = waittime;//calculate and store how long they waited */
                System.out.println("Coach customer " + custhelp + " has been helped at the coach desk.");
                cfree = false; // queue no longer free
                cready = i - getExp(LAMBDA_CS); //calculate when desk will be ready again
            }
            if (fcfree && !fc.empty()){ //if first-class desk is free and first-class queue is not empty
                int custhelp = (int)fc.dequeue(); //take customer from first-class queue
                int waittime = fca[custhelp - 1] - i; //calculate wait time
                if (waittime > fcwaitmax[0]) { //see if max wait time
                    fcwaitmax[0] = waittime; //store if is
                    fcwaitmax[1] = custhelp;
                }
                fca[custhelp-1] = waittime;//calculate and store how long they waited
                System.out.println("First-class customer " + custhelp + " has been helped at the first-class desk.");
                fcfree = false; // queue no longer free
                fcready = i - getExp(LAMBDA_FCS); //calculate when desk will be ready again
            }
            if (fcfree && !c.empty()) {//if first-class desk is free and first-class queue is not empty
                int custhelp = (int)c.dequeue(); //take customer from coach queue
                int waittime = ca[custhelp - 1] - i; //calculate wait time
                if (waittime > cwaitmax[0]) { //see if max wait time
                    cwaitmax[0] = waittime;
                    cwaitmax[1] = custhelp;
                }
                ca[custhelp] = waittime; //calculate and store how long they waited
                System.out.println("Coach customer " + custhelp + " has been helped at the first-class desk.");
                fcfree = false; // queue no longer free
                fcready = i - getExp(LAMBDA_CS);//instructions say service time is dependent on customers, so we use the coach time here
            }
            if (c.length() > cmax[0]) {//keeping track of max coach queue length
                cmax[0] = c.length();
                cmax[1] = i;
            }
            if (fc.length() > fcmax[0]) {//keeping track of max first-class queue length
                fcmax[0] = fc.length();
                fcmax[1] = i;
            }
            if (!cfree) {
                cbusyt++;
            } //if coach service desk busy, add to busy time
            if (!fcfree) {
                fcbusyt++;
            } //if first-class service desk busy, add to busy time
        }

        int i = CUT_OFF;
        while(!empty(c,fc)) {
            if (i%60==0 && i >= 0) {//Mark the minutes so the graphical readout is better
                System.out.println( i / 60 + " minutes until take off");
            }
            if (i%60==0 && i < 0) {//Mark the minutes so the graphical readout is better
                System.out.println( -i / 60 + " minutes after take off");
            }

            if(i == cready) { //check if coach queue is ready again
                cfree = true; //if so, it is free now
            }
            if(i == fcready) { //check if first class queue is ready again
                fcfree = true; //if so, it is free now
            }
            if (cfree && !c.empty()){ //if coach desk is free and coach queue is not empty
                int custhelp = (int)c.dequeue(); //take customer from coach queue
                int waittime = ca[custhelp - 1] - i;// calculate wait time
                if (waittime > cwaitmax[0]) { //see if max wait time
                    cwaitmax[0] = waittime; //store if is
                    cwaitmax[1] = custhelp;
                }
                ca[custhelp-1] = waittime;//calculate and store how long they waited
                System.out.println("Coach customer " + custhelp + " has been helped at the coach desk.");
                cfree = false; // queue no longer free
                cready = i - getExp(LAMBDA_CS); //calculate when desk will be ready again
            }
            if (fcfree && !fc.empty()){ //if first-class desk is free and first-class queue is not empty
                int custhelp = (int)fc.dequeue(); //take customer from first-class queue
                int waittime = fca[custhelp - 1] - i; //calculate wait time
                if (waittime > fcwaitmax[0]) { //see if max wait time
                    fcwaitmax[0] = waittime; //store if is
                    fcwaitmax[1] = custhelp;
                }
                fca[custhelp-1] = waittime;//calculate and store how long they waited
                System.out.println("First-class customer " + custhelp + " has been helped at the first-class desk.");
                fcfree = false; // queue no longer free
                fcready = i - getExp(LAMBDA_FCS); //calculate when desk will be ready again
            }
            if (fcfree && !c.empty()) {//if first-class desk is free and first-class queue is not empty
                int custhelp = (int)c.dequeue(); //take customer from coach queue
                int waittime = ca[custhelp - 1] - i; //calculate wait time
                if (waittime > cwaitmax[0]) { //see if max wait time
                    cwaitmax[0] = waittime;
                    cwaitmax[1] = custhelp;
                }
                ca[custhelp] = waittime; //calculate and store how long they waited
                System.out.println("Coach customer " + custhelp + " has been helped at the first-class desk.");
                fcfree = false; // queue no longer free
                fcready = i - getExp(LAMBDA_CS);//instructions say service time is dependent on customers, so we use the coach time here
            }
            if (c.length() > cmax[0]) {//keeping track of max coach queue length
                cmax[0] = c.length();
                cmax[1] = i;
            }
            if (fc.length() > fcmax[0]) {//keeping track of max first-class queue length
                fcmax[0] = fc.length();
                fcmax[1] = i;
            }
            if (!cfree) {
                cbusyt++;
            } //if coach service desk busy, add to busy time
            if (!fcfree) {
                fcbusyt++;
            } //if first-class service desk busy, add to busy time
            i--;
        }

        int duration = OPEN_TIME - i;//calculate duration in seconds
        double fcocc = (double)fcbusyt*100.0/(double)duration;//calculate first-class service desk busy rate
        double cocc = (double)cbusyt*100.0/(double)duration;//calculate coach service desk busy rate

        int fcwaitsum = 0;
        for (int j = 0; j < fca.length; j++) {
            fcwaitsum = fcwaitsum + fca[j];
        }
        fccount--;
        double fcavwait = (double)fcwaitsum/(double)fccount;

        int cwaitsum = 0;
        for (int j = 0; j < ca.length; j++) {
            cwaitsum = cwaitsum + ca[j];
        }
        ccount--;
        double cavwait = (double)cwaitsum/(double)ccount;


        duration = duration / 60; //convert duration to minutes

        System.out.println();
        System.out.println("RESULTS OF THE SIMULATION");
        System.out.println("------------------------------");
        System.out.println("The simulation lasted " + duration + " minutes.");
        System.out.println("The maximum first-class queue length was " + fcmax[0] + " people at time t = " + fcmax[1]/60 + " minutes until take-off");
        System.out.println("The maximum coach queue length was " + cmax[0] + " people at time t = " + cmax[1]/60 + " minutes until take-off");
        System.out.println("The first-class desk was busy " + fcocc + "% of the time");
        System.out.println("The coach desk was busy " + cocc + "% of the time");
        System.out.println("The longest waiting time in the first-class queue was first-class customer " + fcwaitmax[1] + ", who waited in line for " + Math.round(fcwaitmax[0]/60.0) + " minutes.");
        System.out.println("The longest waiting time in the coach queue was coach customer " + cwaitmax[1] + ", who waited in line for " + Math.round(cwaitmax[0]/60.0) + " minutes.");
        System.out.println("The average wait time for first-class passengers was " + Math.round(fcavwait/60.0) + " minutes.");
        System.out.println("The average wait time for coach passengers was " + Math.round(cavwait/60.0) + " minutes.");

    }

    public static int getExp(double lambda) {
        Random rand = new Random();
        double temp = Math.log(1-rand.nextDouble())/(-lambda);
        int itemp = (int)temp;
        return itemp + 1;
    }

    public static boolean empty(RQueue a, RQueue b) {
        return a.empty() && b.empty();
    }
}

interface Queue<E> extends Iterable<E> {
    E dequeue();
    void enqueue(E data);
    boolean empty();
}

class RQueue<E> implements Queue<E> { //RQueue stands for reference-based queue

    private class Node<T> {
        private Node(T data) {
            this.data = data;
        }
        private T data;
        private Node<T> next;
    }

    public boolean empty() {
        return head == null;
    }

    public E dequeue() {

        E temp = null; //We want to return the element at the head of the queue. Return null if no elements in queue

        if (head != null) { //If there are elements in queue...

            temp = head.data; //...assign the data value of the head to temp

            if (head == tail) {
                head = tail = null; //if head = tail, this element was last element, so set head and tail to null
            }
            else {
                head = head.next; //otherwise, the next element in the queue is the new head
            }
        }

        return temp;
    }

    public void enqueue(E data) {

        Node<E> temp = new Node<E>(data); //Made new node with data value

        if (head == null) {
            head = tail = temp; //if no elements in queue, new value becomes head and tail
        }
        else {
            tail.next = temp; //otherwise,
            tail = tail.next;
        }
    }

    public int length() {
        int count = 0;
        for (Node<E> curr = head; curr != null; curr = curr.next) {
            count++;
        }
        return count;
    }

    public Iterator<E> iterator() {

        return new Iterator<E>() {
            public boolean hasNext() {
                return curr != null;
            }
            public E next() {
                E temp = curr.data;
                curr = curr.next;
                return temp;
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
            private Node<E> curr = head;
        };

    }

    private Node<E> head;
    private Node<E> tail;
}