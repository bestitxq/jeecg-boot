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
import org.jeecg.modules.system.entity.SysUserRegister;
import org.jeecg.modules.system.service.ISysUserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: 用户注册信息表
* @Author: jeecg-boot
* @Date:   2021-03-10
* @Version: V1.0
*/
@Api(tags="用户注册信息表")
@RestController
@RequestMapping("/sys/userRegister")
@Slf4j
public class SysUserRegisterController extends JeecgController<SysUserRegister, ISysUserRegisterService> {
   @Autowired
   private ISysUserRegisterService sysUserRegisterService;

   /**
    * 分页列表查询
    *
    * @param sysUserRegister
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "用户注册信息表-分页列表查询")
   @ApiOperation(value="用户注册信息表-分页列表查询", notes="用户注册信息表-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(SysUserRegister sysUserRegister,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<SysUserRegister> queryWrapper = QueryGenerator.initQueryWrapper(sysUserRegister, req.getParameterMap());
       Page<SysUserRegister> page = new Page<SysUserRegister>(pageNo, pageSize);
       IPage<SysUserRegister> pageList = sysUserRegisterService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    *   添加
    *
    * @param sysUserRegister
    * @return
    */
   @AutoLog(value = "用户注册信息表-添加")
   @ApiOperation(value="用户注册信息表-添加", notes="用户注册信息表-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody SysUserRegister sysUserRegister) {
       sysUserRegisterService.save(sysUserRegister);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param sysUserRegister
    * @return
    */
   @AutoLog(value = "用户注册信息表-编辑")
   @ApiOperation(value="用户注册信息表-编辑", notes="用户注册信息表-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody SysUserRegister sysUserRegister) {
       sysUserRegisterService.updateById(sysUserRegister);
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
       sysUserRegisterService.removeById(id);
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
       this.sysUserRegisterService.removeByIds(Arrays.asList(ids.split(",")));
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
       SysUserRegister sysUserRegister = sysUserRegisterService.getById(id);
       if(sysUserRegister==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(sysUserRegister);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param sysUserRegister
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, SysUserRegister sysUserRegister) {
       return super.exportXls(request, sysUserRegister, SysUserRegister.class, "用户注册信息表");
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
       return super.importExcel(request, response, SysUserRegister.class);
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
        LambdaQueryWrapper<SysUserRegister> qw = Wrappers.lambdaQuery();
        qw.eq(SysUserRegister::getUserId, userId);
        return Result.OK(sysUserRegisterService.count(qw));
    }
}
