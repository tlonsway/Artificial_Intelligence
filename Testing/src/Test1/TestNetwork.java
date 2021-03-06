package Test1;

import org.jblas.*;
import java.util.*;

public class TestNetwork {
	public static void main(String[] args) {
		/*network has 2 inputs, each being a different number
		 * network has 2 outputs, output 0 says input 0 is bigger and vice versa
		 * network trained to test deciding which number is bigger or smaller
		 */
		int[] layers = new int[] {2,2560,2560,2};
		Network n = new Network(layers, 0.05,new Sigmoid());
		long startTime = System.nanoTime();
		int numBackProps = 50000;
		for(int i=0;i<numBackProps;i++) {
			DoubleMatrix x = DoubleMatrix.rand(2,1);
			DoubleMatrix y = new DoubleMatrix(2,1);
			if (x.get(1,0) > x.get(0,0)) {
				y.put(1,0,1);
			} else {
				y.put(0,0,1);
			}
			n.backProp(x, y);
			if (i%100==0) {
				System.out.println(100*(double)i/(double)numBackProps + "% complete");
			}
		}
		long endTime = System.nanoTime();
		long totalTime = endTime-startTime;
		System.out.println("Number of backprops/sec: " + ((double)numBackProps/((double)totalTime/1000000000)));
		int numRight = 0;
		int numTests = 10000;
		for(int i=0;i<numTests;i++) {
			DoubleMatrix x = DoubleMatrix.rand(2,1);
			DoubleMatrix y = new DoubleMatrix(2,1);
			int correct = 0;
			if (x.get(1,0) > x.get(0,0)) {
				y.put(1,0,1);
				correct = 1;
			} else {
				y.put(0,0,1);
			}
			DoubleMatrix result = n.feedForward(x);
			if (result.get(0,0) > result.get(1,0)) {
				if (correct == 0) {
					numRight++;
				} 
			} else {
				if (correct == 1) {
					numRight++;
				}
			}
		}
		System.out.println("Percent Correct: " + 100*(double)numRight/(double)numTests + "%");
		//for(int i=0;i<n.wMatrixArr.length;i++) {
		//	System.out.println(n.wMatrixArr[i]);
		//	System.out.println(n.bMatrixArr[i]);
		//}
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter numbers to have neural network compare them:");
		boolean repeat = true;
		while(repeat) {
			System.out.print("Num 1: ");
			double num1 = sc.nextDouble();
			System.out.print("\nNum 2: ");
			double num2 = sc.nextDouble();
			DoubleMatrix x = new DoubleMatrix(2,1);
			x.put(0,0,num1);
			x.put(1,0,num2);
			DoubleMatrix y = n.feedForward(x);
			System.out.println("\n" + y);
		}
		sc.close();
	}
}