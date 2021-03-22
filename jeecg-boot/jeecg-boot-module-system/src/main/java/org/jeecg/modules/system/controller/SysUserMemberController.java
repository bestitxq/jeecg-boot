package org.jeecg.modules.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
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
import org.jeecg.common.constant.DictConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.SysUserMember;
import org.jeecg.modules.system.service.ISysUserMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 用户会员表
 * @Author: jeecg-boot
 * @Date:   2021-03-10
 * @Version: V1.0
 */
@Api(tags="用户会员表")
@RestController
@RequestMapping("/sys/userMember")
@Slf4j
public class SysUserMemberController extends JeecgController<SysUserMember, ISysUserMemberService> {
	@Autowired
	private ISysUserMemberService sysUserMemberService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysUserMember
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "用户会员表-分页列表查询")
	@ApiOperation(value="用户会员表-分页列表查询", notes="用户会员表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SysUserMember sysUserMember,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysUserMember> queryWrapper = QueryGenerator.initQueryWrapper(sysUserMember, req.getParameterMap());
		Page<SysUserMember> page = new Page<SysUserMember>(pageNo, pageSize);
		IPage<SysUserMember> pageList = sysUserMemberService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sysUserMember
	 * @return
	 */
	@AutoLog(value = "用户会员表-添加")
	@ApiOperation(value="用户会员表-添加", notes="用户会员表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysUserMember sysUserMember) {
		sysUserMemberService.save(sysUserMember);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sysUserMember
	 * @return
	 */
	@AutoLog(value = "用户会员表-编辑")
	@ApiOperation(value="用户会员表-编辑", notes="用户会员表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysUserMember sysUserMember) {
		sysUserMemberService.updateById(sysUserMember);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户会员表-通过id删除")
	@ApiOperation(value="用户会员表-通过id删除", notes="用户会员表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysUserMemberService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "用户会员表-批量删除")
	@ApiOperation(value="用户会员表-批量删除", notes="用户会员表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysUserMemberService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户会员表-通过id查询")
	@ApiOperation(value="用户会员表-通过id查询", notes="用户会员表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysUserMember sysUserMember = sysUserMemberService.getById(id);
		if(sysUserMember==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sysUserMember);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysUserMember
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysUserMember sysUserMember) {
        return super.exportXls(request, sysUserMember, SysUserMember.class, "用户会员表");
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
        return super.importExcel(request, response, SysUserMember.class);
    }

	/**
	 * @description: 用户会员时间信息
	 * @author: bestitxq
	 * @date: 2021/3/10 下午6:41
	 */
	@GetMapping("/memberTime")
	public Result memberTime() {
		// 获取当前登录用户
		LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String userId = user.getId();

		LambdaQueryWrapper<SysUserMember> ew = Wrappers.lambdaQuery();
		ew.eq(SysUserMember::getStatus, DictConstant.YN_YES);
		ew.eq(SysUserMember::getUserId, userId);
		List<SysUserMember> list = sysUserMemberService.list(ew);
		if(CollectionUtil.isEmpty(list)){
			return Result.error("未查询到用户的会员记录信息");
		}
		DateTime nowDate = DateUtil.date();
		Date endTime = list.get(0).getEndTime();

		if(nowDate.after(endTime)){
			return Result.error("用户会员已过期");
		}
		// 剩余会员天数：计算中间间隔多少天
		long betDay = DateUtil.between(nowDate, endTime, DateUnit.DAY);
		return Result.OK(betDay);
	}

}
