package org.jeecg.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.SysUserRegisterRecord;
import org.jeecg.modules.system.service.ISysUserRegisterRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: 用户注册信息表
* @Author: bestitxq
* @Date:   2021-03-10
* @Version: V1.0
*/
@Api(tags="用户注册信息表")
@RestController
@RequestMapping("/sys/userRegisterRecord")
@Slf4j
public class SysUserRegisterRecordController extends JeecgController<SysUserRegisterRecord, ISysUserRegisterRecordService> {
   @Autowired
   private ISysUserRegisterRecordService sysUserRegisterRecordService;

   /**
    * 分页列表查询
    *
    * @param sysUserRegisterRecord
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "用户注册信息表-分页列表查询")
   @ApiOperation(value="用户注册信息表-分页列表查询", notes="用户注册信息表-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(SysUserRegisterRecord sysUserRegisterRecord,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<SysUserRegisterRecord> queryWrapper = QueryGenerator.initQueryWrapper(sysUserRegisterRecord, req.getParameterMap());
       Page<SysUserRegisterRecord> page = new Page<SysUserRegisterRecord>(pageNo, pageSize);
       IPage<SysUserRegisterRecord> pageList = sysUserRegisterRecordService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    *   添加
    *
    * @param sysUserRegisterRecord
    * @return
    */
   @AutoLog(value = "用户注册信息表-添加")
   @ApiOperation(value="用户注册信息表-添加", notes="用户注册信息表-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody SysUserRegisterRecord sysUserRegisterRecord) {
       sysUserRegisterRecordService.save(sysUserRegisterRecord);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param sysUserRegisterRecord
    * @return
    */
   @AutoLog(value = "用户注册信息表-编辑")
   @ApiOperation(value="用户注册信息表-编辑", notes="用户注册信息表-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody SysUserRegisterRecord sysUserRegisterRecord) {
       sysUserRegisterRecordService.updateById(sysUserRegisterRecord);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "用户注册信息表-通过id删除")
   @ApiOperation(value="用户注册信息表-通过id删除", notes="用户注册信息表-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       sysUserRegisterRecordService.removeById(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "用户注册信息表-批量删除")
   @ApiOperation(value="用户注册信息表-批量删除", notes="用户注册信息表-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.sysUserRegisterRecordService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "用户注册信息表-通过id查询")
   @ApiOperation(value="用户注册信息表-通过id查询", notes="用户注册信息表-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       SysUserRegisterRecord sysUserRegisterRecord = sysUserRegisterRecordService.getById(id);
       if(sysUserRegisterRecord==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(sysUserRegisterRecord);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param sysUserRegisterRecordService
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, SysUserRegisterRecord sysUserRegisterRecordService) {
       return super.exportXls(request, sysUserRegisterRecordService, SysUserRegisterRecord.class, "用户注册信息表");
   }

   /**
     * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
       return super.importExcel(request, response, SysUserRegisterRecord.class);
   }

    /**
     * @description: 用户累计已邀请注册成功人数
     * @author: bestitxq
     * @date: 2021/3/10 下午6:41
     */
    @GetMapping("/getInviteUserNum")
    public Result getInviteUserNum() {
        // 获取当前登录用户
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = user.getId();
        LambdaQueryWrapper<SysUserRegisterRecord> qw = Wrappers.lambdaQuery();
        qw.eq(SysUserRegisterRecord::getUserId, userId);
        return Result.OK(sysUserRegisterRecordService.count(qw));
    }
}
