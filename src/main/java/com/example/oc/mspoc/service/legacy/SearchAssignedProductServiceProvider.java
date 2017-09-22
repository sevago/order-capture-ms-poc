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
public class SearchAssignedProductServiceProvider extends OrderingServiceProviderImpl {

	private String fname = "C:\\oms\\omstests\\log\\" + this.getClass().getName() + "_" + System.currentTimeMillis()+ ".log";
	
	public SearchAssignedProductServiceProvider() {}
	
	@Override
	public SearchAssignedProductOutputWrapper invoke(String customerId) {
			
		if (ejbService == null) initialize();
		
		System.out.println("> SEARCH ASSIGNED PRODUCT service is invoked");
			
		amdocs.oms.connector.Log clientLog = Log.getLog(this.getClass().getName());
		Log.initLog(fname, encoding, 3);
		clientLog.logMessage(1, Message.START, "START Logging");
		
		SelectionCriteria obj = new SelectionCriteria();		
		List<CihFilterInfo> filterList = new ArrayList<CihFilterInfo>();		
		filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("CustomerProfileID", new String[]{customerId}, CihStringOperator.EQUAL));
		filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("SubLevelReturnInd", new String[] { new String("AL") }, CihStringOperator.EQUAL));
		filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("ProductState", new String[] { new String("BO") }, CihStringOperator.EQUAL));	
		filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("IncludeAvailableAction", new Boolean[] { true }, CihStringOperator.EQUAL));
		filterList.addAll(OmsServiceInvoker.buildNewCihFilterInfo("MaxNumberOfFullProducts", new Integer[]{100}, CihStringOperator.EQUAL));
		CihFilterInfo[] filters = (CihFilterInfo[])filterList.toArray(new CihFilterInfo[filterList.size()]);
		
		obj.setFilterCriteria(filters);
		obj.setSortCriteria(new CihSortInfo[0]);
		
	    MaskInfo maskInfo = new MaskInfo();	    
	    PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setMaxListSize(2000);
		SearchAssignedProductOutput out = null;
		long avrg = 0;
		
	    System.out.println("API !!");    
	    long start = System.currentTimeMillis();
	    
	    try {
			out = iOmsServicesRemote.searchAssignedProduct(new ApplicationContext(), ctx, obj, null, new PaginationInfo(), maskInfo);
		} catch (InvalidUsageException | RemoteException e) {
			e.printStackTrace();
		}
	    
	    long end = System.currentTimeMillis();
	    System.out.println("time taken : " + (end - start));
	    avrg = avrg + (end - start);
	    
	    System.out.println("avg time taken : " + avrg);
	    clientLog.logMessage(1, Message.STAY, "Result ");	
	    logObject(clientLog, out);
		clientLog.logMessage(1, Message.STOP, "End Logging");
		System.out.println("Finsihed !!");
		clientLog.flush();
		
		return new SearchAssignedProductOutputWrapper(out);
	}

}
