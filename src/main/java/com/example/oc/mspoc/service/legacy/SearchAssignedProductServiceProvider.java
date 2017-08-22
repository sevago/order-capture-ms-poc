package com.example.oc.mspoc.service.legacy;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amdocs.cih.common.core.CihFilterInfo;
import com.amdocs.cih.common.core.CihSortInfo;
import com.amdocs.cih.common.core.CihStringOperator;
import com.amdocs.cih.common.core.MaskInfo;
import com.amdocs.cih.common.core.PaginationInfo;
import com.amdocs.cih.common.core.sn.ApplicationContext;
import com.amdocs.cih.common.datatypes.OrderingContext;
import com.amdocs.cih.exception.InvalidUsageException;
import com.amdocs.cih.services.assignedproduct.lib.SearchAssignedProductOutput;
import com.amdocs.cih.services.oms.interfaces.IOmsServicesRemote;
import com.amdocs.cih.services.oms.lib.SelectionCriteria;
import com.example.oc.mspoc.model.SearchAssignedProductOutputWrapper;

import amdocs.oms.connector.Log;
import amdocs.oms.connector.Message;
import amdocs.oms.connector.SerializedObjectLogger;

@Service
public class SearchAssignedProductServiceProvider implements OrderingServiceProvider {
	
	@Value("${app.oms.uams.user}")
	private String uamsUser;
	
	@Value("${app.oms.uams.password}")
	private String uamsPassword;
	
	@Value("${app.oms.host}")
	private String omsHost;
	
	@Value("${app.oms.port}")
	private String omsPort;
	
	private static OmsServiceInvoker ejbService = null;
	private static IOmsServicesRemote iOmsServicesRemote = null;
	
	private final static String encoding = "UTF-8";
	private final static String fname = "C:\\workspace\\soc9\\logs\\SearchAssignedProductServicePOC_" + System.currentTimeMillis()+ ".xml";
	
	public SearchAssignedProductServiceProvider() {}
	
	public void initialize() {		
    	try {
    		ejbService = OmsServiceInvoker.getInstance(omsHost, omsPort, uamsUser, uamsPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public SearchAssignedProductOutputWrapper invoke(String customerId) {
		
		try {
			
			if (ejbService == null) initialize();
			
			System.out.println("> SEARCH ASSIGNED PRODUCT service is invoked");
			
			iOmsServicesRemote = ejbService.getServiceInvoker();
			
			amdocs.oms.connector.Log clientLog = Log.getLog(this.getClass().getName());
			clientLog.initLog(fname, encoding, 3);
			//serialVersionUID, Locked, JDK_VERSION
			clientLog.logMessage(1, Message.START, "START Logging");
			
			SelectionCriteria obj = new SelectionCriteria();
			
			
			List<CihFilterInfo> filterList = new ArrayList<CihFilterInfo>();

			
			filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("CustomerProfileID", new String[]{customerId}, CihStringOperator.EQUAL));
			
//			filterList.addAll(ServiceInvoker.buildNewCihFilterInfo("assignedProductReferencesForOA", new String[]{"1237029"},  CihStringOperator.EQUAL));
			
//			filterList.addAll(ServiceInvoker.buildNewCihFilterInfo("InstallationAddressID", new String[]{"1102994", "1102993"}, CihStringOperator.IN));
			
			filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("SubLevelReturnInd", new String[] { new String("AL") }, CihStringOperator.EQUAL));
			
			filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("ProductState", new String[] { new String("BO") }, CihStringOperator.EQUAL));
			
			
			filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("IncludeAvailableAction", new Boolean[] { true }, CihStringOperator.EQUAL));
			
//			filterList.addAll(ServiceInvoker.buildNewCihFilterInfo("FMSAddressID", new String[] { new String("123456789") }, CihStringOperator.EQUAL));
			
			filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("MaxNumberOfFullProducts", new Integer[]{100}, CihStringOperator.EQUAL));
			
//			filterList.addAll(ServiceInvoker.buildNewCihFilterInfo("AssignedProducts", new String[] { "300782092", "300782092" }, CihStringOperator.EQUAL));
			
			CihFilterInfo[] filters = (CihFilterInfo[])filterList.toArray(new CihFilterInfo[filterList.size()]);
			
			obj.setFilterCriteria(filters);
			obj.setSortCriteria(new CihSortInfo[0]);
			
			OrderingContext ctx = ejbService.getOrderingContext();
		    MaskInfo maskInfo = new MaskInfo();
		    
		    
		    PaginationInfo paginationInfo = new PaginationInfo();
			paginationInfo.setMaxListSize(2000);
			SearchAssignedProductOutput out = null;
			//----------------------
			long avrg = 0;
			
		    System.out.println("API !!");
		    for (int i = 0; i < 1; i++)
			{
			    long start = System.currentTimeMillis();
	//		    System.out.println("Start time : " + system.c);
			    out = iOmsServicesRemote.searchAssignedProduct(new ApplicationContext(), ctx, obj, null, new PaginationInfo(), maskInfo);
			    
	
			    long end = System.currentTimeMillis();
			    System.out.println("time taken : " + (end - start));
			    avrg = avrg + (end - start);
			}
		    
		    System.out.println("avg time taken : " + avrg);
		    
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
			
			return new SearchAssignedProductOutputWrapper(out);
		} catch (InvalidUsageException | RemoteException | SecurityException | IllegalArgumentException | NamingException | CreateException e) {
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
