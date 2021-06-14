import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

class MultithreadCalculate extends Thread {
    public void run() {
        try {
            int indexNum = -1;
            for (int i = 0; i < MultiThreadPrimeNumGen.cores; i++) {
                if (MultiThreadPrimeNumGen.primeArray[1][i] == -1) {
                    MultiThreadPrimeNumGen.primeArray[1][i] = 2;
                    indexNum = i;
                    break;
                }
            }

            boolean isPrime = true;
            for (int i = 2; i < MultiThreadPrimeNumGen.primeArray[0][indexNum]; i++) {
                if (MultiThreadPrimeNumGen.primeArray[0][indexNum] % i == 0) {
                    isPrime = false;
                    MultiThreadPrimeNumGen.primeArray[1][indexNum] = 0;
                    break;
                }
            }

            if (isPrime) {
                MultiThreadPrimeNumGen.primeArray[1][indexNum] = 1;
            }

            System.out.println("Thread " + Thread.currentThread().getId() + "; Claimed Index: " + indexNum + "; Claimed Number: " + MultiThreadPrimeNumGen.primeArray[0][indexNum] + "; isPrime: " + isPrime);
        }
        catch (Exception e) {
            System.out.println("Exception is caught");
        }
    }
}


public class MultiThreadPrimeNumGen {
    public static int [][] primeArray;
    public static int primeBase = 1;
    public static int cores;

    private static void fillArray() {
        for (int i = 0; i < cores; i++) {
            primeBase += 2;
            primeArray[0][i] = primeBase;
        }

        for (int i = 0; i < cores; i++) {
            primeArray[1][i] = -1;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(System.getProperty("user.home") + "/Desktop" + "/PrimeNumber.txt");
        PrintWriter out = new PrintWriter(file);

        //Gets number of CPU Cores
        cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of Cores: " + cores);

        primeArray = new int[2][cores];
        fillArray();
        printMatrix(primeArray);
        for (int i = 0; i < cores; i++) {
            MultithreadCalculate multithreadCalculate = new MultithreadCalculate();
            multithreadCalculate.start();
        }

        while (true) {
            boolean flag = false;
            for (int i = 0; i < cores; i++) {
                if ((primeArray[1][i] == 0) || (primeArray[1][i] == 1)) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                break;
            }
        }
        printMatrix(primeArray);

        for (int i = 0; i < cores; i++) {
            if (primeArray[1][i] == 1) {
                out.println("PrimeNum: " + primeArray[0][i]);
                out.flush();
            }
        }
    }

    public static void printMatrix(int[][] arr) {
        if (null == arr || arr.length == 0) {
            return;
        }
        int idx = -1;
        StringBuilder[] sbArr = new StringBuilder[arr.length];
        for (int[] row : arr) {
            sbArr[++idx] = new StringBuilder("[\t");
            for (int elem : row) {
                sbArr[idx].append(elem).append("\t");
            }
            sbArr[idx].append("]");
        }
        for (StringBuilder stringBuilder : sbArr) {
            System.out.println(stringBuilder);
        }
    }
}
