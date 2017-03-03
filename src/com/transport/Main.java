package com.transport;

public class Main {
	String TIME_COST;
	String cin_json_path;
	int nargin;
	public String[] main(String json_path,String location_detail_path,String UID){
		if(nargin<1){
			json_path = "F:/file/transport/code/cin_67.json";
	        location_detail_path = "F:/file/transport/code/detail_location.txt";
	        UID = "4";
		}
		cin_json_path = json_path;
		String[] result_file_path = new String[3];
		result_file_path[0] = "F:/file/transport/code/";
		result_file_path[1] = UID;
		result_file_path[2] = ".json";
		////
		result_json = {};
		opt_time = 0;
		for time_cost_recom = 1:1  //���ֲ��ԣ� 0 ��ʾʱ����̣� 1 ��ʾ������ͣ�����ֵ ��ʾ����
		    TIME_COST = time_cost_recom;

		////  ����������ʼ��
		global table TABLE service EACH_SERVICE depot vehicle_capacity vehicle_num vehicle_cost capa_dis capa_time Time_diff max_n_area  SHOU_FA BUSS_TYPE

		  [capa_dis_20,capa_time_20] = get_distance();  //�õ�������ͬ���۵�֮��ľ����ʱ��
		  capa_dis_20 = capa_dis_20 / 1000;  //����ɹ���
		  capa_time_20 = capa_time_20 / 60;  //����ɷ���

		Time_diff = 800;  //������ʱ����λΪ���ӣ�
		 // ���ü��㷽ʽ�����ڳ���1�� 0-5����48Ԫ��5-10����58Ԫ����������3Ԫ���ȴ�10Ԫ/30���ӣ�װж20, ����3����ÿ��10Ԫ      
		vehicle_cost = [];
		vehicle_cost(1).mark = 1;vehicle_cost(1).km0_5 = 48; vehicle_cost(1).km5_10 = 58;vehicle_cost(1).km_over = 3;vehicle_cost(1).wait = 10;vehicle_cost(1).load = 20;vehicle_cost(1).three_nodes = 10;
		vehicle_cost(2).mark = 2;vehicle_cost(2).km0_5 = 58; vehicle_cost(2).km5_10 = 68;vehicle_cost(2).km_over = 4;vehicle_cost(2).wait = 15;vehicle_cost(2).load = 30;vehicle_cost(2).three_nodes = 10;
		vehicle_cost(3).mark = 3;vehicle_cost(3).km0_5 = 68; vehicle_cost(3).km5_10 = 78;vehicle_cost(3).km_over = 5;vehicle_cost(3).wait = 20;vehicle_cost(3).load = 40;vehicle_cost(3).three_nodes = 10;
		  
		////  ������������������������

		//BUSS_TYPE = set_cate();  // 0 ��ʾ��ȡһ�ͣ�1 ��ʾһȡ����, ����ֵ ��ȡ����
		BUSS_TYPE = 1;
		//TIME_COST = 1;  //ʱ����̻��ߴ�����ͻ��߾��⣬ 0 ��ʾʱ����̣� 1 ��ʾ������ͣ�����ֵ ��ʾ����
		////  �˴������ʼ��
		if 0 == BUSS_TYPE || 1 == BUSS_TYPE  //һȡ���ͺͶ�ȡһ��ֻ��Ҫ����һ�ε���
		    SHOU_FA = BUSS_TYPE;
		    if 0 == BUSS_TYPE
		        [current_point] = init_job(); //ȡ�������ʼ������ȡ����˴ε��ȵ����۵��б�
		    else
		        [current_point] = init_job2(); //�ͻ������ʼ������ȡ����˴ε��ȵ����۵��б�
		    end

		    
		    [ALL_solution{1}, all_time{1}] = start(capa_dis_20, capa_time_20,current_point, depot,table);
		    EACH_SERVICE{1} = service;
		    TABLE{1} = table;
		else
		    SHOU_FA = 0; //��ȡһ�ͣ� ��һ�������ռ�����
		    [current_point] = init_job(); //�ջ������ʼ������ȡ����˴ε��ȵ����۵��б�
		    [ALL_solution{1}, all_time{1}] = start(capa_dis_20, capa_time_20,current_point, depot,table);
		    EACH_SERVICE{1} = service;
		    TABLE{1} = table;
		    
		    table=[]; service=[]; capa_dis = []; capa_time = [];vehicle_capacity = []; vehicle_num = [];max_n_area = [];
		    SHOU_FA = 1; //һȡ���ͣ� �ڶ��������ͻ���
		    [current_point] = init_job2(); //�ͻ������ʼ������ȡ����˴ε��ȵ����۵��б�
		    [ALL_solution{2}, all_time{2}] = start(capa_dis_20, capa_time_20,current_point, depot,table);
		    EACH_SERVICE{2} = service;
		    TABLE{2} = table;
		end

		disp 'GA finished! to get json!\n'
		result_json{time_cost_recom+1} = show_result_in_json(ALL_solution);
		time_used = sum(cell2mat(all_time));
		fprintf('strategy //d finished! using time //d s...\n',time_cost_recom ,time_used);
		opt_time = opt_time + time_used;
		end

		mark_empty = [];
		for i = 1:size(result_json,2)
		    if(isempty(result_json{i}))
		        mark_empty = [mark_empty, i];
		    end
		end
		result_json(mark_empty) = []; 

		for i = 1:size(result_json,2)
		    temp_strategy = result_json{i};
		    temp_strategy_name{i} = [temp_strategy.businesstype,'_',temp_strategy.strategy.name];
		    temp_strategy_result{i} = temp_strategy;
		end
		if size(result_json,2) == 1
		    result_struct = struct(temp_strategy_name{1},temp_strategy_result{1});
		elseif size(result_json,2) == 2
		    result_struct = struct(temp_strategy_name{1},temp_strategy_result{1},temp_strategy_name{2},temp_strategy_result{2});
		elseif size(result_json,2) == 3
		    result_struct = struct(temp_strategy_name{1},temp_strategy_result{1},temp_strategy_name{2},temp_strategy_result{2},temp_strategy_name{3},temp_strategy_result{3});
		else
		    disp 'something wrong!';
		end

		savejson('',result_struct,result_file_path);

		fprintf('optimize finished! used time: //d s\n', opt_time);
		end
		return result_file_path;
	}
}
