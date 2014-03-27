package videoplayer.viewer;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import gov.loc.mets.MetsType.FileSec.FileGrp;
import gov.loc.mets.StructMapType;

import com.exlibris.digitool.common.dnx.DnxDocument;
import com.exlibris.digitool.common.dnx.DnxDocumentFactory;
import com.exlibris.digitool.common.dnx.DnxDocumentHelper;
import com.exlibris.dps.ws.delivery.DeliveryAccessWS;
import com.exlibris.dps.ws.delivery.DeliveryAccessWS_Service;
import com.exlibris.dps.ws.delivery.Exception_Exception;
import com.exlibris.dps.sdk.deposit.IEParser;
import com.exlibris.dps.sdk.deposit.IEParserFactory;
/**
 *
 * @author MatanS
 */
public class VideoPlayerViewer extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String VIEWER_PROPERTIES = "/conf/viewer.properties";
	private static final String WSDL_LOCATION = "wsdlLocation";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String wsdlLocation = getWsdUrl();
		URL wsdlLocationUrl = new URL(wsdlLocation);
		DeliveryAccessWS deliveryAccessWS = new DeliveryAccessWS_Service(wsdlLocationUrl,new QName("http://dps.exlibris.com/", "DeliveryAccessWS")).getDeliveryAccessWSPort();

		String dpsDvs = request.getParameter("dps_dvs");
		String pid = request.getParameter("dps_pid");
		List<VideoFile> videoFiles = new ArrayList<VideoFile>();
		@SuppressWarnings("unchecked")
		VideoPlayerBean bean = new VideoPlayerBean(request.getParameterMap());

		//We are dealing with the IE viewer
		if (pid.startsWith("IE")) {

			IEParser ieParser = null;
			FileGrp[] repList = null;
			try {
				ieParser = IEParserFactory.parse(deliveryAccessWS.getIEByDVS(dpsDvs));
				repList = ieParser.getFileGrpArray();
			} catch (Exception e) {
				e.printStackTrace();
			}
			StructMapType[] structMapTypeArray = ieParser.getStructMapsByFileGrpId(repList[0].getID());
			StructMapType mapType = null;

			for (StructMapType structMapType : structMapTypeArray) {
				mapType = structMapType;
				if(structMapType.getTYPE().equals("LOGICAL")){
					break;
				}
			}

			List<Map<String, String>> filesFromStructMapInfo = ieParser.getFilesFromStructMapInfo(mapType.getDiv());

			for (Map<String, String> map : filesFromStructMapInfo) {
				String key = (String)map.keySet().toArray()[0];
				videoFiles.add(new VideoFile(key, map.get(key)));
			}

		} else { //we are dealing with the file viewer
			String fileName = "";
			DnxDocument dnxDoc = null;
			try {
				dnxDoc = DnxDocumentFactory.getInstance().parse(deliveryAccessWS.getDnxDocument(dpsDvs, pid));
			} catch (Exception_Exception e) {
				e.printStackTrace();
			}
			if (dnxDoc != null) {
				DnxDocumentHelper dnxDocumentHelper = new DnxDocumentHelper(dnxDoc);
				fileName = dnxDocumentHelper.getGeneralFileCharacteristics().getFileOriginalName();
			}
			videoFiles.add(new VideoFile(pid, fileName));
		}

		String redirectFiles = "";//template: pid,label;pid,label;
		for (VideoFile vf : videoFiles) {
			redirectFiles += vf.getPid()+","+vf.getLabel() +";";
		}
		redirectFiles = redirectFiles.substring(0, redirectFiles.length()-1);
		redirectFiles += ";";

		String redirect = "";
		try {
			String delServer = deliveryAccessWS.getBaseFileUrl(dpsDvs);
			redirect = "video_player.jsp?files="+redirectFiles;
			request.setAttribute("delServer", delServer);
			request.setAttribute("bean", bean);
		} catch (Exception e) {
			e.printStackTrace();
			redirect = "error.jsp";
		}
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(redirect);
		requestDispatcher.forward(request, response);
	}

	private String getWsdUrl() {
		PropertyResourceBundle resourceBundle = null;
		InputStream resourceAsStream = null;
		try {
			resourceAsStream = getServletContext().getResourceAsStream(VIEWER_PROPERTIES);
			resourceBundle = new PropertyResourceBundle(resourceAsStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				resourceAsStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String wsdlLocation =  resourceBundle.getString(WSDL_LOCATION);
		return wsdlLocation;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}