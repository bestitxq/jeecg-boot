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
import org.jeecg.modules.system.entity.SysUserRegisterRule;
import org.jeecg.modules.system.service.ISysUserRegisterRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: 用户邀请注册规则表
* @Author: jeecg-boot
* @Date:   2021-03-10
* @Version: V1.0
*/
@Api(tags="用户邀请注册规则表")
@RestController
@RequestMapping("/sys/userRegisterRule")
@Slf4j
public class SysUserRegisterRuleController extends JeecgController<SysUserRegisterRule, ISysUserRegisterRuleService> {
   @Autowired
   private ISysUserRegisterRuleService sysUserRegisterRuleService;

   /**
    * 分页列表查询
    *
    * @param sysUserRegisterRule
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "用户邀请注册规则表-分页列表查询")
   @ApiOperation(value="用户邀请注册规则表-分页列表查询", notes="用户邀请注册规则表-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(SysUserRegisterRule sysUserRegisterRule,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<SysUserRegisterRule> queryWrapper = QueryGenerator.initQueryWrapper(sysUserRegisterRule, req.getParameterMap());
       Page<SysUserRegisterRule> page = new Page<SysUserRegisterRule>(pageNo, pageSize);
       IPage<SysUserRegisterRule> pageList = sysUserRegisterRuleService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    *   添加
    *
    * @param sysUserRegisterRule
    * @return
    */
   @AutoLog(value = "用户邀请注册规则表-添加")
   @ApiOperation(value="用户邀请注册规则表-添加", notes="用户邀请注册规则表-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody SysUserRegisterRule sysUserRegisterRule) {
       sysUserRegisterRuleService.save(sysUserRegisterRule);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param sysUserRegisterRule
    * @return
    */
   @AutoLog(value = "用户邀请注册规则表-编辑")
   @ApiOperation(value="用户邀请注册规则表-编辑", notes="用户邀请注册规则表-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody SysUserRegisterRule sysUserRegisterRule) {
       sysUserRegisterRuleService.updateById(sysUserRegisterRule);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "用户邀请注册规则表-通过id删除")
   @ApiOperation(value="用户邀请注册规则表-通过id删除", notes="用户邀请注册规则表-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       sysUserRegisterRuleService.removeById(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "用户邀请注册规则表-批量删除")
   @ApiOperation(value="用户邀请注册规则表-批量删除", notes="用户邀请注册规则表-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.sysUserRegisterRuleService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "用户邀请注册规则表-通过id查询")
   @ApiOperation(value="用户邀请注册规则表-通过id查询", notes="用户邀请注册规则表-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       SysUserRegisterRule sysUserRegisterRule = sysUserRegisterRuleService.getById(id);
       if(sysUserRegisterRule==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(sysUserRegisterRule);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param sysUserRegisterRule
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, SysUserRegisterRule sysUserRegisterRule) {
       return super.exportXls(request, sysUserRegisterRule, SysUserRegisterRule.class, "用户邀请注册规则表");
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
       return super.importExcel(request, response, SysUserRegisterRule.class);
   }


    /**
     * @description: 累计vip规则奖励展示接口 奖励天数
     * @author: bestitxq
     * @date: 2021/3/10 下午6:41
     */
    @GetMapping("/showVipRule")
    public Result showVipRule() {
        return Result.OK(sysUserRegisterRuleService.sumVipRule());
    }
}
