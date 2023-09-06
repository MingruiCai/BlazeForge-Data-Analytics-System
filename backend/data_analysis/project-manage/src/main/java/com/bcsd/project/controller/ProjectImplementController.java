package com.bcsd.project.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.bcsd.common.annotation.Anonymous;
import com.bcsd.common.config.BcsdConfig;
import com.bcsd.common.constant.Constants;
import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.common.utils.file.FileUtils;
import com.bcsd.project.domain.ProjectImplBasicInfo;
import com.bcsd.project.domain.vo.ProjectFileDownloadVO;
import com.bcsd.project.domain.vo.ProjectImplInfoVO;
import com.bcsd.project.domain.vo.VerifyVO;
import com.bcsd.project.domain.vo.ZipVO;
import com.bcsd.project.service.ProjectImplService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 项目实施 控制器
 * @author liuliang
 * @since 2023-02-20
 */
@Slf4j
@RestController
@RequestMapping("/project/impl")
public class ProjectImplementController extends BaseController {

    @Autowired
    private ProjectImplService implService;
    @Autowired
    HttpServletResponse response;

    /**
     * 获取详情
     */
    @PostMapping("/info/{id}")
    public AjaxResult info(@PathVariable("id") Long id){
        return success(implService.getById(id));
    }

    /**
     * 分页列表
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody ProjectImplBasicInfo params){
        Map<String,Object> map = params.getParams();
        map.put("roleKey",getRoles().get(0).getRoleKey());
        startPage(params);
        List<ProjectImplBasicInfo> list = implService.list(params);
        return getDataTable(list,true);
    }

    /**
     * 编辑
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody ProjectImplInfoVO params){
        params.setUserName(getUsername());
        params.setRoleKeys(getRoleKeys());
        return implService.update(params);
    }

    /**
     * 编辑不带审核
     */
    @PostMapping("/not/status/update")
    public AjaxResult updateNotStatus(@RequestBody ProjectImplInfoVO params){
        params.setUserName(getUsername());
        params.setRoleKeys(getRoleKeys());
        return implService.updateNotStatus(params);
    }

    /**
     * 审核
     */
    @PostMapping("/verify")
    public AjaxResult verify(@RequestBody VerifyVO params){
        params.setUserName(getUsername());
        params.setRoleKeys(getRoleKeys());
        return implService.verify(params);
    }

    /**
     * 批量审核
     */
    @PostMapping("/batch/verify")
    public AjaxResult batchVerify(@RequestBody VerifyVO params){
        if (StringUtils.isEmpty(params.getIds())){
            return error("ID集合不能为空！");
        }
        params.setUserName(getUsername());
        params.setRoleKeys(getRoleKeys());
        implService.batchVerify(params);
        return success();
    }

    /**
     * 历史列表
     * @param id
     * @return
     */
    @PostMapping("/history/list/{id}")
    public AjaxResult historyList(@PathVariable("id") Long id){
        return success(implService.historyList(id));
    }

    /**
     * 历史明细
     * @param id
     * @return
     */
    @PostMapping("/history/detail/{id}")
    public AjaxResult historyDetail(@PathVariable("id") Long id){
        return success(implService.getHistoryDetail(id));
    }

    /**
     * 删除历史记录
     * @param id
     * @return
     */
    @PostMapping("/history/delete/{id}")
    public AjaxResult deleteHistory(@PathVariable("id") Long id){
        return success(implService.deleteHistory(id,getUsername()));
    }

    /**
     * 批量下载
     * @param params
     * @return
     * @throws IOException
     */
    @Anonymous
    @GetMapping("/batch/download")
    public AjaxResult batchDownload(ProjectFileDownloadVO params) throws IOException {
        if (StringUtils.isEmpty(params.getProjectNos())){
            return error("至少选择1个项目!");
        }
        if (params.getProjectNos().size()>5){
            return error("批量下载不能超过5个项目！");
        }
        //查询所有项目
        List<ZipVO> list = implService.getZipFiles(params);
        if (StringUtils.isEmpty(list)){
            return error("无附件！");
        }
        ZipOutputStream os = null;
        try {
            FileUtils.setAttachmentResponseHeader(response, DateUtils.dateTimeNow()+".zip");
            os = new ZipOutputStream(response.getOutputStream());
            for (ZipVO vo:list) {
                //压缩文件
                os.putNextEntry(new ZipEntry(vo.getFileName()));
                //数据库资源地址
                String downloadPath = BcsdConfig.getProfile() + StringUtils.substringAfter(vo.getUrl(), Constants.RESOURCE_PREFIX);
                FileInputStream fis = new FileInputStream(downloadPath);
                //写入压缩包
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    os.write(buffer,0,len);
                }
                //输出完成后刷新并关闭流
                fis.close();
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return error("批量下载失败，请联系管理员！");
        }finally {
            IOUtils.close(os);
        }
        return success();
    }

    /**
     * 项目地图点位
     * @return
     */
    @PostMapping("/map/points")
    public AjaxResult mapPoints(){
        return success(implService.getGPData());
    }

    /**
     * 获取所有项目编号和名称
     * @return
     */
    @PostMapping("/all")
    public AjaxResult getAll(){
        return success(implService.getProjectAll());
    }

    /**
     * 根据编号获取项目名称
     * @return
     */
    @PostMapping("/get/{no}")
    public AjaxResult getByNo(@PathVariable("no") String no){
        return success(implService.getProjectNameByNo(no));
    }

    /**
     * 导出
     * @return
     * @throws IOException
     */
    @PostMapping("/export")
    public AjaxResult export() throws IOException {
        //获取数据
        List data = implService.export();
        if (data==null){
            return error("无数据！");
        }
        //设置导出文件名
        FileUtils.setAttachmentResponseHeader(response, "项目实施信息.xlsx");
        //获取模板并导出
        InputStream is = this.getClass().getResourceAsStream("/template/project_info_template.xlsx");
        try(ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(is).build()){
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            excelWriter.fill(data,writeSheet);
        }
        return success();
    }

    /**
     * 撤回
     * @param id
     * @return
     */
    @PostMapping("/rollback/{id}")
    public AjaxResult rollback(@PathVariable("id") Long id){
        VerifyVO params = new VerifyVO();
        params.setId(id);
        params.setRoleKeys(getRoleKeys());
        params.setUserName(getUsername());
        return implService.rollback(params);
    }

    /**
     * 修复数据
     * @return
     */
    @PostMapping("/repair/data")
    public AjaxResult repairData(){
        //implService.repairData();
        return success();
    }



}

