package com.chr.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.internal.SessionImpl;

import com.chr.data.HibernateUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

@ManagedBean(name = "reportController")
@ViewScoped
public class ReportController implements Serializable {

	private static final long serialVersionUID = 1L;
	private String reportName = null;
	private int reportId;
	final String PREFIX = "/WEB-INF/reports/";
	final String SUFFIX = ".jasper";
	private int format=1;

	public void print() throws IOException, JRException, NamingException, SQLException {

		ServletContext theApplicationsServletContext = (ServletContext) FacesContext.getCurrentInstance()
				.getExternalContext().getContext();

		String realPath = theApplicationsServletContext.getRealPath("/WEB-INF/reports/");
		String imagePath = theApplicationsServletContext.getRealPath("/resources/images/");

		LinkedHashMap<String, Object> fillParams = new LinkedHashMap<>();
		setReportName("salaryProcess");
		/*
		 * General parameters
		 */
		fillParams.put("userName", JsfUtil.getUserName());
		fillParams.put("SUBREPORT_DIR", realPath + "\\");
		fillParams.put("imagePath", imagePath + "\\");
		switch (reportId) {
		case 1:
			setReportName("Agency_at_Glance");
			fillParams.put("reportNameParam", "AGENCY_AT_GLANCE");
			break;
		}

		generateReport(getReportName(), fillParams, realPath);

	}

	public void generateReport(String reportName, Map<String, Object> params, String path) {
		Connection conn = null;
		InputStream stream = null;
		int reportFormat = format;
		try {
			String type = "application/pdf";
			javax.naming.Context ctx = new javax.naming.InitialContext();

			
			SessionImpl sessionImpl = (SessionImpl) HibernateUtil.buildSessionFactory().openSession();
	        conn = sessionImpl.connection();

			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();

			stream = ec.getResourceAsStream(PREFIX + reportName + SUFFIX); // name

			if (stream == null) {
				throw new IllegalArgumentException("Unknown report name '" + reportName + "' requested");
			}

			// Fill the requested report with the specified data
			JasperPrint jasperPrint = null;
			try {
				jasperPrint = JasperFillManager.fillReport(stream, params, conn);
			} catch (Exception ex) {
				throw new FacesException(ex);
			} finally {
				// close the statement
				if (conn != null) {
					// cleanup and close the connection.
					try {
						conn.close();
					} catch (Exception ex) {
						throw new FacesException(ex);
					}
				}
				if (stream != null) {
					try {
						stream.close();
					} catch (Exception ex) {
						throw new FacesException(ex);
					}
				}
			}
			HttpServletResponse response = (HttpServletResponse) ec.getResponse();

			if (reportFormat == 1) {
				JRXlsExporter exporter = new JRXlsExporter();
				response.setContentType("application/xls");
				response.setHeader("Content-disposition", "attachment; filename= \"" + reportName + ".xls\"");
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				configuration.setOnePagePerSheet(true);
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				exporter.setConfiguration(configuration);
				exporter.exportReport();
			} else {
				Exporter exporter = null;
				response.setContentType(type);
				response.setHeader("Content-disposition", "attachment; filename= \"" + reportName + ".pdf\"");
				exporter = new JRPdfExporter();
				ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
				exporter.setExporterInput(exporterInput);
				OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
						response.getOutputStream());
				exporter.setExporterOutput(exporterOutput);
				SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
				exporter.setConfiguration(configuration);
				// Export the report
				exporter.exportReport();
				// Tell JavaServer Faces that no output is required
			}
			response.getOutputStream().flush();
			response.getOutputStream().close();
			fc.responseComplete();
		} catch (Exception ex) {
			System.out.println("Exception" + ex);
			throw new FacesException(ex);

		}
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

}
