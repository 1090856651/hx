package com.transport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GetDistance {
	public int[][][] getDistance(){
		int[][][] newArray = new int[2][][];
		//�������
		int[][] D = new int[67][67];
		//ʱ�����
		int[][] T = new int[67][67];
		newArray[0] = D;
		newArray[1] = T;
		try {
			//��ȡλ��ʱ�����Ϣ
			BufferedReader br = new BufferedReader(new FileReader("F:/detail_location.txt"));
			String s = null;
			String[] all_dis_time_line = new String[4];
			while((s=br.readLine())!=null){
				all_dis_time_line = s.split(" ");
				//��һ��Ϊ�б꣬�ڶ���Ϊ�б꣬������Ϊ���룬������Ϊʱ���
				int row = Integer.valueOf(all_dis_time_line[0]);
				int clomn = Integer.valueOf(all_dis_time_line[1]);
				D[row][clomn] = Integer.valueOf(all_dis_time_line[2]);
				D[clomn][row] = Integer.valueOf(all_dis_time_line[2]);
				T[row][clomn] = Integer.valueOf(all_dis_time_line[3]);
				T[clomn][row] = Integer.valueOf(all_dis_time_line[3]);
			}
			br.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < T.length; i++) {
			for (int j = 0; j < T[i].length; j++) {
				System.out.print(T[i][j]+" ");
			}
			System.out.println();
		}
		return newArray;
	}
}
