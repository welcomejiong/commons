package org.corps.bi.tools.util;

import java.util.Random;

public class ProbabilityGenerator {
	
	private static final ProbabilityGenerator DEFAULT_INSTANCE = new ProbabilityGenerator();

	private static Random r = new Random();

	private ProbabilityGenerator() {
		super();
	}

	public static ProbabilityGenerator getInstance() {
		return DEFAULT_INSTANCE;
	}

	/**
     * 产生0到max之间的整数，包括max，概率平均 例如max=3，则产生0,1,2,3这四个数字的概率均为25% [min, max]
     * 
     * @param max
     * @return
     */
	public int getRandomNumber(int max) {
		return getRandomNumber(0, max);
	}

	public int getRandomNumber(int min, int max) {
		int result = r.nextInt((max - min + 1) << 5);
		return (result >> 5) + min;
	}

	public int getRandomChoiceWithPercentArr(int[] percentArr) {
		int sum = 0;
		for (int i : percentArr) {
			sum += i;
		}
		if (sum != 100)
			throw new RuntimeException("Sum(percentArr,i) must be 100.");

		int r = getRandomNumber(99);
		int total = 0;
		for (int i = 0; i < percentArr.length; i++) {
			total += percentArr[i];
			if (r < total) {
				return i;
			}
		}
		throw new RuntimeException("It can't be here!");
	}

	public int getRandomChoiceWithRatioArr(int[] ratios) {
		int sum = 0;
		for (int i : ratios) {
			sum += i;
		}

		int r = getRandomNumber(1, sum);
		int total = 0;
		for (int i = 0; i < ratios.length; i++) {
			total += ratios[i];
			if (r <= total) {
				return i;
			}
		}
		throw new RuntimeException("It can't be here!");
	}

	public boolean getRandomWithPercentage(float percentage) {
		float nextFloat = r.nextFloat();
		return nextFloat < percentage / 100f;
	}

	public boolean getReandomBoolean() {
		return r.nextBoolean();
	}

	public int getRandomChoiceWithPermillageArr(int[] permililageArr) {
		int sum = 0;
		for (int i : permililageArr) {
			sum += i;
		}
		if (sum != 1000)
			throw new RuntimeException("Sum(percentArr,i) must be 1000.");

		int r = getRandomNumber(999);
		int total = 0;
		for (int i = 0; i < permililageArr.length; i++) {
			total += permililageArr[i];
			if (r < total) {
				return i;
			}
		}
		throw new RuntimeException("It can't be here!");
	}

	public float nextFloat() {
		return r.nextFloat();
	}

	/**
	 * 数组中有可能为0的 需要忽略掉 [80,60,50,20]
	 */
	public int getAverageChoiceIndex(float[] percentageArr) {
		float choice = this.nextFloat();
		float occurChoice = 1;
		float totalOccurChoice = 0;
		for (float percentage : percentageArr) {
			occurChoice *= (1 - percentage / 100);
			totalOccurChoice += percentage;
		}
		occurChoice = 1 - occurChoice;

		if (choice < occurChoice) {
			int index = 0;
			float currentIndexChoice = 0;
			for (float percentage : percentageArr) {
				currentIndexChoice += percentage / totalOccurChoice
						* occurChoice;
				if (choice < currentIndexChoice) {
					return index;
				}
				index++;
			}
		}
		return -1;
	}

	/**
	 * 数组中有可能为0的 需要忽略掉
	 */
	public int getRandomChoiceIndex(float[] choiceArr) {

		float sum = 0;
		for (float i : choiceArr) {
			sum += i;
		}
		if (sum != 1) {
			System.out.println("sum:" + sum);
			// throw new RuntimeException("Sum(percentArr,i) must be 100.!");
		}

		float choice = nextFloat();
		float total = 0;
		for (int i = 0; i < choiceArr.length; i++) {
			if (choiceArr[i] == 0f) {
				continue;
			}
			total += choiceArr[i];
			if (choice <= total) {
				return i;
			}
		}
		throw new RuntimeException("It can't be here!");
	}

	public int getRandomChoice(int[] choiceArr) {
		int sum = 0;
		for (int i : choiceArr) {
			sum += i;
		}

		if (sum < 1)
			throw new IllegalArgumentException(
					"seems the choice array is not correct");

		int r = getRandomNumber(sum - 1);
		int total = 0;
		for (int i = 0; i < choiceArr.length; i++) {
			total += choiceArr[i];
			if (r < total) {
				return i;
			}
		}

		throw new RuntimeException("It can't be here!");
	}

	public int randomBasedOnWeight(int[] weights) {
		int[] cumWeight = new int[weights.length + 1];
		cumWeight[0] = 0;
		for (int i = 0; i < weights.length; i++) {
			cumWeight[i + 1] = cumWeight[i] + weights[i];
		}
		int rand = r.nextInt(cumWeight[cumWeight.length - 1]);
		for (int i = 0; i < cumWeight.length - 1; i++) {
			if (rand >= cumWeight[i] && rand < cumWeight[i + 1]) {
				return i;
			}
		}
		throw new RuntimeException("Shouldn't reach here!");
	}
}
