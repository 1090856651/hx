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
		for time_cost_recom = 1:1  //三种策略， 0 表示时间最短， 1 表示代价最低，其他值 表示均衡
		    TIME_COST = time_cost_recom;

		////  常量参数初始化
		global table TABLE service EACH_SERVICE depot vehicle_capacity vehicle_num vehicle_cost capa_dis capa_time Time_diff max_n_area  SHOU_FA BUSS_TYPE

		  [capa_dis_20,capa_time_20] = get_distance();  //得到两两不同销售点之间的距离和时间
		  capa_dis_20 = capa_dis_20 / 1000;  //换算成公里
		  capa_time_20 = capa_time_20 / 60;  //换算成分钟

		Time_diff = 800;  //最长和最短时间差（单位为分钟）
		 // 费用计算方式，对于车辆1， 0-5公里48元，5-10公里58元，超出部分3元，等待10元/30分钟，装卸20, 超过3个点每点10元      
		vehicle_cost = [];
		vehicle_cost(1).mark = 1;vehicle_cost(1).km0_5 = 48; vehicle_cost(1).km5_10 = 58;vehicle_cost(1).km_over = 3;vehicle_cost(1).wait = 10;vehicle_cost(1).load = 20;vehicle_cost(1).three_nodes = 10;
		vehicle_cost(2).mark = 2;vehicle_cost(2).km0_5 = 58; vehicle_cost(2).km5_10 = 68;vehicle_cost(2).km_over = 4;vehicle_cost(2).wait = 15;vehicle_cost(2).load = 30;vehicle_cost(2).three_nodes = 10;
		vehicle_cost(3).mark = 3;vehicle_cost(3).km0_5 = 68; vehicle_cost(3).km5_10 = 78;vehicle_cost(3).km_over = 5;vehicle_cost(3).wait = 20;vehicle_cost(3).load = 40;vehicle_cost(3).three_nodes = 10;
		  
		////  解析输入参数，设置任务类别

		//BUSS_TYPE = set_cate();  // 0 表示多取一送，1 表示一取多送, 其他值 多取多送
		BUSS_TYPE = 1;
		//TIME_COST = 1;  //时间最短或者代价最低或者均衡， 0 表示时间最短， 1 表示代价最低，其他值 表示均衡
		////  此次任务初始化
		if 0 == BUSS_TYPE || 1 == BUSS_TYPE  //一取多送和多取一送只需要进行一次调度
		    SHOU_FA = BUSS_TYPE;
		    if 0 == BUSS_TYPE
		        [current_point] = init_job(); //取货任务初始化，获取参与此次调度的销售点列表
		    else
		        [current_point] = init_job2(); //送货任务初始化，获取参与此次调度的销售点列表
		    end

		    
		    [ALL_solution{1}, all_time{1}] = start(capa_dis_20, capa_time_20,current_point, depot,table);
		    EACH_SERVICE{1} = service;
		    TABLE{1} = table;
		else
		    SHOU_FA = 0; //多取一送， 第一步，先收集货物
		    [current_point] = init_job(); //收货任务初始化，获取参与此次调度的销售点列表
		    [ALL_solution{1}, all_time{1}] = start(capa_dis_20, capa_time_20,current_point, depot,table);
		    EACH_SERVICE{1} = service;
		    TABLE{1} = table;
		    
		    table=[]; service=[]; capa_dis = []; capa_time = [];vehicle_capacity = []; vehicle_num = [];max_n_area = [];
		    SHOU_FA = 1; //一取多送， 第二步，发送货物
		    [current_point] = init_job2(); //送货任务初始化，获取参与此次调度的销售点列表
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
