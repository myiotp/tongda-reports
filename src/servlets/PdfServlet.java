package servlets;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datasource.WebappDataSource;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;

/**
 */
public class PdfServlet extends BaseHttpServlet {
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		ServletContext context = this.getServletConfig().getServletContext();

//		List<JasperPrint> jasperPrintList = BaseHttpServlet.getJasperPrintList(request);
//
//		if (jasperPrintList == null) {
//			throw new ServletException("No JasperPrint documents found on the HTTP session.");
//		}

		Boolean isBuffered = Boolean.valueOf(request.getParameter(BaseHttpServlet.BUFFERED_OUTPUT_REQUEST_PARAMETER));
//		if (isBuffered.booleanValue()) {
//			FileBufferedOutputStream fbos = new FileBufferedOutputStream();
//			JRPdfExporter exporter = new JRPdfExporter(DefaultJasperReportsContext.getInstance());
//			exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
//			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fbos));
//
//			try {
//				exporter.exportReport();
//				fbos.close();
//
//				if (fbos.size() > 0) {
//					response.setContentType("application/pdf");
//					response.setContentLength(fbos.size());
//					ServletOutputStream outputStream = response.getOutputStream();
//
//					try {
//						fbos.writeData(outputStream);
//						fbos.dispose();
//						outputStream.flush();
//					} finally {
//						if (outputStream != null) {
//							try {
//								outputStream.close();
//							} catch (IOException ex) {
//							}
//						}
//					}
//				}
//			} catch (JRException e) {
//				throw new ServletException(e);
//			} finally {
//				fbos.close();
//				fbos.dispose();
//			}
//
//			// else
//			// {
//			// response.setContentType("text/html");
//			// PrintWriter out = response.getWriter();
//			// out.println("<html>");
//			// out.println("<body bgcolor=\"white\">");
//			// out.println("<span class=\"bold\">Empty response.</span>");
//			// out.println("</body>");
//			// out.println("</html>");
//			// }
//		} else {
			response.setContentType("application/pdf");
			OutputStream outputStream = response.getOutputStream();
			try {

				JRPdfExporter exporter = new JRPdfExporter(DefaultJasperReportsContext.getInstance());

				File reportFile = new File(context.getRealPath("/reports/" + ReportConstants.REPORT_NAME + ".jasper"));
				if (!reportFile.exists()) {
					// throw new JRRuntimeException(
					// "File WebappReport.jasper not found. The report design must be compiled
					// first.");
					JasperCompileManager.compileReportToFile(
							context.getRealPath("/reports/" + ReportConstants.REPORT_NAME + ".jrxml"));
				}

				JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportFile.getPath());

				String url = request.getParameter(ReportConstants.REPORT_URL);
				Map<String, Object> parameters = DataPopulator.populate(url);
				// Map<String, Object> parameters = new HashMap<String, Object>();
				// parameters.put("ReportTitle", "Address Report");
				// parameters.put("BaseDir", reportFile.getParentFile());
				// parameters.put("cargoowner", "刘大壮");
				for (Map.Entry<String, Object> entry : parameters.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();
					System.out.println("key=" + key + " value=" + value);
				}
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
						new WebappDataSource());

				// exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

				
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

				exporter.exportReport();
			} catch (Exception e) {
				throw new ServletException(e);
			} finally {
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException ex) {
					}
				}
			}
//		}
	}

}
