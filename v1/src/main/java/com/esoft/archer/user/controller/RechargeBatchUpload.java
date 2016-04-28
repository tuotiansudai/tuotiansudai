package com.esoft.archer.user.controller;

import java.io.InputStream;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserBillService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;


@Component
@Scope(ScopeType.VIEW)
public class RechargeBatchUpload {
	@Resource
	private UserBillService ubs;
	
	@Resource
	private HibernateTemplate ht ;
	
	@Logger
	private static Log log ;
	
	/*public static void main(String[] args) {
		batchImport("d:\\1\\rechargeList.xls");
	}*/
	
	private String fileName ;
	
	
	public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public String batchImport(){
        if( file == null){
        	FacesUtil.addErrorMessage("未发现上传的文件！请先上传文件后，在导入表格！");
        	return null ;
        }
        
        if(file.getFileName().matches("^.+\\.(?i)(xlsx)$")){
        	FacesUtil.addErrorMessage("系统只支持Excel 2003格式的excel文件！");
        	return null ;
        }
        int i = 1;
        try{
           
            InputStream fis = file.getInputstream();
            HSSFWorkbook wbs = new HSSFWorkbook(fis);
            HSSFSheet childSheet = wbs.getSheetAt(0);
            /*if(log.isDebugEnabled()){
            	log.debug("总行数:" + childSheet.getLastRowNum());
            	
            }*/
            
            
//            for(int i = 1;i<=childSheet.getLastRowNum();i++){
            HSSFRow row = childSheet.getRow(i);
            while(row != null){
            	row = childSheet.getRow(i);
                //System.out.println("列数:" + row.getPhysicalNumberOfCells());
                if(log.isDebugEnabled()){
                	log.debug("当前行:" + i);
                }
                if(null != row){
                	//FIXME: 判断用户是否存在
                	final String userId = StringUtils.trim(row.getCell(0).getStringCellValue());
                	if(StringUtils.isEmpty(userId)){
                		break ;
                	}
                	if(ht.get(User.class, userId) == null){
                		FacesUtil.addInfoMessage("批量导入，导入第"+i+"行失败！失败原因：用户名不存在。请检查上传结果！");
                		return null ;
                	}
                	
                	try {
						ubs.transferIntoBalance(
								StringUtils.trim(row.getCell(0).getStringCellValue()),
						 		row.getCell(1).getNumericCellValue(), 
						 		OperatorInfo.ADMIN_OPERATION, 
						 		row.getCell(2).getStringCellValue());
						
					} catch (Exception e) {
						log.error("导入失败"+row.getRowNum(),e);
						FacesUtil.addErrorMessage("批量导入失败！请检查上传结果！"+"失败行数第："+i +"行。");
						e.printStackTrace();
						return null ;
//						break ;
					}
                    
                	 i++;
                }
            }
//            }
        }catch(Exception e){
        	FacesUtil.addErrorMessage("批量导入失败！请检查上传结果！"+"失败行数第："+i +"行。");
			e.printStackTrace();
			return null ;
        }
        
        FacesUtil.addInfoMessage("文件上传成功！共导入"+i+"行。请检查上传结果！");
        //FIXME: 删除源文件
       return null ;
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	private UploadedFile file;
	 
    public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }
}
