package com.baosight.buapx.ua.auth.example;

import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
*/
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;

import com.baosight.buapx.cassandra.client.domain.ClientConfig;
import com.baosight.buapx.cassandra.client.hector.HectorSimpleOperator;
import com.baosight.buapx.cassandra.client.interfaces.CassandraSimpleOperator;
import com.baosight.buapx.ua.auth.api.impl.CassandraBuapxAuthManager;
import com.baosight.buapx.ua.auth.common.EncryptUtils;
import com.baosight.buapx.ua.auth.constants.UserInfoConstants;
import com.baosight.buapx.ua.auth.domain.AuthUserInfo;
 

public class ExampleTest {
	
	
/*	public static void exportToExcel(List<Row<String,String,String>> l){
        try {
            HSSFWorkbook workbook= new HSSFWorkbook();
            HSSFSheet sheet= workbook.createSheet("test");
            for (int i=0;i<l.size();i++){
            	Row<String,String,String> row = l.get(i);
            	HSSFRow excelrow = sheet.createRow(i);
                HSSFCell cell= excelrow.createCell(0);
                cell.setCellValue(row.getKey());
              	
            }
            
            FileOutputStream os= null;
            os = new FileOutputStream("d:\\fisrtExcel.xls");
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ok");
    }*/
	
	public static void main4(String[] args) {
		   String host = "10.60.2.229:9160";//正式环境
    	   String clusterName = "Oy Cluster";
		
		
		ClientConfig config = new ClientConfig();
		config.setAddress(new String[] { host });
		config.setKeyspaceName("buapx");
		config.setUsername("admin");
		config.setPassword("password");
		config.setWriteConsistencyLevelPolicy(HConsistencyLevel.ONE);
		config.setReadConsistencyLevelPolicy(HConsistencyLevel.ONE);
		CassandraSimpleOperator operator = new HectorSimpleOperator(config);
		OrderedRows<String, String, String> list = operator.getRangedSlices("UserInfo", null, null, 100000, "userid");
		 List<Row<String,String,String>> l =list.getList();
//		 for (Row<String,String,String> row: l){
//			 System.out.println("row ="+row.getKey());
//		 }
		//exportToExcel( l);
	 
		System.out.println("coutn="+list.getCount());
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//P103842 		a123456
		//P104160 		z11111
		String[] userids = new String[] { "admin"}; //個人 PERSONAL
		String[] userids0 = new String[] {  "P101440",	"U18432", "U18469", "P225" }; //個人 PERSONAL
		
		String[] userids1 = new String[] { "F360C1","U94310"};  //企業COMPANY
		
		String[] userids2 = new String[] { "F1000019C2","F1000019C3"};  //企業COMPANY
	
		String[] userids3 = new String[] { "P103842" }; //個人 PERSONAL
		
		// String userName="管理员";
		String password = "password";
		String sysCode = "buapx";

		String host = "172.16.18.135:9160"; //开发环境
    	String clusterName = "Test Cluster";
		
		//String host = "10.46.20.59:9160"; //验证环境
    	//String clusterName = "buapx_cluster";
    	
		 // String host = "10.60.2.226:9160";//正式环境
    	//  String clusterName = "Oy Cluster";
		
		
		ClientConfig config = new ClientConfig();
		config.setAddress(new String[] { host });
		config.setKeyspaceName("buapx");
		config.setUsername("admin");
		config.setPassword("password");
		config.setWriteConsistencyLevelPolicy(HConsistencyLevel.ONE);
		config.setReadConsistencyLevelPolicy(HConsistencyLevel.ONE);
		CassandraSimpleOperator operator = new HectorSimpleOperator(config);
		CassandraBuapxAuthManager manager = new CassandraBuapxAuthManager(
				operator);
		manager.setUserMappingColumnFamily("UserMapping");
		manager.setUserInfoColumnFamily("UserInfo");
		try {
			/*
			 * manager.addLoginAlias(userid, userids1);
			 */
			for (String userid : userids) {
//				user.setUserid(userid);
//				user.setDisplayName(userid+"Name");
				//user.setUserType(UserInfoConstants.USERTYPE_COMPANY);//公司
				//user.setUserType(UserInfoConstants.USERTYPE_PERSONAL);//个人
				
				//user.setEncVersion("md5");//md5加密
				//user.setEncVersion("sha");//sha加密
				
			    //user.setPassword(EncryptUtils.passwordEncode(password));
				
//				password = userid;
				 
//			    user.setPassword(EncryptUtils.passwordEncode(password) );
//			    Date now = new Date();
			    //Date expdate =new Date( now.getTime()+1000*3600*24*90 );
//				user.setExpiryDate(now);
//				manager.addUser(user);

//				manager.activeUser(userid);
				
				//manager.removeLoginAlias(userid, userid);
				//manager.removeUser(userid);

				// manager.updatePwdExpiryDate(userid, new
				// Date(System.currentTimeMillis()+1000*60*24*365));
				// 修改密码
				 AuthUserInfo user =manager.queryUserInfo(userid);
				 user.setPassword(EncryptUtils.passwordEncode(password));
				 manager.addUser(user);
				 System.out.println();

				// 增加映射
				//manager.addPermissionToSystem(userid, sysCode, userid);
				
				
				// manager.removePermissionToSystem(sysCode, userid);
				// System.out.println(manager.querySourceLoginName(userid,
				// sysCode));
				// UserInfo newUser=manager.queryUserInfo(userid);
				// System.out.println(newUser.getPassword());
				// System.out.println(EncrypUtil.codedPassword(password));
				// System.out.println(newUser.getExpiryDate());
				// System.out.println(newUser.getHasActived());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("运行完毕");

	}

}
