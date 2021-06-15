import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

class MultithreadCalculate extends Thread {
    int PrimeNumCalculate = -1;
    int indexNum = -1;

    public int getPrimeNumCalculate() {
        return PrimeNumCalculate;
    }

    public void setPrimeNumCalculate(int primeNumCalculate) {
        PrimeNumCalculate = primeNumCalculate;
    }

    public int getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(int indexNum) {
        this.indexNum = indexNum;
    }

    public void run() {
        try {
            boolean isPrime = true;
            for (int i = 2; i < getPrimeNumCalculate(); i++) {
                if (getPrimeNumCalculate() % i == 0) {
                    isPrime = false;
                    MultiThreadPrimeNumGen.primeArray[0][getIndexNum()] = getPrimeNumCalculate();
                    MultiThreadPrimeNumGen.primeArray[1][getIndexNum()] = 0;
                    break;
                }
            }

            if (isPrime) {
                MultiThreadPrimeNumGen.primeArray[0][getIndexNum()] = getPrimeNumCalculate();
                MultiThreadPrimeNumGen.primeArray[1][getIndexNum()] = 1;
            }

            System.out.println("Thread " + Thread.currentThread().getId() + "; Index: " + getIndexNum() + "; Number: " + getPrimeNumCalculate() + "; isPrime: " + isPrime);
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
            primeArray[0][i] = -1;
        }

        for (int i = 0; i < cores; i++) {
            primeArray[1][i] = -1;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(System.getProperty("user.home") + "/Desktop" + "/PrimeNumber.txt");
        PrintWriter out = new PrintWriter(file);

        cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of Cores: " + cores);
        out.println(2);
        out.flush();
        while (true) {
            primeArray = new int[2][cores];
            fillArray();
            for (int i = 0; i < cores; i++) {
                MultithreadCalculate multithreadCalculate = new MultithreadCalculate();
                multithreadCalculate.setPrimeNumCalculate(primeBase += 2);
                multithreadCalculate.setIndexNum(i);
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
                    out.println(primeArray[0][i]);
                }
            }

            out.flush();
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
