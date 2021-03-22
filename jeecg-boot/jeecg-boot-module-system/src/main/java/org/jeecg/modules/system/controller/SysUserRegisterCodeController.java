package org.jeecg.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysUserRegisterCode;
import org.jeecg.modules.system.service.ISysUserRegisterCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: 用户注册邀请码记录表
* @Author: jeecg-boot
* @Date:   2021-03-10
* @Version: V1.0
*/
@Api(tags="用户注册邀请码记录表")
@RestController
@RequestMapping("/sys/userRegisterCode")
@Slf4j
public class SysUserRegisterCodeController extends JeecgController<SysUserRegisterCode, ISysUserRegisterCodeService> {
   @Autowired
   private ISysUserRegisterCodeService sysUserRegisterCodeService;

   /**
    * 分页列表查询
    *
    * @param sysUserRegisterCode
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "用户注册邀请码记录表-分页列表查询")
   @ApiOperation(value="用户注册邀请码记录表-分页列表查询", notes="用户注册邀请码记录表-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(SysUserRegisterCode sysUserRegisterCode,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<SysUserRegisterCode> queryWrapper = QueryGenerator.initQueryWrapper(sysUserRegisterCode, req.getParameterMap());
       Page<SysUserRegisterCode> page = new Page<SysUserRegisterCode>(pageNo, pageSize);
       IPage<SysUserRegisterCode> pageList = sysUserRegisterCodeService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    *   添加
    *
    * @param sysUserRegisterCode
    * @return
    */
   @AutoLog(value = "用户注册邀请码记录表-添加")
   @ApiOperation(value="用户注册邀请码记录表-添加", notes="用户注册邀请码记录表-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody SysUserRegisterCode sysUserRegisterCode) {
       sysUserRegisterCodeService.save(sysUserRegisterCode);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param sysUserRegisterCode
    * @return
    */
   @AutoLog(value = "用户注册邀请码记录表-编辑")
   @ApiOperation(value="用户注册邀请码记录表-编辑", notes="用户注册邀请码记录表-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody SysUserRegisterCode sysUserRegisterCode) {
       sysUserRegisterCodeService.updateById(sysUserRegisterCode);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "用户注册邀请码记录表-通过id删除")
   @ApiOperation(value="用户注册邀请码记录表-通过id删除", notes="用户注册邀请码记录表-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       sysUserRegisterCodeService.removeById(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "用户注册邀请码记录表-批量删除")
   @ApiOperation(value="用户注册邀请码记录表-批量删除", notes="用户注册邀请码记录表-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.sysUserRegisterCodeService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "用户注册邀请码记录表-通过id查询")
   @ApiOperation(value="用户注册邀请码记录表-通过id查询", notes="用户注册邀请码记录表-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       SysUserRegisterCode sysUserRegisterCode = sysUserRegisterCodeService.getById(id);
       if(sysUserRegisterCode==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(sysUserRegisterCode);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param sysUserRegisterCode
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, SysUserRegisterCode sysUserRegisterCode) {
       return super.exportXls(request, sysUserRegisterCode, SysUserRegisterCode.class, "用户注册邀请码记录表");
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
       return super.importExcel(request, response, SysUserRegisterCode.class);
   }

}
