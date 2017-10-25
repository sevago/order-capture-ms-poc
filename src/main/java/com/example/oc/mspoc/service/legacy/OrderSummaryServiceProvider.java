package com.example.oc.mspoc.service.legacy;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amdocs.cih.common.core.MaskInfo;
import com.amdocs.cih.common.core.sn.ApplicationContext;
import com.amdocs.cih.common.datatypes.OrderingContext;
import com.amdocs.cih.exception.InvalidUsageException;
import com.amdocs.cih.services.oms.exceptions.ExternalSystemException;
import com.amdocs.cih.services.oms.interfaces.IOmsServicesRemote;
import com.amdocs.cih.services.order.lib.OrderID;
import com.amdocs.cih.services.order.lib.OrderRef;
import com.amdocs.cih.services.order.lib.RetrieveOrderSummaryInput;
import com.amdocs.cih.services.order.lib.RetrieveOrderSummaryOutput;
import com.example.oc.mspoc.datatype.RetrieveOrderSummaryOutputWrapper;
import com.example.oc.mspoc.service.legacy.OrderingServiceProvider;

import amdocs.oms.connector.Log;
import amdocs.oms.connector.Message;
import amdocs.oms.connector.SerializedObjectLogger;

@Service
public class OrderSummaryServiceProvider implements OrderingServiceProvider {

	@Value("${app.oms.uams.user}")
	private String uamsUser;
	
	@Value("${app.oms.uams.password}")
	private String uamsPassword;
	
	@Value("${app.oms.pt61.host}")
	private String omsHost;
	
	@Value("${app.oms.pt61.port}")
	private String omsPort;
	
	private static OmsServiceInvoker ejbService = null;
	private static IOmsServicesRemote iOmsServicesRemote = null;
	
	private final static String encoding = "UTF-8";
	private final static String fname = "C:\\workspace\\soc9\\logs\\RetrieveOrderSummaryPOC_" + System.currentTimeMillis()+ ".xml";
	
	public OrderSummaryServiceProvider() {}
	
	public void initialize() {
    	try {
    		ejbService = OmsServiceInvoker.getInstance(omsHost, omsPort, uamsUser, uamsPassword);
			//ejbService.login();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public RetrieveOrderSummaryOutputWrapper invoke(String orderId) {
		
		try {
			
			if (ejbService == null) initialize();
			
			System.out.println("> ORDER SUMMARY service is invoked");
			
			iOmsServicesRemote = ejbService.getServiceInvoker();
			
			RetrieveOrderSummaryInput obj = new RetrieveOrderSummaryInput();
			
			amdocs.oms.connector.Log clientLog = Log.getLog(this.getClass().getName());
			Log.initLog(fname, encoding, 3);
			//serialVersionUID, Locked, JDK_VERSION
			clientLog.logMessage(1, Message.START, "START Logging");
			
			OrderRef[] value = new OrderRef[1];
			//String orderId = "535119720A";
			OrderRef tmp = new OrderRef();
			OrderID val = new OrderID();
//		EntityIDBaseAssist.
			val.setOrderID(orderId);
			tmp.setOrderID(val );
			value[0] = tmp;
			obj.setOrderRef(value );
			
			obj.setRetrieveDirectRelatedAvailableInfoOnly(true);
			
			obj.setReturnOneTimeForClosedOrders(true);
			
			OrderingContext ctx = ejbService.getOrderingContext();
//	    ctx.setSecurityToken(ticket);
			MaskInfo maskInfo = new MaskInfo();

			clientLog.logMessage(1, Message.STAY, "Input Parameter : " + orderId);
			logObject(clientLog, obj);
			System.out.println("API !!");
			long start = System.currentTimeMillis();
//	    System.out.println("Start time : " + system.c);
			RetrieveOrderSummaryOutput out = iOmsServicesRemote.retrieveOrderSummary(new ApplicationContext(), ctx, obj, maskInfo);
			long end = System.currentTimeMillis();
			System.out.println("time taken : " + (end - start));
			clientLog.logMessage(1, Message.STAY, "Result ");
			
			logObject(clientLog, out);
			
			/*for (int i = 0; i < out.getOrderSummary().length; i++)
			{
				OrderSummary osTmp = out.getOrderSummary(i);
				OrderHeader header = osTmp.getOrderHeader();
				Method[] list = header.getClass().getDeclaredMethods();
				for (int j = 0; j < header.getClass().getDeclaredFields().length; j++)
				{
					Field t = header.getClass().getDeclaredFields()[j];
					Method t1 = list[j];
					if (t1.getName().contains(t.getName()))
						clientLog.logMessage(Message.INFO, Message.STAY,"Obtaining " + t.getName() + " Value : " + t1.invoke(null, null));
						
				}
					clientLog.logMessage(Message.INFO, Message.STAY,"Obtaining parameter #" + header);
				
				clientLog.logMessage(Message.DEBUG, Message.NONUM, "***  Object # " + (i+1) + " :" + " ***");
			    logObject(clientLog, header);
				
				
			}*/
			clientLog.logMessage(1, Message.STOP, "End Logging");
			System.out.println("Finsihed !!");
			clientLog.flush();
			
			return new RetrieveOrderSummaryOutputWrapper(out);
		} catch (InvalidUsageException | ExternalSystemException | RemoteException | SecurityException | IllegalArgumentException | NamingException | CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void logObject(Log clientLog, Object objectToLog) 
	{
		if (objectToLog == null)
		{
			clientLog.logMessage(Message.DEBUG, Message.NONUM, "null");
		}
		else
		{
			String returnedObjPackageName = objectToLog.getClass().getName();
			String returnedObjName = returnedObjPackageName.substring(returnedObjPackageName.lastIndexOf(".")+1);
			clientLog.logMessage(Message.DEBUG, Message.NONUM, SerializedObjectLogger.serializeObject(returnedObjName, objectToLog));
		}
	}

}
