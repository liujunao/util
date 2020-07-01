package com.wonders.framework.util;

import java.math.BigDecimal;

public class DigitalUtil {
/**
 * 
 * @param a1 除数
 * @param a2 被除数
 * @return  保留两位小数点
 */
	public static Double Divide(Long a1,Long a2){
		BigDecimal bga1= new BigDecimal(a1);
		BigDecimal bga2= new BigDecimal(a2);
		if (bga2.equals(new BigDecimal("0"))) {
			return 0.0D;
		}else{
			return bga1.divide(bga2, 5,BigDecimal.ROUND_HALF_UP).doubleValue();		
		}
		
	}
	public static Double Divide(Double a1,Double a2){
		BigDecimal bga1= new BigDecimal(a1);
		BigDecimal bga2= new BigDecimal(a2);
		if (bga2.equals(new BigDecimal("0")) || bga2.equals(new BigDecimal("0.0"))) {
			return 0.0D;
		}else{
			return bga1.divide(bga2, 5,BigDecimal.ROUND_HALF_UP).doubleValue();		
		}
		
	}
	public static Double Divide(Integer a1,Integer a2){
		BigDecimal bga1= new BigDecimal(a1);
		BigDecimal bga2= new BigDecimal(a2);
		if (bga2.equals(new BigDecimal("0"))) {
			return 0.0D;
		}else{
			return bga1.divide(bga2, 5,BigDecimal.ROUND_HALF_UP).doubleValue();		
		}
		
	}
	public static Double Divide(Number a1,Number a2){
		BigDecimal bga1= new BigDecimal(a1.toString());
		BigDecimal bga2= new BigDecimal(a2.toString());
		if (bga2.equals(new BigDecimal("0")) || bga2.equals(new BigDecimal("0.0"))) {
			return 0.0D;
		}else{
			return bga1.divide(bga2, 5,BigDecimal.ROUND_HALF_UP).doubleValue();		
		}
		
	}
	
	
	public static Double Multiply(Number a1,Number a2){
		BigDecimal bga1= new BigDecimal(a1.toString());
		BigDecimal bga2= new BigDecimal(a2.toString());
		return bga1.multiply(bga2).doubleValue();
	}
}
