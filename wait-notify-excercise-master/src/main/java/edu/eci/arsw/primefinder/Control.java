/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.Scanner;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];

    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];

        int i;

        Object lock = new Object();

        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, lock);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1, lock);
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }

        Scanner scanner = new Scanner(System.in);

        while (anyAlive()) {
            try {
                Thread.sleep(TMILISECONDS);
            } catch (InterruptedException threadProblem) {
                Thread.currentThread().interrupt();
            }

            for (int i = 0; i < NTHREADS; i++) {
                pft[i].pause();
            }

            int total = 0;
            for (int i = 0; i < NTHREADS; i++) {
                total += pft[i].getPrimes().size();
            }
            System.out.println("Primos encontrados hasta ahora: " + total);
            System.out.println("Presiona ENTER para continuar");

            scanner.nextLine();

            for (int i = 0; i < NTHREADS; i++) {
                pft[i].resumeExecution();
            }
        }

        scanner.close();
    }

    private boolean anyAlive() {
        for (int i = 0; i < NTHREADS; i++) {
            if (pft[i].isAlive()) {
                return true;
            }
        }
        return false;
    }

}
