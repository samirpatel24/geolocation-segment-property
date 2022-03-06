package com.liferay.geolocation.segment.context.contibutor;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.segments.context.Context;
import com.liferay.segments.context.contributor.RequestContextContributor;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Component;

/**
 * @author samir
 */
@Component(
	immediate = true,
	property = {
	"request.context.contributor.key=" +GeolocationSegmentContextContributor.KEY,
	"request.context.contributor.type=String"
	},
	service = RequestContextContributor.class
)
public class GeolocationSegmentContextContributor
	implements RequestContextContributor {

	
	public static final String KEY = "country";
	
	
	@Override
	public void contribute(Context context, HttpServletRequest httpServletRequest) {
		// TODO Auto-generated method stub
		
		String actualLocation = "unknown";
		String jsonPayload = "";
		if (context.get(KEY) == null) {
			try {
				ObjectMapper mapper = new ObjectMapper();

				String ipAddress = getIpAddress(httpServletRequest);
				_log.debug(String.format("Using ip %s", ipAddress));

				HttpUriRequest request = RequestBuilder
						.get("https://ipapi.co/" + ipAddress + "/json")
						.build();

				JsonNode jsonResult = mapper.readTree(getRequest(request));

				System.out.println("Result"+jsonResult);
				jsonPayload = jsonResult.toString();
				String country = jsonResult.get("country_name").asText();
				
				actualLocation = country;
				
		//	System.out.println("IP "+ip);
			
			}catch (IOException e) {
				_log.debug(e.getMessage());
				_log.debug(e.getStackTrace());
			} catch (Exception e) {
				_log.debug(e.getMessage());
				_log.debug("Global exception: " + e.toString());
			}
			
			}
	
		_log.debug(String.format("The country is %s", actualLocation));
		_log.debug(String.format("The jsonpayload  is %s", jsonPayload));

		context.put(KEY, actualLocation);
	}
	
	private String getRequest(HttpUriRequest request) {
		HttpClient client = HttpClientBuilder.create().build();

		HttpResponse response = null;
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(entity, "UTF-8");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}
	
	
	private String getIpAddress (HttpServletRequest request) {

		String ipAddress = request.getHeader("X-Real-Ip");
		if (ipAddress == null || ipAddress.isEmpty()) {
			ipAddress = request.getHeader("X-Forwarded-For");
		}
		if (ipAddress == null || ipAddress.isEmpty()) {
			ipAddress = request.getRemoteAddr();
		}

		return ipAddress.replaceFirst(",.*","").trim();
	}
	
	private static final Log _log = LogFactoryUtil.getLog(GeolocationSegmentContextContributor.class);
	
		
}