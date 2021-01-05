package com.company;

import mpi.MPI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    static int processId, noProcesses;
    static ArrayList<ArrayList<Integer>> memory = new ArrayList<>();
    static boolean done = false;
    static LinkedList<Info> notifyList = new LinkedList<Info>();
    static ReentrantLock mutex = new ReentrantLock();

    public static void writeValue(int sourceId, int variable, int value) {
        Info info = new Info();
        info.setSourceId(sourceId);
        info.setVariable(variable);
        info.setValue(value);
        info.setCompare(-1);
        System.out.println("info source id:" + info.getSourceId());
        mutex.lock();
        notifyList.add(info);
        System.out.println("notify");
        notifyList.forEach(System.out::println);
        mutex.unlock();

    }

    public static void printAllValues(int sourceId) {
        System.out.println("current process: " + processId);
        System.out.println("called from:" + sourceId);
        System.out.println(memory);
        for (int i = 1; i <= noProcesses; i++) {
            System.out.println("variable " + i + " is " + memory.get(sourceId).get(i) + "\n");
        }
    }

    public static void compare(int sourceId, int variable, int initValue, int updateValue) {
        Info info = new Info();
        info.setSourceId(sourceId);
        info.setVariable(variable);
        info.setValue(updateValue);
        info.setCompare(initValue);
        mutex.lock();
        notifyList.add(info);
        mutex.unlock();
    }

    public static void broadcastSendChanges(ArrayList<Info> changes) {
        int[] n = new int[1];
        n[0] = changes.size();
        System.out.println("whatever1");
        System.out.println("n of broadcastsend is: " + n[0]);
        MPI.COMM_WORLD.Bcast(n, 0, 1, MPI.INT, 0);
        System.out.println("whatever");
        if (n[0] == 0) {
            System.out.println("changes 0");
            return;
        }
        System.out.println("changes not 0");
        mutex.lock();

        for (Info change : changes) {

            if (change.getCompare() == -1 ||
                    change.getCompare() == memory.get(change.getSourceId()).get(change.getVariable()))
                memory.get(change.getSourceId()).set(change.getVariable(), change.getValue());

            MPI.COMM_WORLD.Bcast(change, 0, 1, MPI.OBJECT, 0);

        }

        mutex.unlock();


    }

    public static void broadcastReceiveChanges() {
        int[] n = new int[1];
        System.out.println("broadcasting receive changes");
        MPI.COMM_WORLD.Bcast(n, 0, 1, MPI.INT, 0);

        if (n[0] == 0)
            return;

        System.out.println("gets past recv with n: " + n[0]);
        mutex.lock();
        for (int i = 0; i < n[0]; i++) {
            Info info = new Info();
            MPI.COMM_WORLD.Bcast(info, 0, 1, MPI.OBJECT, 0);
            if (info.getCompare() == -1 ||
                    info.getCompare() == memory.get(info.getSourceId()).get(info.getVariable()))
                memory.get(info.getSourceId()).set(info.getVariable(), info.getValue());
        }
        mutex.unlock();


    }

    public static ArrayList<Info> getChangesOfProcess(int source, int tag) {
        int[] size = new int[1];
        MPI.COMM_WORLD.Recv(size, 0, 1, MPI.INT, source, tag);

        System.out.println("size in get changes is:" + size[0]);
        if (size[0] == 0) {
            System.out.println("no changes");
            return new ArrayList<>();
        }

        System.out.println("got here");
        ArrayList<Info> changes = new ArrayList<>();
        for (int i = 0; i < size[0]; i++) {
            Info change = new Info();
            MPI.COMM_WORLD.Recv(change, 0, 1, MPI.OBJECT, source, tag);
            changes.add(change);
        }
        System.out.println(changes);
        return changes;

    }

    public static void sendChanges(ArrayList<Info> changes, int tag) {

        System.out.println("sending changes with size:" + changes.size());
        int[] size = new int[1];
        size[0] = changes.size();
        MPI.COMM_WORLD.Send(size, 0, 1, MPI.INT, 0, tag);

        if (size[0] == 0)
            return;

        for (Info change : changes) {
            MPI.COMM_WORLD.Send(change, 0, 1, MPI.OBJECT, 0, tag);
        }

    }

    public static void runMainThread() {

        int tag = 0;
        while (!done) {

            ArrayList<Info> allChangeInfo = new ArrayList<>();
            mutex.lock();
            while (notifyList.size() > 0) {
                allChangeInfo.add(notifyList.pop());
            }
            mutex.unlock();
            System.out.println("MAIN THREAD RUNNING");
            broadcastSendChanges(allChangeInfo);

            for (int i = 1; i < noProcesses; i++) {
                allChangeInfo = getChangesOfProcess(i, tag);
                System.out.println(allChangeInfo.size());
                if (allChangeInfo.size() > 0) {
                    mutex.lock();
                    for (Info info : allChangeInfo) notifyList.push(info);
                    mutex.unlock();
                }
            }
            tag++;
        }
    }


    public static void runWorkerThread() {
        System.out.println("worker thread running");
        int tag = 0;
        while (!done) {
            System.out.println("here");
            broadcastReceiveChanges();
            System.out.println("gets past b receive");
            ArrayList<Info> allChangeInfo = new ArrayList<>();
            mutex.lock();
            while (notifyList.size() > 0) {
                allChangeInfo.add(notifyList.pop());
            }
            mutex.unlock();
            sendChanges(allChangeInfo, tag);
            tag++;
        }
    }


    public static void runProcess0() throws InterruptedException {

        System.out.println("process 0 is running");
        writeValue(1, 2, 100);
        Thread.sleep(10000);
        printAllValues(1);
        compare(1, 2, 100, 101);
        Thread.sleep(10000);
        printAllValues(1);

    }

    public static void runProcess1() throws InterruptedException {

        System.out.println("process 1 is running");
        Thread.sleep(10000);
        writeValue(1, 3, 200);
        Thread.sleep(10000);
        printAllValues(1);
        Thread.sleep(10000);
        compare(1, 3, 200, 202);
        Thread.sleep(10000);
        printAllValues(1);

    }

    public static void runProcess2() throws InterruptedException {

        System.out.println("process 2 is running");
        Thread.sleep(30000);
        writeValue(1, 2, 200);
        Thread.sleep(20000);
        printAllValues(1);
        Thread.sleep(20000);
        compare(1, 2, 300, 303);
        Thread.sleep(20000);
        printAllValues(1);

    }

    public static void main(String[] args) throws InterruptedException {
        MPI.Init(args);
        processId = MPI.COMM_WORLD.Rank();
        noProcesses = MPI.COMM_WORLD.Size();
        System.out.println(processId);

        if (processId == 0) {

            Thread main = new Thread(Main::runMainThread);
            main.start();
            Thread.sleep(2000);
            runProcess0();
            Thread.sleep(200000);
            System.out.println("main done");
            done=true;
            main.join();

        } else {
            Thread.sleep(20000);
            Thread worker = new Thread(Main::runWorkerThread);
            worker.start();
            switch (processId) {
                case 1:
                    runProcess1();
                    break;
                case 2:
                    runProcess2();
                    break;
                default:
                    break;
            }


            Thread.sleep(200000);
            System.out.println("Worker " + processId + " finished");
            done = true;
            worker.join();

        }

        MPI.Finalize();
    }
}
