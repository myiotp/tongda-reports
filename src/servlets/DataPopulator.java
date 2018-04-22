package servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class DataPopulator {
	public static String DATE_FORMAT = "yyyy年MM月dd日";
	
	private static String polish(Object obj) {
		if(obj == null)
			return "";
		return obj.toString();
	}
	private static String polishDate(Object obj) {
		if(obj == null)
			return "";
//		SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
//	    Date date = new Date(Long.parseLong(obj.toString()));
//	    return sf.format(date);
		return obj.toString();
	}
	private static String item19(JSONObject jsonObject) {
		StringBuffer sb = new StringBuffer();
		for(int i = 1; i<=5; i++) {
			Object item = jsonObject.get("item"+i);
			Object price = jsonObject.get("price"+i);
			if(price != null) {
				try {
					double d = Double.parseDouble(price.toString());
					if(d > 0) {
						sb.append(item).append(":").append(d).append("元\r\n");
					}
				}catch(Exception e) {
					//
				}
			}
		}
		
		return sb.toString();
	}
//	private static Object polishCompanyContactInfo(JSONObject jsonObject) {
//		if(jsonObject == null)
//			return "";
//		return "公司电话：" + polish(jsonObject.get("company1")) + "   传真：" + polish(jsonObject.get("company2")) +  "手机：" + polish(jsonObject.get("company3"));
//	}
	private static Object polishCompanyAddress(JSONObject jsonObject) {
		if(jsonObject == null)
			return "";
		return polish(jsonObject.get("company4"));
	}
	public static Map<String, Object> populate(String url) {
		url = "http://localhost:8080/onemap/api/usertransaction/id/" + url;
		Map<String, Object> map = new HashMap<>();
		map.put("itme0", "https://tongdagufen.cn/images/kaida56_logo.png");
		map.put("item1", "山 东 凯 达 物 流 有 限 公 司");
		map.put("item2", "货 物 运 输 协 议 书");
		
		JSONObject jsonObject = get(url);
		if(jsonObject != null) {
			map.put("item3", jsonObject.get("ordernumber"));
			
			map.put("item19", item19(jsonObject));
			map.put("item20", polish(jsonObject.get("approver1")));
			map.put("item21", polishDate(jsonObject.get("datetime1")));
			map.put("item22", polish(jsonObject.get("approver2")));
			map.put("item23", polishDate(jsonObject.get("datetime2")));
			map.put("item24", polish(jsonObject.get("approver3")));
			map.put("item25", polishDate(jsonObject.get("datetime3")));
			map.put("item26", polish(jsonObject.get("approver4")));
			map.put("item27", polishDate(jsonObject.get("datetime4")));
			map.put("item28", polish(jsonObject.get("company1")));
			map.put("item281", polish(jsonObject.get("company2")));
			map.put("item282", polish(jsonObject.get("company3")));
			map.put("item29", polishCompanyAddress(jsonObject));
			map.put("item30", polish(jsonObject.get("approver5")));
			map.put("item31", polish(jsonObject.get("approver6")));
			
			JSONObject cargoObj = jsonObject.getJSONObject("cargoObj");
			JSONObject truckObj = jsonObject.getJSONObject("truckObj");
			if(cargoObj != null) {
				map.put("item4", cargoObj.get("cargoOwner"));
				map.put("item5", cargoObj.get("ownerCellphone"));
				map.put("item6", cargoObj.get("username"));
				map.put("item7", cargoObj.get("emergencyCellphone"));
				
				map.put("item12", polish(cargoObj.get("fromname")) + polish(cargoObj.get("fromAddress")));
				
				map.put("item14", polish(cargoObj.get("toname")) + polish(cargoObj.get("toAddress")));
				map.put("item15", polish(cargoObj.get("price")));
				map.put("item16", "");
				map.put("item17", polish(cargoObj.get("shipTimestamp")));
				map.put("item18", polish(cargoObj.get("shipTimestamp")));//@ TODO
			}
			if(truckObj != null) {
				map.put("item8", truckObj.get("owner"));
				map.put("item9", truckObj.get("ownerCellphone"));
				map.put("item10", truckObj.get("username"));
				map.put("item11", truckObj.get("emergencyCellphone"));
				
				map.put("item13", polish(truckObj.get("fromname")) + polish(truckObj.get("fromAddress")));
			}
		}
		return map;
	}
	
	
	private static JSONObject get(String url) {
		// get请求返回结果
		JSONObject jsonResult = null;
		CloseableHttpClient client = HttpClients.createDefault();
		// 发送get请求
		HttpGet request = new HttpGet(url);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
		request.setConfig(requestConfig);
		try {
			CloseableHttpResponse response = client.execute(request);

			// 请求发送成功，并得到响应
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 读取服务器返回过来的json字符串数据
				HttpEntity entity = response.getEntity();
				String strResult = EntityUtils.toString(entity, "utf-8");
				// 把json字符串转换成json对象

				jsonResult = JSONObject.parseObject(strResult);
				jsonResult = jsonResult.getJSONObject("data");
			} else {
				System.out.println("get请求提交失败:" + url);
			}
		} catch (IOException e) {
			System.out.println("get请求提交失败:" + url);
			e.printStackTrace();
		} finally {
			request.releaseConnection();
		}
		return jsonResult;
	}
}
