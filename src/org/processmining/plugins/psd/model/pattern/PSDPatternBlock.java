package org.processmining.plugins.psd.model.pattern;

import org.processmining.plugins.psd.model.PSDBlock;

public class PSDPatternBlock extends PSDBlock {
	private double avgBeginTime = 0;
	private double avgEndTime = 0;
	private long frequencyBegin = 0;
	private long frequencyEnd = 0;

	/**
	 * Constructor
	 * 
	 * @param beginTime
	 *            long
	 * @param endTime
	 *            long
	 * @param dataInst
	 *            String
	 */
	public PSDPatternBlock(long beginTime, long endTime, String dataInst) {
		super(dataInst);
		avgBeginTime = beginTime;
		avgEndTime = endTime;
		frequencyBegin = 1;
		frequencyEnd = 1;
	}

	/**
	 * Adjusts the average begin time, so beginTime is included.
	 * 
	 * @param beginTime
	 *            double
	 */
	public void addBeginTime(double beginTime) {
		double totalTime = avgBeginTime * frequencyBegin;
		frequencyBegin++;
		avgBeginTime = (totalTime + beginTime) / frequencyBegin;
	}

	/**
	 * Adjusts the average end time, so endTime is included.
	 * 
	 * @param endTime
	 *            double
	 */
	public void addEndTime(double endTime) {
		double totalTime = avgEndTime * frequencyEnd;
		frequencyEnd++;
		avgEndTime = (totalTime + endTime) / frequencyEnd;
	}

	/**
	 * Returns the average begin time of the block
	 * 
	 * @return double
	 */
	public double getAverageBeginTime() {
		return avgBeginTime;
	}

	/**
	 * Returns the average end time of the block
	 * 
	 * @return double
	 */
	public double getAverageEndTime() {
		return avgEndTime;
	}

	/**
	 * Returns the average duration of this period of activity
	 * 
	 * @return double
	 */
	public double getTimeIn() {
		return (avgEndTime - avgBeginTime);
	}
}

