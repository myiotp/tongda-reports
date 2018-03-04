import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import datasource.WebappDataSource;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

public class TestReportHere {
	 public static void main(String[] args) {  
	        ByteArrayOutputStream outPut=new ByteArrayOutputStream();  
	        FileOutputStream outputStream=null;  
	        File file=new File("/Users/xiningwang/Downloads/report.xlsx");  	          
	        try {  
	        	JasperReport jasperReport = (JasperReport)JRLoader.loadObjectFromFile("/Users/xiningwang/Documents/workspace-sts-3.9.0.RELEASE/reports/WebContent/reports/ShipOrder_A4_Landscape.jasper");
	    		
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("item0", "http://47.104.158.49/images/kaida56_logo.png");
				parameters.put("item1", "Address Report");
				parameters.put("item2", "刘大壮");
							
				JasperPrint jasperPrint = 
					JasperFillManager.fillReport(
						jasperReport, 
						parameters, 
						new WebappDataSource()
						);
	          
	        JRAbstractExporter exporter = new JRXlsExporter();  
	        //创建jasperPrint  
	            exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);  
	            //生成输出流  
	            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,outPut);  
	            //去除两行之前的空白  
	            exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);  
	            //设置所有页只打印到一个Sheet中  
	            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);  
	            //设置Excel表格的背景颜色为默认的白色  
	            exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);  
	            exporter.exportReport();  
	            outputStream=new FileOutputStream(file);  
	            outputStream.write(outPut.toByteArray());  

	        }catch (Exception e) {  
	            e.printStackTrace();  
	        }finally{  
	            try {  
	                outPut.flush();  
	                outPut.close();  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }  
	        }  
	    }  
}
